package com.qianmi.boat.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Chen Haitao on 2015/8/21.
 */
public class ClientThread extends Thread {

    private Socket s;
    private String ip;
    private int port;
    private BufferedReader in = null;
    private PrintWriter out = null;

    public ClientThread(String ip, int port)
            throws IOException {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try
        {
            L.d("creating socket..");
            s = new Socket(ip, port);
            s.setSoTimeout(5000);
            s.setKeepAlive(true);
            L.d("socket created");

            in = new BufferedReader(new InputStreamReader(s
                    .getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    s.getOutputStream())), true);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return s;
    }


    public void shutDown() {
        try {
            in.close();
            out.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setMsg(String msg) {
        // Send data.
        if ( s.isClosed( ) || s.isOutputShutdown( ) )
        {
            L.e("socket is closed");
            return;
        }
        L.d("before sending msg...");

        if (s.isConnected()) {
            L.d("socket is connected");
            L.d("sending msg : " + msg);
            out.println(msg);
            out.flush();
            L.d("sent...");
        }
    }
}
