package com.qianmi.boat.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Chen Haitao on 2015/8/21.
 */
public class ClientThread extends Thread {

    private Socket s;
    private String ip;
    private int port;
    private int timeout = 10000;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Context ctx = null;
    private Handler inHandler;
    private Handler outHandler;
    public boolean isRun = true;

    public ClientThread(String ip, int port, Context context, Handler inhandler, Handler outhandler)
            throws IOException {
        this.ip = ip;
        this.port = port;
        this.ctx = context;
        this.inHandler = inhandler;
        this.outHandler = outhandler;
    }

    /**
     * 连接socket服务器
     */
    public void conn() {

        try {
            L.i("连接中……");
            s = new Socket(ip, port);
            s.setSoTimeout(timeout);// 设置阻塞时间
            s.setKeepAlive(true);
            in = new BufferedReader(new InputStreamReader(
                    s.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    s.getOutputStream())), true);
            L.d("输入输出流获取成功");
        } catch (UnknownHostException e) {
            L.d("连接错误UnknownHostException 重新获取");
            e.printStackTrace();
            conn();
        } catch (IOException e) {
            L.d("连接服务器io错误");
            e.printStackTrace();
        } catch (Exception e) {
            L.d("连接服务器错误Exception" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            conn();
            String line = "";
            while (isRun) {
                try {
                    if (s != null) {
                        L.d("2.检测数据");
                        while ((line = in.readLine()) != null) {
                            L.d("3.getdata" + line + " len=" + line.length());
                            L.d("4.start set Message");
                            Message msg = inHandler.obtainMessage();
                            msg.obj = line;
                            inHandler.sendMessage(msg);// 结果返回给UI处理
                            L.d("5.send to handler");
                        }

                    } else {
                        L.d("没有可用连接");
                        conn();
                    }
                } catch (Exception e) {
                    L.d("数据接收错误" + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     *
     * @param mess
     */
    public void Send(String mess) {
        try {
            if (s != null) {
                L.d("发送" + mess + "至"
                        + s.getInetAddress().getHostAddress() + ":"
                        + String.valueOf(s.getPort()));
                out.println(mess);
                out.flush();
                L.d("发送成功");
                Message msg = outHandler.obtainMessage();
                msg.obj = mess;
                msg.what = 1;
                outHandler.sendMessage(msg);// 结果返回给UI处理
            } else {
                L.d("client 不存在");
                Message msg = outHandler.obtainMessage();
                msg.obj = mess;
                msg.what = 0;
                outHandler.sendMessage(msg);// 结果返回给UI处理
                L.d("连接不存在重新连接");
                conn();
            }

        } catch (Exception e) {
            L.d("send error");
            e.printStackTrace();
        } finally {
            L.d("发送完毕");

        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (s != null && !s.isClosed()) {
                s.close();
                s = null;
            }
        } catch (Exception e) {
            L.d( "close err");
            e.printStackTrace();
        }

    }
}
