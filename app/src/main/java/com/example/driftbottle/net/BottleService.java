package com.example.driftbottle.net;

import com.google.gson.JsonObject;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BottleService {

    @POST("/register")
    Call<RegisterMessage> register(@Body JsonObject param);

    @POST("/login")
    Call<LoginMessage> login(@Body JsonObject param);

    @GET("/getBottle")
    Call<BottleMessage> getBottle(@Header("Authorization") String token);

    @POST("/sendBottle")
    Call<ResponseBody> throwBottle(@Header("Authorization") String token,@Body JsonObject param);

    @Multipart
    @POST("/uploadImg")
//    @Headers("Content-Type:multipart/form-data")
    Call<ResponseBody> setImg(@Header("Authorization") String token,@Part MultipartBody.Part file);

    @GET("/getImg")
    Call<ResponseBody> getImg(@Header("Authorization") String token);





}
