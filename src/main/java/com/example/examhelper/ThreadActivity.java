package com.example.examhelper;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class ThreadActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mInfoTextView;
    private Button mStartButton;
    private Button mRestartButton;
    private ProgressBar mHorizontalProgressBar;
    private ProgressBar mProgressBar;

    private boolean done = false;

    private TryingDBHelper tryingDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        mInfoTextView = findViewById(R.id.textViewInfo);

        mStartButton = findViewById(R.id.buttonStart);
        mRestartButton = findViewById(R.id.buttonRestart);
        mStartButton.setOnClickListener(this);
        mRestartButton.setOnClickListener(this);


        mHorizontalProgressBar = findViewById(R.id.progressBar2);

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        done = sharedPreferences.getBoolean("done_climbing", false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonStart:
                if (!done) {
                    CatTask catTask = new CatTask();
                    catTask.execute("cat1.jpg", "cat2.jgp", "cat3.jpg", "cat4.jpg");
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    mInfoTextView.setText("Уже лазил, больше не хочу");
                }
                break;
            case R.id.buttonRestart:
                done = false;
                mInfoTextView.setText("Ладно, уговорил");
        }
    }

    private class CatTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mInfoTextView.setText("Полез на крышу");
            mStartButton.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Integer doInBackground(String... urls) {
            tryingDBHelper = new TryingDBHelper(ThreadActivity.this);

            try {
                for (int i = 1; i < 6; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    publishProgress(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 5;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mInfoTextView.setText("Залез" + " Возвращаем результат: " + result);
            mStartButton.setVisibility(View.VISIBLE);
            mHorizontalProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.INVISIBLE);

            done = true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mInfoTextView.setText("Этаж: " + values[0]);
            mHorizontalProgressBar.setProgress(values[0] * 20);
        }

        private void getFloor() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
