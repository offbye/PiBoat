package com.qianmi.boat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qianmi.boat.activity.CheckActivity;
import com.qianmi.boat.activity.ControllerActivity;
import com.qianmi.boat.activity.NavigationActivity;
import com.qianmi.boat.activity.SettingsActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.controller)
    public void onClickController(View view) {
        startActivity(new Intent(MainActivity.this, ControllerActivity.class));
    }

    @OnClick(R.id.navi)
    public void onClickNavi(View view) {
        startActivity(new Intent(MainActivity.this, NavigationActivity.class));
    }

    @OnClick(R.id.check)
    public void onClickCheck(View view) {
        startActivity(new Intent(MainActivity.this, CheckActivity.class));
    }

    @OnClick(R.id.navi)
    public void onClickSetting(View view) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }
}
