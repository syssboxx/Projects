package com.syssboxx.yamba;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class StatusActivity extends Activity  {

    private static final String TAG = "StatusActivity";

        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //dynamically create the view
            // add the fragment

            //if it's the first time the activity is created
            if (savedInstanceState == null) {
                StatusFragment fragment = new StatusFragment();
                getFragmentManager().beginTransaction()
                        .add(android.R.id.content, fragment, fragment.getClass().getSimpleName())
                        .commit();
            }

        }

}


