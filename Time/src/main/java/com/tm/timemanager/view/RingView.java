package com.tm.timemanager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.tm.timemanager.application.MyApplication;


/**
 * Created by CHENQIAO on 2016/4/19.
 */
public class RingView extends View{


    int i ;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mRotate;
    private Matrix mMatrix = new Matrix();
    private Shader mShader;

    public RingView(Context context) {
        super(context);
        initview(context);
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview(context);
    }

    private void initview(Context context) {
        int width = MyApplication.getPhoneWidth(context);
        int height = MyApplication.getPhoneHeight(context);
        i = (height > width) ? width : height;

        setFocusable(true);
        setFocusableInTouchMode(true);
        float x = 200;
        float y = 200;
        mShader = new SweepGradient(x, y, new int[] {0xFF09F68C,
                0xFFB0F44B,
                0xFFE8DD30}, null);
        mPaint.setShader(mShader);
        mPaint.setStyle(Paint.Style.STROKE);
        PathEffect effect = new DashPathEffect(new float[] { 5, 8, 5,8}, 1);
        mPaint.setPathEffect(effect);
        mPaint.setStrokeWidth(i/10);
    }

    public void getArc(Canvas canvas, float o_x, float o_y, float r,
                       float startangel, float endangel, Paint paint){
        RectF rect = new RectF(o_x - r, o_y - r, o_x + r, o_y + r);
        Path path = new Path();
        path.moveTo(o_x,o_y);
        path.lineTo((float)(o_x+r*Math.cos(startangel*Math.PI/180))
                , (float)(o_y+r*Math.sin(startangel*Math.PI/180)));
        path.lineTo((float)(o_x+r*Math.cos(endangel*Math.PI/180))
                , (float)(o_y+r*Math.sin(endangel*Math.PI/180)));
        path.addArc(rect, startangel, endangel-startangel);
        canvas.clipPath(path);
        canvas.drawCircle(o_x, o_y, r, paint);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = mPaint;
        float x = i/2;
        float y = i/2;

        canvas.drawColor(Color.TRANSPARENT);

        mMatrix.setRotate(mRotate, x, y);
        mShader.setLocalMatrix(mMatrix);
        mRotate += 3;
        if (mRotate >= 360) {
            mRotate = 0;
        }
        invalidate();
        getArc(canvas,x,y,i/2-80,0,360,paint);
    }
}
