package com.example.driftbottle.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static Toast getShortToast(Context context,String text){
        return Toast.makeText(context,text,Toast.LENGTH_SHORT);
    }

    public static Toast getLongToast(Context context,String text){
        return Toast.makeText(context,text,Toast.LENGTH_LONG);
    }


}
