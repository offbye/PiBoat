package com.qianmi.boat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.qianmi.boat.activity.CheckActivity;
import com.qianmi.boat.activity.ControllerActivity;
import com.qianmi.boat.activity.MapActivity;
import com.qianmi.boat.activity.SettingsActivity;
import com.qianmi.boat.utils.L;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private LayoutInflater mInflater;
    private AlertDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        mInflater = LayoutInflater.from(mContext);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.controller)
    public void onClickController(View view) {
        mDialog = new AlertDialog.Builder(MainActivity.this).create();
        mDialog.show();
        mDialog.getWindow().setContentView(R.layout.input_url);
        mDialog.getWindow()
                .findViewById(R.id.connect)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText ipV = (EditText) mDialog.getWindow().findViewById(R.id.host);
                        EditText portV = (EditText) mDialog.getWindow().findViewById(R.id.port);
                        String ip = ipV.getText().toString();
                        String port = portV.getText().toString();
                        L.d("connect (" + ip + ":" + port + ")");
                        Intent intent = new Intent();
                        intent.putExtra(ControllerActivity.EXTRA_IP, ip);
                        intent.putExtra(ControllerActivity.EXTRA_PORT, port);
                        intent.setClass(mContext, ControllerActivity.class);
                        startActivity(intent);
                        mDialog.dismiss();
                    }
                });
    }

    @OnClick(R.id.navi)
    public void onClickNavi(View view) {
        startActivity(new Intent(MainActivity.this, MapActivity.class));
    }

    @OnClick(R.id.check)
    public void onClickCheck(View view) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    @OnClick(R.id.settings)
    public void onClickSetting(View view) {
        startActivity(new Intent(MainActivity.this, CheckActivity.class));
    }
}
