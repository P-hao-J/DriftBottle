package com.example.driftbottle.welcome;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.driftbottle.databinding.ActivityWelcomeBinding;
import com.example.driftbottle.loginorregister.LoginOrRegisterActivity;
import com.example.driftbottle.main.MainActivity;
import com.example.driftbottle.net.JWebSocketClient;
import com.example.driftbottle.net.MyService;
import com.example.driftbottle.net.WebSocketMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Permission;
import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";
    private ActivityWelcomeBinding binding;
    private final String[] permissions = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);
        requestPermission();

        EventBus.getDefault().register(this);

        // ** 屏幕
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Log.d(TAG, "=== "+displayMetrics.density);
        Log.d(TAG, "=== "+displayMetrics.densityDpi);



        tryToConnect();

    }

    private void tryToConnect(){
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        JWebSocketClient client = JWebSocketClient.getInstance();
        client.addHeader("Authorization",token);
        if (client.isOpen()){
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            try {
                client.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void waitJudgeLogin(WebSocketMessage message){
        Log.d(TAG, "=== waitJudgeLogin: ");
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        boolean isLogout = sharedPreferences.getBoolean("isLogout",false);
        if (message.getCode()==WebSocketMessage.OPEN && !isLogout){
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            Intent intent = new Intent(WelcomeActivity.this, LoginOrRegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void requestPermission() {
        // Here, thisActivity is the current activity
        for (int i=0;i<permissions.length;i++){
            if (ContextCompat.checkSelfPermission(WelcomeActivity.this,permissions[i])!=PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])){
                    ActivityCompat.requestPermissions(WelcomeActivity.this,permissions,0);
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}