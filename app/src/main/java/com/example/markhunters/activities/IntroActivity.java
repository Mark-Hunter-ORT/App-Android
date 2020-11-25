package com.example.markhunters.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.markhunters.R;
import com.example.markhunters.activities.signin.SignInActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        (new Handler()).postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }, 2000);
    }
}