package com.easyege.examhelper;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipInputStream;

public class ThreadActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String DB_NAME = "default (3).db";
    private InputStream is;
    private OutputStream myOutput;
    private ZipInputStream zis;
    private TextView mInfoTextView;
    private Button mOKButton;
    private ProgressBar mHorizontalProgressBar;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        mInfoTextView = findViewById(R.id.textViewInfo);
        mOKButton = findViewById(R.id.buttonOK);
        mOKButton.setOnClickListener(this);
        mOKButton.setVisibility(View.INVISIBLE);
        mHorizontalProgressBar = findViewById(R.id.progressBar2);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean done = sharedPreferences.getBoolean("done_copying", false);
        if (!done) {
            CatTask catTask = new CatTask();
            catTask.execute();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonOK) {
            finish();
        }
    }

    private void massCopy() throws IOException {
        Log.d("myLogs", "merging databases...");
        is = this.getResources().openRawResource(R.raw.def);
        //is = this.getResources().openRawResource(R.raw.testdef);

        String DB_PATH = this.getDatabasePath(DB_NAME).getPath();
        myOutput = new FileOutputStream(DB_PATH);
        zis = new ZipInputStream(new BufferedInputStream(is));
    }

    @SuppressLint("StaticFieldLeak")
    private class CatTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mInfoTextView.setText("База данных распаковывается...");
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... urls) {
            int i = 0;
            Log.d("myLogs", "merging databases...");
            String DB_PATH = ThreadActivity.this.getDatabasePath(DB_NAME).getPath();
            Log.d("myLogs", "Путь к базе: " + DB_PATH);
            File dbFile = new File(DB_PATH.replace(DB_NAME, ""));
            Log.d("myLogs", "Результат создания директории: " + dbFile.mkdir());
            Log.d("myLogs", dbFile.toString());

            try {
                massCopy();
                try {
                    while (zis.getNextEntry() != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int count;

                        while ((count = zis.read(buffer)) != -1) {
                            baos.write(buffer, 0, count);
                            i++;
                            publishProgress(i);
                            //TimeUnit.MILLISECONDS.sleep(10);
                        }
                        baos.writeTo(myOutput);
                        int len = baos.toByteArray().length;
                        Log.d("myLogs", "Длина массива" + len);
                    }
                /*} catch (InterruptedException e) {
                    e.printStackTrace();*/
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    zis.close();
                    myOutput.flush();
                    myOutput.close();
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return i;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.d("myLogs", "onPostExecute");

            mInfoTextView.setText("");
            mInfoTextView.append(getString(R.string.success));
            mInfoTextView.append(" ");
            mInfoTextView.append(String.valueOf(result));
            mInfoTextView.append(getResources().getString(R.string.kb));

            mProgressBar.setVisibility(View.INVISIBLE);
            mOKButton.setVisibility(View.VISIBLE);

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ThreadActivity.this).edit();
            editor.putBoolean("done_copying", true);
            editor.apply();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mInfoTextView.setText("");
            mInfoTextView.append(getResources().getString(R.string.progress));
            mInfoTextView.append(" ");
            mInfoTextView.append(String.valueOf(values[0]));
            mInfoTextView.append(" ");
            mInfoTextView.append(getResources().getString(R.string.kb));
            mHorizontalProgressBar.setProgress((int) (values[0] / 1800.0 * 100));
        }
    }
}
