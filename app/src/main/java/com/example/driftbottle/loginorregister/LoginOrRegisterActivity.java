package com.example.driftbottle.loginorregister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.driftbottle.databinding.ActivityLoginOrRegisterBinding;
import com.example.driftbottle.ui.custom.DoubleSelectBar;

public class LoginOrRegisterActivity extends AppCompatActivity {

    private ActivityLoginOrRegisterBinding binding;
    private static final int LOGIN_STATE = 0;
    private static final int REGISTER_STATE = 1;
    private int currentState = 0;
    private static final String TAG = "LoginOrRegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginOrRegisterBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

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
                if (currentState == LOGIN_STATE){
                    //TODO

                }else if (currentState == REGISTER_STATE){
                    String password = binding.editTextPassword.getText().toString();
                    String confirmPassword = binding.editTextConfirmPassword.getText().toString();
                    if (!password.equals(confirmPassword)){
                        Toast.makeText(LoginOrRegisterActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                    }else if (password.length() <6 || password.length()>15){
                        Toast.makeText(LoginOrRegisterActivity.this,"密码长度为8~15",Toast.LENGTH_SHORT).show();
                    }else{
                        //TODO

                    }


                }
            }
        });



    }
}