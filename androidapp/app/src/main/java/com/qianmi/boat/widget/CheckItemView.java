package com.qianmi.boat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianmi.boat.R;
import com.qianmi.boat.envent.DataChangeEvent;
import com.qianmi.boat.envent.DataChangeListener;

/**
 * Created by su on 2015/8/21.
 */
public class CheckItemView extends RelativeLayout implements DataChangeListener{

    private Context context;
    private CheckBox mCheckBox;
    private TextView mTextView;
    private DataChangeListener myDataChangeListener;

    public CheckItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
        addListener();
    }

    public CheckItemView(Context context,String cont) {
        super(context);
        this.context = context;
        initViews();
        mTextView.setText(cont);
        addListener();
    }

    private void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.check_item_relative, this);
        this.mCheckBox = (CheckBox) findViewById(R.id.checkbox_item);
        this.mTextView = (TextView) findViewById(R.id.textview_item);
    }

    public boolean isChoosed() {
        return mCheckBox.isChecked();
    }

    private void addListener() {
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    onDataChange(0);
                }else {
                    onDataChange(1);
                }
            }
        });
    }

    public void setDataChangeListener(DataChangeListener listener) {
        myDataChangeListener = listener;
    }

    @Override
    public void dataChange(DataChangeEvent event,int count) {
        // TODO Auto-generated method stub
    }

    protected void onDataChange(int count) {
        if (myDataChangeListener == null)
            return;
        DataChangeEvent event = new DataChangeEvent(this, count);
        myDataChangeListener.dataChange(event,count);
    }
}
