package com.syssboxx.yamba;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

/**
 * Created by Stacy on 19.2.2016 г..
 */
public class StatusFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "StatusFragment";

    private Button buttonTweet;
    private EditText editStatus;
    private TextView textCount;

    private int defaultTextColor;

    SharedPreferences prefs;

    final int MAX_COUNT = 140;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_status,container,false);

        editStatus = (EditText) view.findViewById(R.id.editStatus);
        textCount = (TextView) view.findViewById(R.id.textCount);
        buttonTweet = (Button) view.findViewById(R.id.buttonTweet);

        buttonTweet.setOnClickListener(this);

        //get deafult color of the text according to the choosen them
        defaultTextColor = textCount.getTextColors().getDefaultColor();

        editStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = MAX_COUNT - editStatus.length();
                textCount.setText(Integer.toString(count));
                textCount.setTextColor(defaultTextColor);

                if(count < 10){
                    textCount.setTextColor(Color.RED);
                    if(count < 0){
                        count = 0;
                        textCount.setText(Integer.toString(count));
                    }
                }

            }
        });

        return view;

    }


    @Override
    public void onClick(View v) {
        String status = editStatus.getText().toString();
        Log.d(TAG, status);
        PostTask postTask = new PostTask();
        postTask.execute(status);
    }


    private final class PostTask extends AsyncTask<String,Void,String> {

        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(getActivity(), "Posting","Please wait...");
            progress.setCancelable(true);
        }

        //executes on the non-UI thread
        @Override
        protected String doInBackground(String... params) {
            String result="";

            try{
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String username = prefs.getString("username","");
                String password = prefs.getString("password","");

                //check if password or username are not empty
                //if empty set a toast to set login info and go back to Settings Activity
                if(username.isEmpty() || password.isEmpty()){
                    Intent i = new Intent(getActivity(),SettingsActivity.class);
                    startActivity(i);
                    result = "Please update your username and password";
                }
                //if it's ok, start the network task
                YambaClient yambaCloud = new YambaClient(username,password);
                yambaCloud.postStatus(params[0]);
                result = "Successfully posted";
                Log.e(TAG,"Successfully posted"+params[0]);
            }catch (Exception e){
                Log.e(TAG,"Failed to post");
                e.printStackTrace();
                result = "Failed to post to yamba service";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            progress.dismiss();
            if(getActivity()!= null && result != null){
                Toast.makeText(StatusFragment.this.getActivity(), result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
