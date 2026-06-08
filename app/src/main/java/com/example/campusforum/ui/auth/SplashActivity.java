package com.example.campusforum.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusforum.MainActivity;
import com.example.campusforum.R;
import com.example.campusforum.database.DatabaseHelper;
import com.example.campusforum.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 800L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DatabaseHelper.getInstance(this).getReadableDatabase();

        new Handler(Looper.getMainLooper()).postDelayed(this::openNextScreen, SPLASH_DELAY_MS);
    }

    private void openNextScreen() {
        SessionManager sessionManager = new SessionManager(this);
        Class<?> targetActivity = sessionManager.isLoggedIn() ? MainActivity.class : LoginActivity.class;
        Intent intent = new Intent(this, targetActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
