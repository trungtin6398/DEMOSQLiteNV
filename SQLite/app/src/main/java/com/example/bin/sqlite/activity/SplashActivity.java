package com.example.bin.sqlite.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.bin.sqlite.service.MyAsyncTask;
import com.example.bin.sqlite.R;
import com.example.bin.sqlite.database.SQLite;

public class SplashActivity extends AppCompatActivity {

    MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SQLite createDatabase = new SQLite(this);
        createDatabase.open();

        myAsyncTask = new MyAsyncTask(SplashActivity.this);
        myAsyncTask.execute();

//        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                finish();
//            }
//        }, 2500);
    }
}
