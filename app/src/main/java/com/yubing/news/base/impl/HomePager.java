package com.yubing.news.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;

import com.yubing.news.base.BasePager;

/**
 * 首页Pager
 * Created by yu on 2017/3/7.
 */

public class HomePager extends BasePager {
    public HomePager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        mTvTilte.setText("今日头条");

        mImgBtnMenu.setVisibility(View.GONE);

        setSlidingMenuEnable(false);//设置侧边栏是否可以打开
        TextView text=new TextView(mActivity);
        text.setText("首页");
        text.setTextColor(Color.RED);
        text.setTextSize(24);
        text.setGravity(Gravity.CENTER);
      //向Framenlayout 添加动态布局
        mFlContent.addView(text);

    }


}
