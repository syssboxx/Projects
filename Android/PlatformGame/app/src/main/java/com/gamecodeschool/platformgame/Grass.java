package com.gamecodeschool.platformgame;

/**
 * Created by Stacy on 12.2.2016 г..
 */
public class Grass extends  GameObject {

   Grass(float worldStartX, float worldStartY,char type) {
        final float HEIGHT = 1;
        final float WIDTH = 1;

        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);

        //set the bitmap namefor the grass
        setBitmapName("turf");

        //where does the tile start - x and y locations from constructor parametters
        setWorldLocation(worldStartX,worldStartY,0);
    }

    public void update(long fps, float gravity) {}
}
