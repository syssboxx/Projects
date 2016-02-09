package com.gamecodeschool.c1tappydefender;

import java.util.Random;

/**
 * Created by Stacy on 9.2.2016 г..
 */
public class SpaceDust {

    private int x,y;
    private int speed;

    //detect dust leaving the screen
    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    public SpaceDust(int screenX, int screenY){
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        //set a random speed between 0 and 9
        Random generator = new Random();
        speed = generator.nextInt(10);

        //set starting coordinates
        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);
    }

    public void update(int playerSpeed){
        //speed up when the playes speeds up
        x-=playerSpeed;
        x-=speed;

        //respawn
        if(x < 0){
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
            speed = generator.nextInt(15);
        }
    }

    //getters and setters
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

}
