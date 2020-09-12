package com.example.markhunters.signin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.markhunters.R;

public class UserCreationActivity extends AppCompatActivity {

    public static final String FIREBASE_USER = "firebase_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);
    }
}