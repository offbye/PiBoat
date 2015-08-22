package com.qianmi.boat.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.qianmi.boat.R;
import com.qianmi.boat.widget.CheckListRelativeLayout;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.linar_setting1)
    CheckListRelativeLayout linarSetting1;
    @Bind(R.id.linar_setting2)
    CheckListRelativeLayout linarSetting2;
    @Bind(R.id.linar_setting3)
    CheckListRelativeLayout linarSetting3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        settData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    private void settData() {
        linarSetting1.setTitleName("使用者检查清单");
        linarSetting1.setListViews(Arrays.asList("相机已连接并打开电源"));

        linarSetting2.setTitleName("控制面板");
        linarSetting2.setListViews(Arrays.asList("螺旋桨", "方向舵"));

        linarSetting3.setTitleName("系统检查清单");
        linarSetting3.setListViews( Arrays.asList("网络", "GPS"));

    }
}
