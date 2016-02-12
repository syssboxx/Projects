package com.gamecodeschool.platformgame;

import android.content.Context;

/**
 * Created by Stacy on 9.2.2016 г..
 */
public class Player extends GameObject {

    Player(Context context,float worldStartX, float worldStartY,int pixelPerMeter){
        final float HEIGHT = 2;
        final float WIDTH = 1;

        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType('p');

        //set the bitmap - it's a sprite sheet with multiple frames of animation
        setBitmapName("player");
    }


    public void update(long fps, float gravity) {}
}
