package com.qianmi.boat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qianmi.boat.R;

/**
 * Created by Chen Haitao on 2015/8/21.
 */
public class Controller extends View {

    private Context mContext;
    private Paint mPaint;
    private Bitmap mBg;
    private Bitmap mReadyPoint;
    private Bitmap mTouchPoint;
    private int[] mPosInit;
    private int[] mPosCurrent;
    private int mWidth;
    private int mHeight;


    public Controller(Context context) {
        this(context, null);

    }

    public Controller(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mPosInit = new int[2];
        mPosCurrent = new int[2];
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mWidth = getWidth();
        mHeight = getHeight();
        mBg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        mReadyPoint = BitmapFactory.decodeResource(getResources(), R.drawable.ready_point);
        mTouchPoint = BitmapFactory.decodeResource(getResources(), R.drawable.touch_point);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        event.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBg, 0, 0, mPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
