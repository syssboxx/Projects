package com.syssboxx.yamba;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
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


public class StatusActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "StatusActivity";

    private Button buttonTweet;
    private EditText editStatus;
    private TextView textCount;

    private int defaultTextColor;

    final int MAX_COUNT = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        editStatus = (EditText) findViewById(R.id.editStatus);
        textCount = (TextView) findViewById(R.id.textCount);
        buttonTweet = (Button) findViewById(R.id.buttonTweet);

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

    }

    @Override
    public void onClick(View v) {
        String status = editStatus.getText().toString();
        Log.d(TAG, status);
        PostTask postTask = new PostTask();
        postTask.execute(status);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_status, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final class PostTask extends AsyncTask<String,Void,String>{

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... params) {
            String result;
            YambaClient yambaCloud = new YambaClient("student","password");
            try {
                yambaCloud.postStatus(params[0]);
                result = "Successfully posted";
            } catch (YambaClientException e) {
                e.printStackTrace();
                result = "Failed to post to yamba service";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(StatusActivity.this,result,Toast.LENGTH_LONG).show();
        }
    }





}


