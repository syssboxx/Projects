package com.gamecodeschool.c1tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by Stacy on 5.2.2016 г..
 */

//model
public class PlayerShip {

    //ship's screen coordinates
    private int x;
    private int y;
    private int speed = 0;
    private Bitmap bitmap;
    private boolean isBoosting;
    private int shieldStrength;

    //stop ship leaving the screen
    private int maxY;
    private int minY;

    //hitbox for collision detection
    private Rect hitbox;

    //player constants
    private final int GRAVITY = -12;

    //limit the bounds of the ship's speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    //set initial position and speed
    public PlayerShip(Context context, int screenX, int screenY){
        x = 50;
        y = 50;
        speed = 1;
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ship);
        isBoosting = false;
        minY = 0;
        maxY = screenY - bitmap.getHeight();
        shieldStrength = 2;

        //initialize the hitbox
        hitbox = new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());

    }

    //increment the ship's value x
    public void update(){

        if(isBoosting){
            //speed up
            speed +=2;
        }else{
            //slow down
            speed -=5;
        }
        //constraint speed
        if(speed > MAX_SPEED){
            speed = MAX_SPEED;
        }

        //move the ship up or down - simulate gravity
        y-=speed + GRAVITY;

        //constraint the ship don't go off screen
        if(y < minY){
            y = minY;
        }
        if(y > maxY){
            y = maxY;
        }

        //x++;

        //update the hitbox location with the last coordinates ot the player
        hitbox.left = x;
        hitbox.top = y;
        hitbox.right = x + bitmap.getWidth();
        hitbox.bottom = x + bitmap.getHeight();
    }

    //getters and setters
    //share its state with the game view - representation, speed and coordinates
    public Bitmap getBitmap(){
        return this.bitmap;
    }

    public int getSpeed(){
        return this.speed;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setBoosting(){
        this.isBoosting = true;
    }

    public  void stopBoosting(){
        this.isBoosting = false;
    }

    public int getShieldStrength(){
        return shieldStrength;
    }

    public Rect getHitbox(){
        return hitbox;
    }

}
