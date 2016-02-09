package com.gamecodeschool.c1tappydefender;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Space;

import java.util.ArrayList;

/**
 * Created by Stacy on 5.2.2016 г..
 */

//controller - controls when it's time to update
public class TDView  extends SurfaceView implements Runnable{

    //to be accessed within and outside the thread
    volatile  boolean isPlaying;
    Thread gameThread = null;

    private int screenX;
    private int screenY;

    //Game objects
    private PlayerShip player;
    public EnemyShip enemy1;
    public EnemyShip enemy2;
    public EnemyShip enemy3;

    //game statistics variables
    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    //stardust
    public ArrayList<SpaceDust> dustList = new ArrayList<SpaceDust>();

    //for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder holder;

    //for initialization for a new game to start
    private Context context;
    

    public TDView(Context context, int x, int y) {
        super(context);
        this.context = context;

        //prepare for drawing - initialize drawing objects
        holder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        //initialize playership
        player = new PlayerShip(context,x,y);

        //initialize enemies
        enemy1 = new EnemyShip(context,x,y);
        enemy2 = new EnemyShip(context,x,y);
        enemy3 = new EnemyShip(context,x,y);

        //initialize the star dust objects
        int numSpecs = 40;
        for (int i = 0; i < numSpecs;i++ ){
            SpaceDust spec = new SpaceDust(x,y);
            dustList.add(spec);
        }
    }

    @Override
    public void run() {
        while(isPlaying){
            update();
            draw();
            control();
        }
    }

    public void update(){

        //collision detection on new positions before move because we are testing last frames
        //position which has just been drawn

        //if images excess 100 pixles wide then the -100 value must be increased accordingly
        if(Rect.intersects(player.getHitbox(),enemy1.getHitbox())){
            enemy1.setX(-100);
        }
        if(Rect.intersects(player.getHitbox(),enemy2.getHitbox())){
            enemy2.setX(-100);
        }
        if(Rect.intersects(player.getHitbox(),enemy3.getHitbox())){
            enemy3.setX(-100);
        }

        //update the player
        player.update();

        //update the enemies
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());

        //update the stardust objects
        for (SpaceDust sd : dustList){
            sd.update(player.getSpeed());
        }
    }

    public void draw(){
        if(holder.getSurface().isValid()){

            //lock the area of memory we will be drawing to
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.argb(255,0,0,0));

            /*
            //for debugging, set white color
            paint.setColor(Color.argb(255,255,255,255));

            //draw hit boxes
            canvas.drawRect(player.getHitbox().left,player.getHitbox().top,
                            player.getHitbox().right,player.getHitbox().bottom,
                            paint);

            canvas.drawRect(enemy1.getHitbox().left,enemy1.getHitbox().top,
                            enemy1.getHitbox().right,enemy1.getHitbox().bottom,
                            paint);

            canvas.drawRect(enemy2.getHitbox().left,enemy2.getHitbox().top,
                            enemy2.getHitbox().right,enemy2.getHitbox().bottom,
                            paint);

            canvas.drawRect(enemy3.getHitbox().left,enemy3.getHitbox().top,
                            enemy3.getHitbox().right,enemy3.getHitbox().bottom,
                            paint);

            */

            //set white color for spacedust
            paint.setColor(Color.argb(255,255,255,255));

            //draw stardust pixels
            for(SpaceDust sd : dustList){
                canvas.drawPoint(sd.getX(),sd.getY(),paint);
            }

            //draw the player
            canvas.drawBitmap(player.getBitmap(),player.getX(),player.getY(),paint);

            //draw the enemies
            canvas.drawBitmap(enemy1.getBitmap(),enemy1.getX(),enemy1.getY(),paint);
            canvas.drawBitmap(enemy2.getBitmap(),enemy2.getX(),enemy2.getY(),paint);
            canvas.drawBitmap(enemy3.getBitmap(),enemy3.getX(),enemy3.getY(),paint);

            //draw game statistics details (hud)
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.argb(255,255,255,255));
            paint.setTextSize(15);
            canvas.drawText("Fastest:"+fastestTime +"s",10,20,paint);
            canvas.drawText("Time:"+timeTaken +"s",screenX/2,20,paint);
            canvas.drawText("Distance:"+distanceRemaining/1000 +"km",screenX/3,screenY - 20,paint);
            canvas.drawText("Schield:"+player.getShieldStrength() +"s",10,screenY-20,paint);
            canvas.drawText("Speed:"+player.getSpeed()*60 +"mps",(screenX/3)*2,screenY-20,paint);

            //unlock the canvas and draw the scene
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void control(){
        try{
            gameThread.sleep(17);
        }catch (InterruptedException e){

        }
    }

    //clean up the thread if the game is interrupted or the player quits
    public void pause(){
        isPlaying = false;
        try{
            gameThread.join();
        }catch (InterruptedException e){

        }
    }

    //make a new thread and start it
    public void resume(){
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;
        }
        return true;

    }
}
