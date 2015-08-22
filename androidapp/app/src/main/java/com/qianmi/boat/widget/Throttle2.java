package com.qianmi.boat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qianmi.boat.R;

/**
 * Created by Chen Haitao on 2015/8/21.
 */
public class Throttle2 extends LinearLayout {

    private Context mContext;
    private ImageButton mOne;
    private ImageButton mTwo;
    private ImageButton mThree;
    private TextView mStatus;

    private ThrottleTrigger mTrigger;

    public static final int T_1 = 1;
    public static final int T_2 = 2;
    public static final int T_3 = 3;

    public Throttle2(Context context) {
        super(context);
    }

    public Throttle2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.throttle_layout, this);
        mStatus = (TextView) view.findViewById(R.id.status);
        mOne = (ImageButton) view.findViewById(R.id.t_1);
        mOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOne != null) {
                    mTrigger.onThrottleTrigger(T_1);
                    mStatus.setText("档位1");
                }
            }
        });
        mTwo = (ImageButton) view.findViewById(R.id.t_2);
        mTwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwo != null) {
                    mTrigger.onThrottleTrigger(T_2);
                    mStatus.setText("档位2");
                }
            }
        });
        mThree = (ImageButton) view.findViewById(R.id.t_3);
        mThree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mThree != null) {
                    mTrigger.onThrottleTrigger(T_3);
                    mStatus.setText("档位3");
                }
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
