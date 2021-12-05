package com.example.driftbottle.util;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

public class Base64Util {

    public static String fileToBase64(File file) {
        if (file == null) return null;
        String ret = null;
        byte[] encode = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                encode = Files.readAllBytes(file.toPath());
            }else{
                encode = new byte[(int)file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(encode);
                fis.close();
            }
            ret = Base64.encodeToString(encode,Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }






}
