package com.qianmi.boat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.qianmi.boat.R;

/**
 * Created by su on 2015/8/22.
 */
public class ThrottleBar extends LinearLayout {


    private SeekBarView mySeekBarView;
    private ThrottleTrigger mTrigger;
    private Context context;

    public ThrottleBar(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ThrottleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        mySeekBarView = new SeekBarView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
       // params.setMargins(0, ScreenUtils.dip2px(20,context),0,ScreenUtils.dip2px(20,context));
        this.setBackgroundResource(R.drawable.verbar);
        mySeekBarView.setProgressDrawable(null);
        mySeekBarView.setThumb(getResources().getDrawable(R.drawable.thumb));
        this.addView(mySeekBarView, params);
        mySeekBarView.setMax(100);
        mySeekBarView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTrigger.onThrottleTrigger(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setThrottleTrigger(ThrottleTrigger trigger) {
        this.mTrigger = trigger;
    }

    public void removeThrottleTrigger() {
        this.mTrigger = null;
    }

    public interface ThrottleTrigger {
        void onThrottleTrigger(int direction);
    }
}
