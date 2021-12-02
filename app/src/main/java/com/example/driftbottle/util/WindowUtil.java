package com.example.driftbottle.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;

public class WindowUtil {

    Activity mActivity = null;
    private DisplayMetrics displayMetrics;
    private static final String TAG = "WindowUtil";

    public WindowUtil(Activity activity){
        mActivity = activity;
        displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    public float getPhoneWidth(){
        if (displayMetrics==null) return 0;
        return displayMetrics.xdpi;
    }

    public float getPhoneHeight(){
        if (displayMetrics==null) return 0;
        return displayMetrics.ydpi;
    }

    public float dp2px(int dp){
        Log.d(TAG, "=== dp2px: "+dp);
        return dp/displayMetrics.density;
    }


}
