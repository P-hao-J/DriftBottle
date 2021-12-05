package com.example.driftbottle.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyService {

    public static final String host = "119.91.138.130:8080";

    private MyService(){}

    public static BottleService getInstance(){
        return MyServiceHolder.sInstance;
    }

    private static class MyServiceHolder{

        private static final BottleService sInstance = new Retrofit.Builder()
                .baseUrl("http://"+host)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(BottleService.class);
    }


}
