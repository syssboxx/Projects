package com.gamecodeschool.c1tappydefender;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;


public class GameActivity extends Activity {

    //an object that handle the view
    private TDView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get displey obejct to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        //load resolution x,y to a point object
        Point size = new Point();
        display.getSize(size);


        //create an instance of the gameView - TDView
        //the view is dynamically created and drawn
        gameView = new TDView(this,size.x, size.y);
        setContentView(gameView);
    }

    //if the activity is paused - then pause the game thread too
    @Override
    protected void onPause(){
        super.onPause();
        gameView.pause();
    }

    //if the activity is resumed, then resume and restart the game thread too
    @Override
    protected void onResume(){
        super.onResume();
        gameView.resume();
    }



}
