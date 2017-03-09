package com.yubing.news.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.yubing.news.base.BasePager;

/**
 * 智慧服务Pager
 * Created by yu on 2017/3/7.
 */

public class SmartPager extends BasePager {
    public SmartPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        mTvTilte.setText("生活");
        setSlidingMenuEnable(true);//设置侧边栏是否可以打开
        TextView text=new TextView(mActivity);
        text.setText("智慧服务");
        text.setTextColor(Color.RED);
        text.setTextSize(24);
        text.setGravity(Gravity.CENTER);
      //向Framenlayout 添加动态布局
        mFlContent.addView(text);

    }
}
