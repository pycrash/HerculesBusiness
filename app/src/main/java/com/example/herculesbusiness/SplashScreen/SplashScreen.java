package com.example.herculesbusiness.SplashScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import com.example.herculesbusiness.Home.HomeActivity;
import com.example.herculesbusiness.Login.LoginActivity;
import com.example.herculesbusiness.R;
import com.orhanobut.hawk.Hawk;

public class SplashScreen extends AppCompatActivity {
    String TAG = "SplashScreen";
    private static final int SPLASH_DELAY = 900;
    boolean userLoggedIn = false;
    private final Handler mHandler = new Handler();
    private final Launcher mLauncher = new Launcher();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Hawk.init(SplashScreen.this).build();
        userLoggedIn = (Hawk.contains("email"));
        SharedPreferences appSettingPrefs = getSharedPreferences("AppSetting", 0);
        boolean isNightModeOn = appSettingPrefs.getBoolean("NightMode", false);

        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }
    @Override
    protected void onStop() {
        mHandler.removeCallbacks(mLauncher);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHandler.postDelayed(mLauncher, SPLASH_DELAY);
    }
    private void launch() {
        if (!isFinishing()) {
            if (userLoggedIn) {
                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                String email = Hawk.get("email");
                Log.d(TAG, "onCreate: SplashScreen to login activity intent started");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("email", email);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();


            } else {

                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                Log.d(TAG, "onCreate: SplashScreen to login activity intent started");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();

            }
            finish();
        }
    }
    private class Launcher implements Runnable {
        @Override
        public void run() {
            launch();
        }
    }

}