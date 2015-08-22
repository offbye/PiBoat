package com.qianmi.boat.activity;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.VideoView;

import com.qianmi.boat.R;
import com.qianmi.boat.utils.ControllerManager;
import com.qianmi.boat.utils.L;
import com.qianmi.boat.widget.Controller;
import com.qianmi.boat.widget.Throttle2;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ControllerActivity extends Activity implements Controller.Trigger, Throttle2.ThrottleTrigger, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final String TEST_URL = "rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp";

    public static final String EXTRA_IP = "extra_ip";
    public static final String EXTRA_PORT = "extra_port";

    @Bind(R.id.controller_left)
    Controller mControllerLeft;
    @Bind(R.id.video)
    VideoView mVideoView;
    @Bind(R.id.throttle_right)
    Throttle2 mThrottle;
    @Bind(R.id.tv_msg)
    TextView mMsg;
    @Bind(R.id.tv_msg_control)
    TextView mMsgControl;

    private Context mContext;
    private int mVHeight;
    private int mVWidth;
    private Handler mInHandler;
    private Handler mOutHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_controller);
        ButterKnife.bind(this);
        initHandler();
        mContext = this;
        mControllerLeft.setTrigger(this);
        mThrottle.setThrottleTrigger(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                showMsg("缓冲数据...");
                new Handler(mContext.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showMsg("");
                    }
                }, 3000);
                return true;
            }
        });

        showMsg("初始化...");
    }

    private void initHandler() {
        mInHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    if (msg.obj != null) {
                        String s = msg.obj.toString();
                        if (s.trim().length() > 0) {
                            mMsgControl.setText(s);
                        } else {
                            L.d("没有数据返回不更新");
                        }
                    }

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        };

        mOutHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    if (msg.obj != null) {
                        String s = msg.obj.toString();
                        if (msg.what == 1) {
                            mMsgControl.setText(s + "      发送成功");
                        } else {
                            mMsgControl.setText(s + "      发送失败");
                        }
                    }

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.pause();
        ControllerManager.getInstance(mContext).stopConnect();
    }

    @Override
    public void onTrigger(int direction) {
        switch (direction) {
            case Controller.DIRECTION_LEFT:
                L.d("trigger left");
                L.d("connect the server ....");
                ControllerManager.getInstance(mContext).connectServer("192.168.43.2", 9999, mContext, mInHandler, mOutHandler);
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

        mVHeight = mp.getVideoHeight();
        mVWidth = mp.getVideoWidth();
        L.d("video width and height : " + mVWidth + "," + mVHeight);
    }

    @Override
    public void onThrottleTrigger(int direction) {
        switch (direction) {
            case Throttle2.T_1:

                break;

            case Throttle2.T_2:

                break;

            case Throttle2.T_3:

                break;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        showMsg("连接错误");
        return true;
    }
}
