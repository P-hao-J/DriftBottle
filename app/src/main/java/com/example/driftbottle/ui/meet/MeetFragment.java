package com.example.driftbottle.ui.meet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.driftbottle.R;
import com.example.driftbottle.databinding.FragmentMeetBinding;
import com.example.driftbottle.loginorregister.LoginOrRegisterActivity;
import com.example.driftbottle.main.MainActivity;
import com.example.driftbottle.net.BottleMessage;
import com.example.driftbottle.net.MyService;
import com.example.driftbottle.util.ToastUtil;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetFragment extends Fragment {

    private static final String TAG = "MeetFragment";
    private FragmentMeetBinding binding = null;
    private FragmentActivity activity = null;
    private ViewGroup rootView = null;
    private int MODE_THROW = 0;
    private int MODE_PICk = 1;
    private int pickNum = 0;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView letterTextView;
    private EditText letterEditText;
    private RelativeLayout rl;
    private MainActivity mainActivity;
    private ProgressBar progressBar;

    public MeetFragment() {
        // Required empty public constructor
    }


    public static MeetFragment newInstance() {
        MeetFragment fragment = new MeetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMeetBinding.inflate(inflater,container,false);
        activity = getActivity();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = (ViewGroup) view;


        setPickBottle();
        View.OnClickListener listener = setBottleListener(R.layout.popup_window_bg,R.id.btn_letter_cancel,R.id.btn_letter_send,R.id.img_letter_head,R.id.text_letter_name,R.id.edittext_letter,MODE_THROW);
        binding.imgNewBottle.setOnClickListener(listener);


    }

    private View.OnClickListener setBottleListener(int popup_window_bg, int btn_letter_cancel, int btn_letter_send, int img_letter_head,int userNameTextViewId,int editTextId,int mode) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = activity.getSharedPreferences("data",Context.MODE_PRIVATE);
                String myName = sharedPreferences.getString("userName","用户名");
                String base64 = sharedPreferences.getString("base64",null);
                showPopupWindow(popup_window_bg,btn_letter_cancel,btn_letter_send,img_letter_head,userNameTextViewId,editTextId,null,myName,base64,mode);
            }
        };
        return listener;
    }

    private View.OnClickListener setPickBottleListener(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    mainActivity = (MainActivity) activity;
                    mainActivity.refuseClick();
                    progressBar = new ProgressBar(activity);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(200,200);
                    lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                    rl = (RelativeLayout) rootView;
                    rl.addView(progressBar,lp);

                    v.setVisibility(View.INVISIBLE);
                    addPickNum();
                    SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                    String token = sharedPreferences.getString("token",null);
                    Call<BottleMessage> call =  MyService.getInstance().getBottle(token);
                    call.enqueue(new Callback<BottleMessage>() {
                        @Override
                        public void onResponse(Call<BottleMessage> call, Response<BottleMessage> response) {
                            int code = response.code();
                            if (code==200){
                                String base64 = response.body().getImg_base64();
                                String message = response.body().getMessage();
                                String userName = response.body().getUser_name();
                                try {
                                    showPopupWindow(R.layout.popup_window_bg2,R.id.btn_receive_letter_throw_back,
                                            R.id.btn_receive_letter_respond,R.id.img_receive_letter_head,
                                            R.id.text_receive_letter_name,R.id.text_receive_letter,message,userName,base64,MODE_PICk);
                                }catch (Exception e){
                                    Log.d(TAG, "=== e--> "+e);
                                }

                            }else if (code==401){
                                ToastUtil.getShortToast(activity,"token过期").show();
                            }else{
                                ToastUtil.getShortToast(activity,"未知错误").show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BottleMessage> call, Throwable t) {
                            rl.removeView(progressBar);
                            mainActivity.recoverClickable();
                        }
                    });
                }catch (Exception e){
                    Log.d(TAG, "=== e--> "+e);
                }

            }
        };
        return listener;
    }

    private void showPopupWindow(int layoutId,int leftBtnId,int rightBtnId,int imageViewId,int userNameTextViewId,int editTextId, String message,String userName,String base64,int mode){

        if (rl!=null){
            rl.removeView(progressBar);
        }
        if (mainActivity!=null){
            mainActivity.recoverClickable();
        }

        //变暗
        WindowManager.LayoutParams lp =  activity.getWindow().getAttributes();
        lp.alpha = 0.3f;
        activity.getWindow().setAttributes(lp);

        //充入布局，测量
        View contentView = getLayoutInflater().inflate(layoutId,null);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(rootView.getMeasuredWidth(), View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(rootView.getMeasuredHeight(), View.MeasureSpec.AT_MOST);
        contentView.measure(widthMeasureSpec,heightMeasureSpec);

        //下面这句不能用只用一个view参数的接口,
        PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                double width = v.getWidth();
                double height = v.getHeight();
                double x = event.getX();
                double y = event.getY();
                if (x>0 && x<width && y>0 && y<height){
                    //在弹窗内,需要返回false,否则弹窗内事件无法触发
                    return false;
                }
                return true;
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp =  activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });

        TextView userNameText = contentView.findViewById(userNameTextViewId);
        userNameText.setText(userName);

        ImageView headImg = contentView.findViewById(imageViewId);
        if (base64.startsWith("data")){
            base64 = base64.split(",")[1];
        }
        byte[] decode = Base64.decode(base64,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode,0,decode.length);
        Glide.with(MeetFragment.this).load(bitmap).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(headImg);

        letterTextView = null;
        letterEditText = null;
        if (mode == MODE_PICk){
            letterTextView = contentView.findViewById(editTextId);
            letterTextView.setText(message);
        }else if (mode == MODE_THROW){
            letterEditText = contentView.findViewById(editTextId);
        }

        Button throwBackBtn = contentView.findViewById(leftBtnId);
        if (throwBackBtn!=null){
            throwBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "=== onClick: dismiss");
                    popupWindow.dismiss();
                }
            });
        }

        Button respondBtn = contentView.findViewById(rightBtnId);
        if (respondBtn!=null){
            respondBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mode == MODE_PICk){
                        //TODO
                    }else if (mode == MODE_THROW){
                        if (letterEditText==null) return;
                        String newMessage = letterEditText.getText().toString();
                        if (TextUtils.isEmpty(newMessage)){
                            ToastUtil.getShortToast(activity, "内容不能为空").show();
                            return;
                        }
                        SharedPreferences sharedPreferences = activity.getSharedPreferences("data",Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString("token","1");
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("message",newMessage);
                        Log.d(TAG, "=== message--> "+newMessage);
                        Call<ResponseBody> call = MyService.getInstance().throwBottle(token,jsonObject);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                int code = response.code();
                                Log.d(TAG, "=== response code--> "+code);
                                if (code == 200){
                                    ToastUtil.getShortToast(v.getContext(),"漂流瓶已扔出").show();
                                }else if (code==401){
                                    ToastUtil.getShortToast(v.getContext(),"token过期").show();
                                }else{
                                    ToastUtil.getShortToast(v.getContext(),"未知错误").show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                ToastUtil.getShortToast(v.getContext(),"未知错误").show();
                            }
                        });
                    }
                    popupWindow.dismiss();
                }
            });
        }

        int x = rootView.getWidth()/2-contentView.getMeasuredWidth()/2;
        int y = rootView.getHeight()/2-contentView.getMeasuredHeight()/2;
        popupWindow.showAtLocation(rootView,Gravity.NO_GRAVITY,x,y);
    }

    private void addPickNum(){
        pickNum++;
        if (pickNum==5){
            binding.imgBottle1.setVisibility(View.VISIBLE);
            binding.imgBottle2.setVisibility(View.VISIBLE);
            binding.imgBottle3.setVisibility(View.VISIBLE);
            binding.imgBottle4.setVisibility(View.VISIBLE);
            binding.imgBottle5.setVisibility(View.VISIBLE);
            pickNum = 0;
        }
    }

    private void setPickBottle(){
        Log.d(TAG, "=== setPickBottle: ");
        View.OnClickListener listener = setPickBottleListener();
        binding.imgBottle1.setOnClickListener(listener);
        binding.imgBottle2.setOnClickListener(listener);
        binding.imgBottle3.setOnClickListener(listener);
        binding.imgBottle4.setOnClickListener(listener);
        binding.imgBottle5.setOnClickListener(listener);
    }



}