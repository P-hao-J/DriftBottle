package com.example.driftbottle.loginorregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.driftbottle.databinding.ActivityLoginOrRegisterBinding;
import com.example.driftbottle.main.MainActivity;
import com.example.driftbottle.net.BottleService;
import com.example.driftbottle.net.JWebSocketClient;
import com.example.driftbottle.net.LoginMessage;
import com.example.driftbottle.net.MyService;
import com.example.driftbottle.net.RegisterMessage;
import com.example.driftbottle.ui.custom.DoubleSelectBar;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginOrRegisterActivity extends AppCompatActivity {

    private ActivityLoginOrRegisterBinding binding;
    private static final int LOGIN_STATE = 0;
    private static final int REGISTER_STATE = 1;
    private int currentState = 0;
    private static final String TAG = "LoginOrRegisterActivity";
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginOrRegisterBinding.inflate(getLayoutInflater());
        rootView = binding.getRoot();
        setContentView(rootView);

        binding.selectBar.setMyClickListener(new DoubleSelectBar.LeftAndRightListener(){

            @Override
            public void onClickLeft() {
                currentState = LOGIN_STATE;
                binding.btnLoginOrRegister.setText("登录");
                binding.editTextConfirmPassword.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onClickRight() {
                currentState = REGISTER_STATE;
                binding.btnLoginOrRegister.setText("注册");
                binding.editTextConfirmPassword.setVisibility(View.VISIBLE);
            }
        });


        binding.btnLoginOrRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = binding.editTextName.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if (userName.equals("") || password.equals("")){
                    Toast.makeText(LoginOrRegisterActivity.this,"用户名，密码不为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() <6 || password.length()>15){
                    Toast.makeText(LoginOrRegisterActivity.this,"密码长度为8~15",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentState == LOGIN_STATE){

                    ProgressBar progressBar = new ProgressBar(LoginOrRegisterActivity.this);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(200,200);
                    lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    RelativeLayout relativeLayout = (RelativeLayout) rootView;
                    relativeLayout.addView(progressBar,lp);

                    WindowManager.LayoutParams params =  getWindow().getAttributes();
                    params.alpha = 0.3f;
                    getWindow().setAttributes(params);

                    binding.selectBar.setClickable(false);
                    binding.btnLoginOrRegister.setClickable(false);


                    BottleService service = MyService.getInstance();
                    JsonObject object = new JsonObject();
                    object.addProperty("user_name",userName);
                    object.addProperty("password",password);
                    Call<LoginMessage> call = service.login(object);
                    call.enqueue(new Callback<LoginMessage>() {
                        @Override
                        public void onResponse(Call<LoginMessage> call, Response<LoginMessage> response) {
                            binding.selectBar.setClickable(true);
                            binding.btnLoginOrRegister.setClickable(true);
                            WindowManager.LayoutParams params =  getWindow().getAttributes();
                            params.alpha = 1f;
                            getWindow().setAttributes(params);
                            relativeLayout.removeView(progressBar);
                            int code = response.code();
                            if (code==200){
                                String base64 = response.body().getImg_base64();
                                String token = response.body().getToken();
                                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                Log.d(TAG, "=== userName--> "+userName);
                                editor.putString("userName",userName);
                                editor.putString("base64",base64);
                                editor.putString("token",token);
                                editor.putBoolean("isLogout",false);
                                Log.d(TAG, "=== base64--> "+base64);
                                editor.apply();
                                Intent intent = new Intent(LoginOrRegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else if (code==500){
                                try {
                                    String errString = response.errorBody().string();
                                    JSONObject jsonObject = new JSONObject(errString);
                                    String msg = jsonObject.getString("msg");
                                    if (msg.equals("password error")){
                                        Toast.makeText(LoginOrRegisterActivity.this,"密码错误",Toast.LENGTH_LONG).show();
                                    }else if (msg.equals("record not found")){
                                        Toast.makeText(LoginOrRegisterActivity.this,"用户名不存在",Toast.LENGTH_LONG).show();
                                    }
                                } catch (IOException | JSONException e) {
                                    Toast.makeText(LoginOrRegisterActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(LoginOrRegisterActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginMessage> call, Throwable t) {
                            Toast.makeText(LoginOrRegisterActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                        }
                    });

                }else if (currentState == REGISTER_STATE){
                    String confirmPassword = binding.editTextConfirmPassword.getText().toString();
                    if (!password.equals(confirmPassword)){
                        Toast.makeText(LoginOrRegisterActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                    }else{
                            JsonObject object = new JsonObject();
                            object.addProperty("user_name",userName);
                            object.addProperty("password",password);

                            BottleService bottleService = MyService.getInstance();
                            Call<RegisterMessage> call =  bottleService.register(object);
                            call.enqueue(new Callback<RegisterMessage>() {
                                    @Override
                                    public void onResponse(Call<RegisterMessage> call, Response<RegisterMessage> response) {
                                        int code = response.code();
                                        if (code==200){
                                            Toast.makeText(LoginOrRegisterActivity.this,"注册成功,请登录",Toast.LENGTH_LONG).show();
                                        }else if (code==500){
                                            Toast.makeText(LoginOrRegisterActivity.this,"用户名已存在",Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(LoginOrRegisterActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<RegisterMessage> call, Throwable t) {
                                        Toast.makeText(LoginOrRegisterActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                                    }
                            });

                    }
                }
            }
        });


//        JWebSocketClient.getInstance().send("{\"receiver\":16,\"message\":\"777\"}");

    }

    


}