package com.androidtask.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidtask.R;
import com.androidtask.model.User;
import com.google.gson.Gson;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000;
    Handler handler = new Handler();
    Runnable run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        run = new Runnable() {
            @Override
            public void run() {

                SharedPreferences prefs = getSharedPreferences(LogIn.MY_PREFS_NAME, MODE_PRIVATE);
                Gson gson = new Gson();
                String json = prefs.getString("userObject", "");

                if (!json.equals("")) {
                    User user = gson.fromJson(json, User.class);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("userData", user);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, LogIn.class);
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        };
        handler.postDelayed(run, SPLASH_DELAY);
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(run);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(run);
        super.onDestroy();
    }
}
