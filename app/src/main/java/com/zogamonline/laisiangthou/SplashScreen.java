package com.zogamonline.laisiangthou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zogamonline.laisiangthou.activities.lst.Laisiangthou;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen); // not needed - at the moment it is completely empty

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, Laisiangthou.class));
                finish();
            }
        }, 400);
    }
}