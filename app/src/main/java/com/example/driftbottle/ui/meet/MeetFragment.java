package com.example.driftbottle.ui.meet;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.driftbottle.R;
import com.example.driftbottle.databinding.FragmentMeetBinding;

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



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        View.OnClickListener listener = setBottleListener(R.layout.popup_window_bg,R.id.btn_letter_cancel,R.id.btn_letter_send,R.id.img_letter_head);
        binding.imgNewBottle.setOnClickListener(listener);


    }

    private View.OnClickListener setBottleListener(int layoutId,int leftBtnId,int rightBtnId,int imageViewId){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                WindowManager.LayoutParams lp =  activity.getWindow().getAttributes();
                lp.alpha = 0.3f;
                activity.getWindow().setAttributes(lp);

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

                ImageView headImg = contentView.findViewById(imageViewId);
                Log.d(TAG, "=== contentView--> "+contentView);
                Log.d(TAG, "=== headImg--> "+headImg);
                Glide.with(MeetFragment.this).load(R.mipmap.ic_personal_member).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(headImg);

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
                            //TODO
                            popupWindow.dismiss();
                        }
                    });
                }

                int x = rootView.getWidth()/2-contentView.getMeasuredWidth()/2;
                int y = rootView.getHeight()/2-contentView.getMeasuredHeight()/2;
                popupWindow.showAtLocation(rootView,Gravity.NO_GRAVITY,x,y);

            }
        };
        return listener;
    }

    private void setPickBottle(){
        Log.d(TAG, "=== setPickBottle: ");
        View.OnClickListener listener = setBottleListener(R.layout.popup_window_bg2,R.id.btn_receive_letter_throw_back,R.id.btn_receive_letter_respond,R.id.img_receive_letter_head);
        binding.imgBottle1.setOnClickListener(listener);
        binding.imgBottle2.setOnClickListener(listener);
        binding.imgBottle3.setOnClickListener(listener);
        binding.imgBottle4.setOnClickListener(listener);
        binding.imgBottle5.setOnClickListener(listener);
    }



}