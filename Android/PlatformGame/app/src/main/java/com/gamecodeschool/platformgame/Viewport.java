package com.gamecodeschool.platformgame;

import android.graphics.Rect;
import android.view.View;

/**
 * Created by Stacy on 9.2.2016 г..
 */
//it's similar to movie camera that follows the action of the game
//it defines the area of the game world that is shown to the player, it's centered to the player figure
//it's used also to determine which game obejcts are inside and outside of the game scene
//it translates game world coordinates into pixel coordinates for drawing on screen
//calculates pixelPerMeter

public class Viewport {

    private Vector2DPoint5D currentViewportWorldCenter;
    private Rect convertedRect;
    private int pixelsPerMeterX;
    private int pixelsPerMeterY;
    private int screenResolutionX;
    private int screenResolutionY;
    private int screenCenterX;
    private int screenCenterY;
    private int metersToShowX;
    private int metersToShowY;

    //for debugging, to calculate the number of clipped objects
    private int numClipped;

    Viewport(int x, int y){
        screenResolutionX = x;
        screenResolutionY = y;

        screenCenterX = screenResolutionX/2;
        screenCenterY = screenResolutionY/2;

        //device with res 840x400pxl will have pixels per meter x/y of 32/22
        pixelsPerMeterX = screenResolutionX / 32;
        pixelsPerMeterY = screenResolutionY / 18;

        //a little more pixels will be shown to avoid gaps between the edges of the screen
        metersToShowX = 34;
        metersToShowY = 20;

        convertedRect = new Rect();
        currentViewportWorldCenter = new Vector2DPoint5D();
    }

    //getters and setters
    public void setWorldCenter(float x, float y){
        currentViewportWorldCenter.x = x;
        currentViewportWorldCenter.y = y;
    }

    public int getScreenWidth(){
        return screenResolutionX;
    }

    public int getScreenHeight(){
        return screenResolutionY;
    }

    public int getPixelsPerMeterX(){
        return pixelsPerMeterX;
    }

    //methods

    //converts the locations of the objects in the visible viewport from world cooridnates
    //to pixel screen coordinates to be drawn
    //it takes as input x,y coordinates, width and height of the game object
    //and calculates left, top,right and bottom values of the a rectangle to be returned as result
    public Rect worldToScreen(float objectX, float objectY, float objectHeight, float objectWidth){

        int left = (int)(screenCenterX - (currentViewportWorldCenter.x - objectX)* pixelsPerMeterX);
        int top = (int)(screenCenterY - ((currentViewportWorldCenter.y - objectY)*pixelsPerMeterY));
        int right = (int)(left + (objectWidth*pixelsPerMeterX));
        int bottom = (int)(top + (objectHeight * pixelsPerMeterY));

        convertedRect.set(left,top,right,bottom);
        return convertedRect;
    }

    //remove objects that are not inside the viewport rectangle
    //true is assigned if the object is outside and it is clipped
    public boolean clipObjects(float objectX, float objectY, float objectHeight, float objectWidth){
        boolean isClipped = true;

        if(objectX - objectWidth < currentViewportWorldCenter.x + (metersToShowX/2)){
            if(objectX + objectWidth > currentViewportWorldCenter.x - (metersToShowX/2)){
                if(objectY - objectHeight < currentViewportWorldCenter.y + (metersToShowY/2)){
                    if(objectY + objectHeight > currentViewportWorldCenter.y - (metersToShowY/2)){
                        //the object is inside
                        isClipped = false;
                    }
                }
            }
        }
        //for debugging
        if(isClipped){
            numClipped++;
        }

        return isClipped;
    }

    public int getNumClipped(){
        return numClipped;
    }

    //the number ot clipped objects will be reset to 0 at each frame
    public void resetNumClipped(){
        numClipped = 0;
    }


}
