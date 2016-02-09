package com.gamecodeschool.c1tappydefender;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //prepare to load the fastest time from file
        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("hiscores",MODE_PRIVATE);

        //get references to the TextView for the high scores
        final TextView textFastestTime = (TextView) findViewById(R.id.textHighScore);

        //get fastest time, if not available high score = 1000000
        long fastestTime = prefs.getLong("fastestTime",1000000);
        textFastestTime.setText("Fastest Time :" + fastestTime);

        //get ref to the button
        final Button buttonPlay = (Button)findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this,GameActivity.class);
        startActivity(i);

        //now shut down the activity
        finish();
    }

    //it the player hits back button - quit the app
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return false;
    }
}
