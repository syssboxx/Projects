package com.gamecodeschool.platformgame;


import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;


public class PlatformActivity extends Activity {

    //object to handle the view
    private PlatformView platformView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get display object to access device's screen resolution and load resolution
        Display display = getWindowManager().getDefaultDisplay();
        Point resolution = new Point();
        display.getSize(resolution);

        //set the game view
        platformView = new PlatformView(this,resolution.x,resolution.y);
        setContentView(platformView);
    }

    //pause the thread when the activity is paused
    @Override
    protected void onPause(){
        super.onPause();
        platformView.pause();
    }

    //restart the thread when the activity is resumed
    @Override
    protected void onResume(){
        super.onResume();
        platformView.resume();
    }
}
