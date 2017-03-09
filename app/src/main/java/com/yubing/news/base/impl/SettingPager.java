package com.yubing.news.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yubing.news.base.BasePager;

/**
 * 设置Pager
 * Created by yu on 2017/3/7.
 */

public class SettingPager extends BasePager {
    public SettingPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        mTvTilte.setText("设置");
        setSlidingMenuEnable(false);//设置侧边栏是否可以打开
        mImgBtnMenu.setVisibility(View.GONE);
        TextView text=new TextView(mActivity);
        text.setText("设置");
        text.setTextColor(Color.RED);
        text.setTextSize(24);
        text.setGravity(Gravity.CENTER);
      //向Framenlayout 添加动态布局
        mFlContent.addView(text);

    }
}
