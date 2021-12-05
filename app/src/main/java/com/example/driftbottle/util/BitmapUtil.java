package com.example.driftbottle.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class BitmapUtil {

    public static Bitmap fileToBitmap(File file){
        if (file == null) return null;
        byte[] encode = null;
        Bitmap bitmap = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                encode = Files.readAllBytes(file.toPath());
            }else{
                encode = new byte[(int)file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(encode);
                fis.close();
            }
            bitmap = BitmapFactory.decodeByteArray(encode,0,encode.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
