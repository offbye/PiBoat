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
        linarSetting1.setListViews( Arrays.asList("Larry", "Moe", "Curly"));
        linarSetting2.setListViews( Arrays.asList("Larry", "Curly"));
        linarSetting3.setListViews( Arrays.asList("Larry", "Moe", "Curly"));

    }
}
