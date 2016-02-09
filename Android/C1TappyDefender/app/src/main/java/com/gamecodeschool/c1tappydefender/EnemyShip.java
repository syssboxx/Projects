package com.gamecodeschool.c1tappydefender;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Stacy on 9.2.2016 г..
 */
public class EnemyShip {

    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed =1;

    //detect leaving the screen
    private int maxX;
    private int minX;

    //spawn enemies within screen bounds
    private int maxY;
    private int minY;

    //hitbox for collision detection
    private Rect hitbox;

    public EnemyShip(Context context, int screenX, int screenY){
        //create a random enemy object from the 3 images
        Random generator = new Random();
        int enemyIndex = generator.nextInt(3);
        switch(enemyIndex){
            case 0:
                bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy2);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy3);
                break;
        }

        scaleBitmap(screenX);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        //random enemy speed = between 10 and 15 - min and max enemy speed
        speed = generator.nextInt(6)+10;

        //spawn off screen and emerges from the right side
        //random height
        x=screenX;
        y=generator.nextInt(maxY) - bitmap.getHeight();

        //initialize the hitbox
        hitbox = new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());
    }


    //the enemies moves with the players speed, if the player boosts - they are faster too
    public void update(int playerSpeed){
        //move to left
        x-=playerSpeed;
        x-=speed;

        //respawn when left the screen, they move only horizontally by x
        if(x < minX - bitmap.getWidth()){
            Random generator = new Random();
            speed = generator.nextInt(10)+10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }

        //update the hitbox location with the last coordinates ot the player
        hitbox.left = x;
        hitbox.top = y;
        hitbox.right = x + bitmap.getWidth();
        hitbox.bottom = x + bitmap.getHeight();
    }

    public void scaleBitmap(int x){
        //use the x resolution to decide how much to scale the bitmap
        //to reduce the game objects depending the screen size
        //for low res display - scale down the image
        if(x < 1000){
            //3x smaller
            bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/3,bitmap.getHeight()/3,false);
        }else if (x > 1200){
            //2x smaller
            bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,false);
        }
        //for higher res the size won't be scaled but more enemies wil be added

    }


    //getters and setters

    public Bitmap getBitmap(){
        return bitmap;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Rect getHitbox(){
        return hitbox;
    }

    //used from TDView update() to make a new enemy out of the screen
    public void setX(int x){
        this.x = x;
    }


}
