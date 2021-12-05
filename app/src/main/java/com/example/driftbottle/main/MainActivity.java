package com.example.driftbottle.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.driftbottle.R;
import com.example.driftbottle.adapter.MyFragmentStateAdapter;
import com.example.driftbottle.databinding.ActivityMainBinding;
import com.example.driftbottle.ui.meet.MeetFragment;
import com.example.driftbottle.ui.message.MessageFragment;
import com.example.driftbottle.ui.mine.MineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    private List<Class> fragments = null;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        fragments = new ArrayList<>();
        fragments.add(MeetFragment.class);
        fragments.add(MessageFragment.class);
        fragments.add(MineFragment.class);
        MyFragmentStateAdapter adapter = new MyFragmentStateAdapter(MainActivity.this);
        adapter.setList(fragments);
        binding.viewPager.setAdapter(adapter);



        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.bottomNavigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_heart:
                        binding.bottomNavigation.getMenu().getItem(0).setChecked(true);             //必须
                        binding.viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_msg:
                        binding.bottomNavigation.getMenu().getItem(1).setChecked(true);
                        binding.viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_mine:
                        binding.bottomNavigation.getMenu().getItem(2).setChecked(true);
                        binding.viewPager.setCurrentItem(2);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


    }

    public void refuseClick(){
        dialog = new Dialog(MainActivity.this,R.style.TransparentWindowBg);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void recoverClickable(){
        if (dialog == null) return;
        dialog.setCancelable(true);
        dialog.dismiss();

    }

}