package com.gamecodeschool.c1tappydefender;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Space;

import java.io.IOException;
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

    private boolean isGameEnded;

    //Game objects
    private PlayerShip player;
    public EnemyShip enemy1;
    public EnemyShip enemy2;
    public EnemyShip enemy3;
    public EnemyShip enemy4;
    public EnemyShip enemy5;

    //game statistics variables
    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    //for score persistence
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    //stardust
    public ArrayList<SpaceDust> dustList = new ArrayList<SpaceDust>();

    //for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder holder;

    //for sound effects
    private SoundPool soundPool;
    int start = -1;
    int bump = -1;
    int win = -1;
    int destroyed = -1;

    //for initialization for a new game to start
    private Context context;


    public TDView(Context context, int x, int y) {
        super(context);
        this.context = context;

        //preparing and initializing the sound effects
        //using the deprecated soundpool - target version <21
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        //create objects of 2 required classes
        AssetManager assetManager = context.getAssets();
        AssetFileDescriptor descriptor;

        try{
            //create the effects in memory ready to use
            descriptor = assetManager.openFd("start.ogg");
            start = soundPool.load(descriptor,0);

            //create the effects in memory ready to use
            descriptor = assetManager.openFd("win.ogg");
            win = soundPool.load(descriptor,0);

            //create the effects in memory ready to use
            descriptor = assetManager.openFd("bump.ogg");
            bump = soundPool.load(descriptor,0);

            //create the effects in memory ready to use
            descriptor = assetManager.openFd("destroyed.ogg");
            destroyed = soundPool.load(descriptor,0);
        }catch (IOException e){
            //print an error message to the console
            Log.e("error","failed to load sound files");
        }

        //load high scores
        //get references to file hiscores,
        //it it doesnt' exist - its created

        prefs = context.getSharedPreferences("hiscores", context.MODE_PRIVATE);

        //initialize the reditor
        editor = prefs.edit();

        //load fastest time from an entry from the file "fastestTime"
        //if not available highscore = 1000000;
        fastestTime = prefs.getLong("fastestTime",1000000);



        //prepare for drawing - initialize drawing objects
        holder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        startGame();

        //initialize playership
//        player = new PlayerShip(context,x,y);

        //initialize enemies
//        enemy1 = new EnemyShip(context,x,y);
//        enemy2 = new EnemyShip(context,x,y);
//        enemy3 = new EnemyShip(context,x,y);

        //initialize the star dust objects
//        int numSpecs = 40;
//        for (int i = 0; i < numSpecs;i++ ){
//            SpaceDust spec = new SpaceDust(x,y);
//            dustList.add(spec);
//        }
    }

    @Override
    public void run() {
        while(isPlaying){
            update();
            draw();
            control();
        }
    }

    public void startGame(){
        //initialize game objects
        isGameEnded = false;
        player = new PlayerShip(context,screenX,screenY);
        enemy1 = new EnemyShip(context,screenX,screenY);
        enemy2 = new EnemyShip(context,screenX,screenY);
        enemy3 = new EnemyShip(context,screenX,screenY);

        //for higher res
        if(screenX > 1000){
            enemy4 = new EnemyShip(context,screenX,screenY);
        }
        if(screenX > 1200){
            enemy5 = new EnemyShip(context,screenX,screenY);
        }


        int numSpecs = 400;
        for (int i = 0; i < numSpecs;i++ ){
            SpaceDust spec = new SpaceDust(screenX,screenY);
            dustList.add(spec);
        }

        //reset time and distance
        distanceRemaining = 10000; // 10km
        timeTaken = 0;

        //get start time
        timeStarted = System.currentTimeMillis();

        //play sound for start
        soundPool.play(start,1,1,0,0,1);
    }

    public void update(){

        //collision detection on new positions before move because we are testing last frames
        //position which has just been drawn

        boolean hasHitDetected = false;

        //if images excess 100 pixles wide then the -100 value must be increased accordingly
        if(Rect.intersects(player.getHitbox(),enemy1.getHitbox())){
            hasHitDetected = true;
            enemy1.setX(-100);
        }
        if(Rect.intersects(player.getHitbox(),enemy2.getHitbox())){
            hasHitDetected = true;
            enemy2.setX(-100);
        }
        if(Rect.intersects(player.getHitbox(),enemy3.getHitbox())){
            hasHitDetected = true;
            enemy3.setX(-100);
        }

        //check the additional enemies
        if(screenX > 1000){
            if(Rect.intersects(player.getHitbox(),enemy4.getHitbox())){
                hasHitDetected = true;
                enemy4.setX(-100);
            }
        }

        if(screenX > 1200){
            if(Rect.intersects(player.getHitbox(),enemy5.getHitbox())){
                hasHitDetected = true;
                enemy5.setX(-100);
            }
        }

        //the game is completed - the player has lost - no more shields, he is hit
        //game over
        if(hasHitDetected){
            soundPool.play(bump,1,1,0,0,1);
            player.reduceShieldStrength();
            if(player.getShieldStrength()<0){
                soundPool.play(destroyed,1,1,0,0,1);
                isGameEnded = true;
            }
        }

        //update the player
        player.update();

        //update the enemies
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());

        //for higher res

        if(screenX > 1000){
            enemy4.update(player.getSpeed());
        }
        if(screenX > 1200){
            enemy5.update(player.getSpeed());
        }

        //update the stardust objects
        for (SpaceDust sd : dustList){
            sd.update(player.getSpeed());
        }

        //if game is still not completed
        if(!isGameEnded){
            //calculate game statistics details
            //substract distance to home planet based on current speed
            distanceRemaining -= player.getSpeed();

            //how long has the player been flying
            timeTaken = System.currentTimeMillis() - timeStarted;
        }

        //WIN THE GAME
        //the game is completed - the player has won - he has arrived at home
        if(distanceRemaining < 0){
            soundPool.play(win,1,1,0,0,1);
            //check for fastest time
            if(timeTaken < fastestTime){
                //save high score
                editor.putLong("fastestTime",timeTaken);
                editor.commit();
                fastestTime = timeTaken;
            }
            isGameEnded = true;
            distanceRemaining = 0; //to not show negative distance
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

            //for higher res
            if(screenX > 1000){
                canvas.drawBitmap(enemy4.getBitmap(),enemy4.getX(),enemy4.getY(),paint);
            }
            if(screenX > 1200){
                canvas.drawBitmap(enemy5.getBitmap(),enemy5.getX(),enemy5.getY(),paint);
            }

            //drawing the HUD
            //game statistics details according to this is game playing or just has ended
            //still playing
            if(!isGameEnded){
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255,255,255,255));
                paint.setTextSize(15);
                canvas.drawText("Fastest:"+formatTime(fastestTime) +"s",10,20,paint);
                canvas.drawText("Time:"+formatTime(timeTaken) +"s",screenX/2,20,paint);
                canvas.drawText("Distance:"+distanceRemaining/1000 +"km",screenX/3,screenY - 20,paint);
                canvas.drawText("Schield:"+player.getShieldStrength() +"s",10,screenY-20,paint);
                canvas.drawText("Speed:"+player.getSpeed()*60 +"mps",(screenX/3)*2,screenY-20,paint);
            }else{
                //it happens when game over - draw a big game over text and show game stats
                //the thread continues but the HUD stop updating
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over",screenX/2,160,paint);

                paint.setTextSize(25);
                canvas.drawText("Fastest:"+formatTime(fastestTime) +"s",screenX/2,200,paint);
                canvas.drawText("Time:"+formatTime(timeTaken) +"s",screenX/2,240,paint);
                canvas.drawText("Distance:"+distanceRemaining/1000 +"km",screenX/2,280,paint);

                paint.setTextSize(80);
                canvas.drawText("Tap to replay",screenX/2,350,paint);
            }



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
                //if the user is on pause screen, he starts a new game
                if(isGameEnded){
                    startGame();
                }
                break;
        }
        return true;

    }

    //helper methods

    //use timeTaken in milliseconds and reorganize it into seconds and fractions of seconds
    private String formatTime(long time){
        long seconds = (time)/1000;
        long thousandths = (time)-(seconds * 1000);
        String strThousandths = ""+thousandths;

        if(thousandths < 100){
            strThousandths = "0"+thousandths;
        }
        if(thousandths < 10){
            strThousandths = "0"+strThousandths;
        }
        String stringTime = ""+seconds+"."+strThousandths;
        return stringTime;
    }


}
