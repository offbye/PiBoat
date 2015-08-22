package com.qianmi.boat.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qianmi.boat.R;
import com.qianmi.boat.envent.DataChangeEvent;
import com.qianmi.boat.envent.DataChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 2015/8/21.
 */
public class CheckListRelativeLayout extends LinearLayout {


    private Context context;
    private TextView tvTitleName;
    private TextView tvTitleDetail;
    private LinearLayout linearCont;
    private RelativeLayout relateiveLayout;

    private String name;
    private ArrayList<String> checkItemList = new ArrayList<>();

    public CheckListRelativeLayout(Context context,String name,ArrayList<String> list) {
        super(context);
        this.context = context;
        initViews();
        this.checkItemList.addAll(list);
        addContView();
    }

    public CheckListRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.check_list_linearlayout, this);
        this.tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        this.tvTitleDetail = (TextView) findViewById(R.id.tv_title_detail);
        linearCont = (LinearLayout) findViewById(R.id.linear_cont);
        relateiveLayout = (RelativeLayout) findViewById(R.id.relative_title);
        relateiveLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearCont.getVisibility() == VISIBLE) {
                    goneLinearCont();
                } else {
                    linearCont.setVisibility(VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.menu_visible);
                    linearCont.startAnimation(animation);
                }
            }
        });
    }

    private void goneLinearCont() {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.menu_gone);
        linearCont.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearCont.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void addContView() {
        for (int i = 0; i < checkItemList.size(); i++) {
            CheckItemView checkItemView = new CheckItemView(context, checkItemList.get(i));
            linearCont.addView(checkItemView);
            checkItemView.setDataChangeListener(new DataChangeListener() {
                @Override
                public void dataChange(DataChangeEvent event, int count) {
                    refreshView();
                }
            });
        }
    }

    private void refreshView() {
        int count = 0;
        int allCount = linearCont.getChildCount();
        for (int i = 0; i < allCount; i++) {
            if(((CheckItemView)linearCont.getChildAt(i)).isChoosed()){
                count++;
            }
        }
        if (allCount == count) {
            tvTitleDetail.setTextColor(getResources().getColor(R.color.white));
            goneLinearCont();
        }else{
            linearCont.setVisibility(VISIBLE);
            tvTitleDetail.setTextColor(getResources().getColor(R.color.red));
        }
        tvTitleDetail.setText(count+"/"+allCount+"["+allCount+"]");
    }

    private void getSelectedCount() {

    }

    public void setListViews(List<String> list) {
        this.checkItemList.addAll(list);
        addContView();
    }

    public void setTitleName(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        tvTitleName.setText(name);
    }

}
