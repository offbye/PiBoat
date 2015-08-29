package com.qianmi.boat.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.qianmi.boat.R;
import com.qianmi.boat.utils.ControllerManager;

public class CheckActivity extends AppCompatActivity {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        mContext =this;

        Button  buttonReboot = (Button)findViewById(R.id.buttonReboot);
        buttonReboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ControllerManager.getInstance(mContext).sendMsg("reboot");

            }
        });

        Button  buttonShutdown = (Button)findViewById(R.id.buttonShutdown);
        buttonShutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ControllerManager.getInstance(mContext).sendMsg("halt");

            }
        });

        Button  buttonRstp = (Button)findViewById(R.id.buttonRtsp);
        buttonRstp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ControllerManager.getInstance(mContext).sendMsg("rtsp");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
