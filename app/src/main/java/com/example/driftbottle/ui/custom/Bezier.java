package com.example.driftbottle.ui.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class Bezier extends View {

    PointF start,end,control;
    Paint paint;
    float centerX,centerY;
    private Path path;
    private static final String TAG = "Bezier";

    @Override
    public long getDrawingTime() {
        return super.getDrawingTime();
    }

    public Bezier(Context context) {
        super(context);
        init();
    }

    public Bezier(Context context, @Nullable AttributeSet attrs) {
        //xml里的空间要有这个构造函数
        super(context, attrs);

        if (context instanceof Activity){
            Activity activity = (Activity) context;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            Log.d(TAG, "=== width--> "+displayMetrics.widthPixels);

        }

        init();
    }

    private void init(){
        start = new PointF();
        end = new PointF();
        control = new PointF();
        path = new Path();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w/2;
        centerY = h/2;
        start.x = centerX-200;
        start.y = centerY;
        end.x = centerX+200;
        end.y = centerY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        control.x = event.getX();
        control.y = event.getY();
        invalidate();
        return true;    //一定要返回true
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //绘制点
        canvas.drawPoint(start.x,start.y,paint);
        canvas.drawPoint(end.x,end.y,paint);
        canvas.drawPoint(control.x,control.y,paint);



        //绘制辅助线
        paint.setStrokeWidth(4);
        canvas.drawLine(start.x,start.y,control.x,control.y,paint);
        canvas.drawLine(control.x,control.y,end.x,end.y,paint);

        paint.setStrokeWidth(8);

        path.reset();
        path.moveTo(start.x,start.y);
        path.quadTo(control.x,control.y,end.x,end.y);
        canvas.drawPath(path,paint);

    }
}
