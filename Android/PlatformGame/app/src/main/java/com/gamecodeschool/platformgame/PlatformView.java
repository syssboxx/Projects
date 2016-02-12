package com.gamecodeschool.platformgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Stacy on 9.2.2016 г..
 */
public class PlatformView extends SurfaceView implements Runnable {

    private boolean isDebugging = true;
    private volatile boolean isRunning;
    private Thread gameThread = null;

    //for drawing
    private Paint paint;
    //canvas could be initially local but later will be used outside of draw
    private Canvas canvas;
    private SurfaceHolder holder;

    Context context;
    long startFrameTime;
    long timeThisFrame;
    long fps;

    //the engine classes
    private LevelManager levelManager;
    private Viewport viewport;
    private InputController inputController;

    //constructor
    public PlatformView(Context context, int screenWidth, int screenHeight){
        super(context);
        this.context = context;

        //initialize drawing objects
        holder = getHolder();
        paint = new Paint();

        //initialize the viewport object
        viewport = new Viewport(screenWidth,screenHeight);

        //load the first level
        loadLevel("LevelCave",15,2);

    }


    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run() {
        while (isRunning){
            startFrameTime = System.currentTimeMillis();

            update();
            draw();

            //calculate the fps this frame
            //this will be used for time animations and movement
            timeThisFrame = System.currentTimeMillis()-startFrameTime;
            if(timeThisFrame >= 1){
                fps = 1000/timeThisFrame;
            }
        }
    }

    public void update(){
        for (GameObject object : levelManager.gameObjects){
            if(object.isActive()){
                //clip anything ouside the screen
                if(!viewport.clipObjects(object.getWorldLocation().x,
                                        object.getWorldLocation().y,
                                        object.getHeight(),object.getWidth())){
                    //set the object visible
                    object.setVisible(true);
                }else{
                    //set the objects not visible, they will not be drawn
                    object.setVisible(false);
                }
            }
        }
    }

    public void draw(){
        if(holder.getSurface().isValid()){
            //first loch the area of memory we will be drawing on
            canvas = holder.lockCanvas();

            //fill the last frame with some arbitrary color
            paint.setColor(Color.argb(255,255,255,255));
            canvas.drawColor(Color.argb(255,255,255,255));

            //draw all game objects
            Rect toScreen2d = new Rect();

            //draw a layer on a time
            for (int layer = -1; layer <=1 ; layer++) {
                for(GameObject object : levelManager.gameObjects){
                    //draw only the visible objects
                    //and only from this layer
                    if(object.isVisible() && object.getWorldLocation().z == layer){
                        toScreen2d.set(viewport.worldToScreen(object.getWorldLocation().x,
                                                              object.getWorldLocation().y,
                                                              object.getWidth(),object.getHeight()));

                        //draw the appropriate bitmap
                        canvas.drawBitmap(levelManager.bitmapsArray[levelManager.getBitmapIndex(object.getType())],
                                                            toScreen2d.left,toScreen2d.top,paint);

                  }
                }
            }

            //draw text for debugging
            if(isDebugging){
                paint.setTextSize(16);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255,255,255,255));

                canvas.drawText("fps:"+fps,10,60,paint);
                canvas.drawText("num objects :"+levelManager.gameObjects.size(),10,80,paint);
                canvas.drawText("num clipped:"+viewport.getNumClipped(),10,100,paint);
                canvas.drawText("playerX:"+levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().x,
                        10,120,paint);
                canvas.drawText("playerY:"+levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().y,
                        10,140,paint);

                //reset the number of clipped object each frame
                viewport.resetNumClipped();
            }

            //unlock the canvas and draw the scene
            holder.unlockCanvasAndPost(canvas);
        }
    }

    //clean up the thread if the game is interrupted
    public void pause(){
        isRunning = false;
        try{
            gameThread.join();
        }catch (InterruptedException e){
            Log.e("error","failed to pause thread");
        }
    }

    //make a new thread and start it, on resuming the game
    public void resume(){
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    //level method

    //need to know which level and player coordinates to load
    public void loadLevel(String level, int px, int py){
        levelManager = null;

        //create a new levelManager
        levelManager = new LevelManager(context,viewport.getPixelsPerMeterX(),viewport.getScreenWidth(),
                inputController,level,px,py);

        //create a new input controller
        inputController = new InputController(viewport.getScreenWidth(),viewport.getScreenHeight());

        //set the players location as the world center
        //center the screen on the player object
        viewport.setWorldCenter(levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().x,
                                levelManager.gameObjects.get(levelManager.playerIndex).getWorldLocation().y);
    }
}
