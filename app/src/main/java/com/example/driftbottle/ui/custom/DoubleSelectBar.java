package com.example.driftbottle.ui.custom;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.driftbottle.R;
import com.example.driftbottle.util.WindowUtil;

public class DoubleSelectBar extends View {

    Paint mPaint;
    private static final String TAG = "DoubleSelectBar";
    private int phoneWidth = 0;
    private float width = 0;
    private float radius = 0;
    private float height = 0;
    private String leftText;
    private String rightText;
    private PointF[] points;
    private PointF leftCircleCenter;
    private PointF rightCircleCenter;
    private Path bigPath;
    private Path leftPath;
    private Path rightPath;
    private float minX;
    private float maxX;
    private final int LEFT_REGION = 0;
    private final int RIGHT_REGION = 1;
    private int curRegion;
    float leftAxis;
    float rightAxis;
    private LeftAndRightListener listener = null;

    public DoubleSelectBar(Context context) {
        super(context);
        
    }

    public DoubleSelectBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DoubleSelectBar);
        radius = typedArray.getDimension(R.styleable.DoubleSelectBar_radius,50);
        leftText = typedArray.getString(R.styleable.DoubleSelectBar_leftText);
        rightText = typedArray.getString(R.styleable.DoubleSelectBar_rightText);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "=== onSizeChanged: ");
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        setPoints();
        setPaths();
    }

    private void init(){
        Log.d(TAG, "=== init: ");
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        points = new PointF[13];
        for (int i=0;i<points.length;i++){
            points[i] = new PointF();
        }
    }

    private void setPoints(){
        if (radius >= height){
            radius = height/3;
        }
        minX = 0;
        maxX = width;
        leftCircleCenter = new PointF();
        rightCircleCenter = new PointF();
        setPointF(leftCircleCenter, minX +radius,radius);
        setPointF(rightCircleCenter, maxX -radius,radius);
        setPointF(points[0], minX,height);
        setPointF(points[1], minX,radius);
        setPointF(points[2], minX,0);
        setPointF(points[3], minX +radius,0);
        setPointF(points[4],(minX + maxX)/2-radius,0);
        setPointF(points[5],(minX + maxX)/2,0);
        setPointF(points[6],(minX + maxX)/2+radius,0);
        setPointF(points[7], maxX -radius,0);
        setPointF(points[8], maxX,0);
        setPointF(points[9], maxX,radius);
        setPointF(points[10], maxX,height);
        setPointF(points[11],(minX + maxX)/2,height);
        setPointF(points[12],(minX + maxX)/2,radius);
        leftAxis = (minX+maxX)/4;
        rightAxis = (minX+maxX)*3/4;
    }

    private void setPointF(PointF point,float x,float y){
        if (point==null) return;
        point.set(x,y);
    }

    private void setPaths(){
        bigPath = new Path();
        bigPath.moveTo(points[0].x,points[0].y);
        bigPath.lineTo(points[1].x,points[1].y);
        bigPath.quadTo(points[2].x,points[2].y,points[3].x,points[3].y);
        bigPath.lineTo(points[7].x,points[7].y);
        bigPath.quadTo(points[8].x,points[8].y,points[9].x,points[9].y);
        bigPath.lineTo(points[10].x,points[10].y);
        bigPath.close();

        leftPath = new Path();
        leftPath.moveTo(points[0].x,points[0].y);
        leftPath.lineTo(points[1].x,points[1].y);
        leftPath.quadTo(points[2].x,points[2].y,points[3].x,points[3].y);
        leftPath.lineTo(points[4].x,points[4].y);
        leftPath.quadTo(points[5].x,points[5].y,points[12].x,points[12].y);
        leftPath.lineTo(points[11].x,points[11].y);
        leftPath.close();

        rightPath = new Path();
        rightPath.moveTo(points[11].x,points[11].y);
        rightPath.lineTo(points[12].x,points[12].y);
        rightPath.quadTo(points[5].x,points[5].y,points[6].x,points[6].y);
        rightPath.lineTo(points[7].x,points[7].y);
        rightPath.quadTo(points[8].x,points[8].y,points[9].x,points[9].y);
        rightPath.lineTo(points[10].x,points[10].y);
        rightPath.close();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        PointF point = new PointF(x,y);
        if (outOfBound(point)){
            return false;
        }
        int touch = 0;
        if (x<(minX+maxX)/2){
            touch = LEFT_REGION;
        }else{
            touch = RIGHT_REGION;
        }
        if (curRegion!=touch){
            curRegion = touch;
            invalidate();
            if (listener!=null){
                if (curRegion==LEFT_REGION) listener.onClickLeft();
                else listener.onClickRight();
            }
        }
        return true;
    }

    private float getDistance(PointF point,PointF circleCenter){
        return (float) Math.sqrt(Math.pow(point.x-circleCenter.x,2)+Math.pow(point.y-circleCenter.y,2));
    }

    private boolean outOfBound(PointF point){
        if (point.x<leftCircleCenter.x && point.y<leftCircleCenter.y){
            if (getDistance(point,leftCircleCenter) > radius){
                return true;
            }
        }
        if (point.x>rightCircleCenter.x && point.y<leftCircleCenter.y){
            if (getDistance(point,rightCircleCenter) > radius){
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(getResources().getColor(R.color.pink_double_select_bar));
        canvas.drawPath(bigPath,mPaint);
        if(curRegion==LEFT_REGION){
            mPaint.setColor(Color.WHITE);
            canvas.drawPath(leftPath,mPaint);
        }else{
            mPaint.setColor(Color.WHITE);
            canvas.drawPath(rightPath,mPaint);
        }
        mPaint.setTextSize(60);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);

        if (leftText!=null){
            float leftTextSize = mPaint.measureText(leftText);
            float leftX = leftAxis-leftTextSize/2;
            float leftY = height/2+leftTextSize/4;
            canvas.drawText(leftText,leftX,leftY,mPaint);
        }
        if (rightText!=null){
            float rightTextSize = mPaint.measureText(rightText);
            float rightX = rightAxis-rightTextSize/2;
            float rightY = height/2+rightTextSize/4;
            canvas.drawText(rightText,rightX,rightY,mPaint);
        }

    }

    public void setMyClickListener(LeftAndRightListener listener){
        this.listener = listener;
    }

     public interface LeftAndRightListener{
         void onClickLeft();
         void onClickRight();
    }


}
