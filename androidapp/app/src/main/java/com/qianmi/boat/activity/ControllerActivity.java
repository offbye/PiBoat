package com.qianmi.boat.activity;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.VideoView;

import com.qianmi.boat.R;
import com.qianmi.boat.utils.ControllerManager;
import com.qianmi.boat.utils.L;
import com.qianmi.boat.widget.Controller;
import com.qianmi.boat.widget.Throttle;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ControllerActivity extends Activity implements Controller.Trigger, Throttle.ThrottleTrigger, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final String TEST_URL = "rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp";

    public static final String EXTRA_IP = "extra_ip";
    public static final String EXTRA_PORT = "extra_port";

    @Bind(R.id.controller_left)
    Controller mControllerLeft;
    @Bind(R.id.video)
    VideoView mVideoView;
    @Bind(R.id.throttle_right)
    Throttle mThrottle;
    @Bind(R.id.tv_msg)
    TextView mMsg;

    private Context mContext;
    private int mVHeight;
    private int mVWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_controller);
        ButterKnife.bind(this);
        mContext = this;
        mControllerLeft.setTrigger(this);
        mThrottle.setThrottleTrigger(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

        showMsg("初始化...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ControllerManager.getInstance(mContext).shutDown();
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
                L.d("play rtsp");
                playRtspStream(TEST_URL);
                break;

            case Controller.DIRECTION_DOWN:
                L.d("trigger down");
                break;
        }
    }

    private void playRtspStream(String rtspUrl){
        mVideoView.setVideoURI(Uri.parse(rtspUrl));
        mVideoView.requestFocus();
        mVideoView.start();
    }

    private void showMsg(String msg) {
        if (mMsg != null) {
            mMsg.setText(msg);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        showMsg("连接成功");
        new Handler(mContext.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showMsg("");
            }
        }, 1000);

        mVHeight = mp.getVideoHeight();
        mVWidth = mp.getVideoWidth();
        L.d("video width and height : " + mVWidth + "," + mVHeight);
    }

    @Override
    public void onThrottleTrigger(int direction) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
