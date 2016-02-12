package com.gamecodeschool.platformgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Stacy on 9.2.2016 г..
 */
public abstract class GameObject {

    private Vector2DPoint5D worldLocation;
    private float width;
    private float height;

    private boolean isActive;
    private boolean isVisible;
    private int animFrameCount=1;
    private char type;

    private String bitmapName;

    public abstract void update(long fps, float gravity);

    public String getBitmapName(){
        return bitmapName;
    }

    public Bitmap prepareBitmap(Context context,String bitmapName,int pixelsPerMeter){
        //make an resource id from the bitmap name
        int resId = context.getResources().getIdentifier(bitmapName,"drawable",context.getPackageName());

        //create the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);

        //scale the bitmap based on the number of pixels per meter on the device,
        //multiplied by the number of frames in the image
        //par def 1 frame
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)(width * pixelsPerMeter * animFrameCount),
                                                  (int)(height*pixelsPerMeter),false);

        return bitmap;
    }

    //getters and setters
    public Vector2DPoint5D getWorldLocation(){
        return worldLocation;
    }

    public void setWorldLocation(float x, float y, int z){
        this.worldLocation = new Vector2DPoint5D();
        this.worldLocation.x = x;
        this.worldLocation.y = y;
        this.worldLocation.z = z;
    }

    public void setBitmapName(String bitmapName){
        this.bitmapName = bitmapName;
    }

    public float getWidth(){
        return width;
    }

    public void setWidth(float width){
        this.width = width;
    }

    public float getHeight(){
        return height;
    }

    public void setHeight(float height){
        this.height = height;
    }

    public boolean isActive(){
        return isActive;
    }

    public boolean isVisible(){
        return isVisible;
    }

    public void setVisible(boolean isVisible){
       this.isVisible = isVisible;
    }

    public char getType(){
        return type;
    }

    public void setType(char type){
        this.type = type;
    }

}
