package com.syssboxx.yamba;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Stacy on 19.2.2016 г..
 * SettingsActivity use SettingsFragment
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //the first time the activity is created
        if(savedInstanceState == null){
            SettingsFragment settingsFragment = new SettingsFragment();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content,settingsFragment,settingsFragment.getClass().getSimpleName())
                    .commit();
        }
    }
}
