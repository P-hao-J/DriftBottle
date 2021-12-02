package com.example.driftbottle.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.driftbottle.databinding.ActivityWelcomeBinding;
import com.example.driftbottle.loginorregister.LoginOrRegisterActivity;
import com.example.driftbottle.main.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWelcomeBinding binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Log.d(TAG, "=== "+displayMetrics.density);
        Log.d(TAG, "=== "+displayMetrics.densityDpi);


        if (isLogined()){
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(WelcomeActivity.this, LoginOrRegisterActivity.class);
            startActivity(intent);
        }


    }

    private boolean isLogined(){
        //TODO
        return false;
    }



}