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



        binding.imgNewBottle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                WindowManager.LayoutParams lp =  activity.getWindow().getAttributes();
                lp.alpha = 0.3f;
                activity.getWindow().setAttributes(lp);

                View contentView = getLayoutInflater().inflate(R.layout.popup_window_bg,null);
                int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), View.MeasureSpec.AT_MOST);
                int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View.MeasureSpec.AT_MOST);
                contentView.measure(widthMeasureSpec,heightMeasureSpec);      //view1不会自己测量，需要自己调用measure


                PopupWindow popupWindow = new PopupWindow(contentView,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp =  activity.getWindow().getAttributes();
                        lp.alpha = 1f;
                        activity.getWindow().setAttributes(lp);
                    }
                });

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

                ImageView headImg = contentView.findViewById(R.id.img_letter_head);
                Glide.with(MeetFragment.this).load(R.mipmap.ic_personal_member).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(headImg);

                Button cancelBtn = contentView.findViewById(R.id.btn_letter_cancel);
                if (cancelBtn!=null){
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                }

                Button throwBtn = contentView.findViewById(R.id.btn_letter_send);
                if (throwBtn!=null){
                    throwBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO
                            popupWindow.dismiss();
                        }
                    });
                }

                int x = view.getWidth()/2-contentView.getMeasuredWidth()/2;
                int y = view.getHeight()/2-contentView.getMeasuredHeight()/2;
                popupWindow.showAtLocation(view,Gravity.NO_GRAVITY,x,y);
            }
        });
    }





}