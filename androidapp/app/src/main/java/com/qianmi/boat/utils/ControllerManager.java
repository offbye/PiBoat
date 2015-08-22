package com.qianmi.boat.utils;

import android.content.Context;
import android.os.Handler;

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

    public void connectServer(String ip, int port, Context context, Handler inHandler, Handler outHandler) {
        try {
            clientThread = new ClientThread(ip, port, context, inHandler,outHandler );
            clientThread.start();
            SPUtils.put(mContext, "ip", ip);
            SPUtils.put(mContext, "port", port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {

        if (clientThread != null) {
            clientThread.Send(msg);
        } else {
            L.e("client thread is null");
        }
    }

    public void stopConnect() {
        if (clientThread != null) {
            clientThread.isRun = false;
            clientThread.close();
            clientThread = null;
            L.d("disconnect");
        }
    }
}
