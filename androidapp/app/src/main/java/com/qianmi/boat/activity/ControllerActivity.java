package com.qianmi.boat.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.qianmi.boat.R;
import com.qianmi.boat.utils.ControllerManager;
import com.qianmi.boat.utils.L;
import com.qianmi.boat.widget.Controller;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ControllerActivity extends Activity implements Controller.Trigger {


    @Bind(R.id.controller_left)
    Controller mControllerLeft;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_controller);
        ButterKnife.bind(this);
        mControllerLeft.setTrigger(this);
        mContext = this;

    }

    @Override
    public void onTrigger(int direction) {
        switch (direction) {
            case Controller.DIRECTION_LEFT:
                L.d("trigger left");
                L.d("connect the server ....");
                ControllerManager.getInstance(mContext).connectServer("192.168.43.2", 9999);
                break;

            case Controller.DIRECTION_RIGHT:
                L.d("trigger right");
                L.d("sending msg...");
                ControllerManager.getInstance(mContext).sendMsg("helloworld");
                break;

            case Controller.DIRECTION_UP:
                L.d("trigger up");
                break;

            case Controller.DIRECTION_DOWN:
                L.d("trigger down");
                break;
        }
    }
}
