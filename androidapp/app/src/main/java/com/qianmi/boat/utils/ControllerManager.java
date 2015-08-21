package com.qianmi.boat.utils;

import android.content.Context;

import java.io.IOException;

/**
 * Created by Chen Haitao on 2015/8/21.
 */
public class ControllerManager {

    private Context mContext;
    private static ControllerManager sInstance;
    private ClientThread clientThread;

    public ControllerManager(Context context) {
        this.mContext = context;
    }

    public static ControllerManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ControllerManager(context);
        }
        return sInstance;
    }

    public void connectServer(String ip, int port) {
        try {
            clientThread = new ClientThread(ip, port);
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {

        if (clientThread != null) {
            clientThread.setMsg(msg);
        } else {
            L.e("client thread is null");
        }
    }

}
