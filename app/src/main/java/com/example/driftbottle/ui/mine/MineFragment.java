package com.example.driftbottle.ui.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.driftbottle.R;
import com.example.driftbottle.databinding.FragmentMineBinding;
import com.example.driftbottle.loginorregister.LoginOrRegisterActivity;
import com.example.driftbottle.net.BottleService;
import com.example.driftbottle.net.MyService;
import com.example.driftbottle.util.Base64Util;
import com.example.driftbottle.util.BitmapUtil;
import com.example.driftbottle.util.RealPathFromUriUtils;
import com.example.driftbottle.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int PHOTO_FROM_ALBUM = 0;
    private static final String TAG = "MineFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentMineBinding binding;
    private Activity activity;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMineBinding.inflate(inflater,container,false);
        activity = getActivity();
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "=== onViewCreated: ");

        ImageView imageView = binding.imageMinePagerHead;
        SharedPreferences preferences = activity.getSharedPreferences("data", Context.MODE_PRIVATE);
        String base64 = preferences.getString("base64",null);
        String userName = preferences.getString("userName","用户名");
        if (base64==null){
            Log.d(TAG, "=== base64为空");
            Glide.with(MineFragment.this)
                    .load(R.drawable.default_head_img)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(imageView);
        }else {
            if (base64.startsWith("data")){
                base64 = base64.split(",")[1];
            }
            byte[] decode = Base64.decode(base64,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode,0,decode.length);
            Glide.with(MineFragment.this)
                    .load(bitmap)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(imageView);
        }

        binding.btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PHOTO_FROM_ALBUM);
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = activity.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
                editor.putBoolean("isLogout",true);
                editor.apply();
                Intent intent = new Intent(activity, LoginOrRegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        binding.textMinePagerName.setText(userName);



//        binding.btnAlbum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,PHOTO_FROM_ALBUM);
//            }
//        });
//
//        binding.btnGetHead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://119.91.138.130:8080")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                BottleService service = retrofit.create(BottleService.class);
//                String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOjE2LCJVc2VyTmFtZSI6InRlc3QyMSIsImV4cCI6MTYzODc3NTY1NywiaWF0IjoxNjM4Njg5MjU3fQ.rbtM5LWny4S29V43JOBjDLS8iJ9hqMQtylNnmfKV8_M";
//                Call<ResponseBody> call = service.getImg(token);
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        Log.d(TAG, "=== response--> "+response.code());
//                        try {
//                            Log.d(TAG, "=== response--> "+response.body().string());
//                            Gson gson = new Gson();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                    }
//                });
//            }
//        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PHOTO_FROM_ALBUM:
                Uri uri = data.getData();
                String path = RealPathFromUriUtils.getFileAbsolutePath(getActivity(),uri);
                File file = new File(path);
                Bitmap bitmap = BitmapUtil.fileToBitmap(file);
                if (bitmap==null){
                    Glide.with(activity).load(BitmapFactory.decodeResource(getResources(),R.drawable.default_head_img))
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(binding.imageMinePagerHead);
                }else{
                    Glide.with(activity).load(bitmap)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(binding.imageMinePagerHead);
                }
                binding.imageMinePagerHead.setImageURI(uri);

                SharedPreferences preferences = activity.getSharedPreferences("data",Context.MODE_PRIVATE);
                String token = preferences.getString("token",null);
                Log.d(TAG, "=== token--> "+token);

                RequestBody fileBody = RequestBody.create(file,MediaType.parse("image/*"));
                MultipartBody.Part part = MultipartBody.Part.createFormData("img",file.getName(),fileBody);

                try {
                    Call<ResponseBody> call = MyService.getInstance().setImg(token,part);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            int code = response.code();
                            Log.d(TAG, "=== code--> "+code);
                            if (code == 200){
                                ToastUtil.getShortToast(activity,"上传成功").show();
                                SharedPreferences.Editor editor = activity.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
                                String base64 = Base64Util.fileToBase64(file);
                                editor.putString("base64", base64);
                                editor.apply();
                            }else if (code == 401){
                                ToastUtil.getShortToast(activity,"token过期").show();
                            }else if (code == 500){
                                ToastUtil.getShortToast(activity,"无此用户").show();
                            }else{
                                ToastUtil.getShortToast(activity,"未知错误").show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d(TAG, "=== onFailure: ");
                            ToastUtil.getShortToast(activity,"未知错误").show();
                        }
                    });
                }catch (Exception e){
                    Log.d(TAG, "=== e--> "+e);
                }



                break;

            default:
                break;
        }
    }





}