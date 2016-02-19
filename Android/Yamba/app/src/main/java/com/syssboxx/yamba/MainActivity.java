package com.syssboxx.yamba;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //called when the user click on a menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       int id = item.getItemId();
        Intent i;

       switch(id){
            case R.id.action_settings :
                i = new Intent(this,SettingsActivity.class);
                startActivity(i);
                return true;

//           case R.id.action_tweet :
//               i = new Intent(this,StatusActivity.class);
//               startActivity(i);
//               return true;

           case R.id.action_tweet:
               i = new Intent("com.syssboxx.yamba.action.tweet");
               startActivity(i);
               return true;
           default:
               return false;
        }



    }
}
