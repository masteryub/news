package com.yubing.news.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yubing.news.R;
import com.yubing.news.activity.MainActivity;

/**
 * 主页下5个子页面的基类
 * Created by yu on 2017/3/6.
 */

public class BasePager {
    public Activity mActivity;
    public View mRootView;
    //标题对象
    public TextView mTvTilte;

    public FrameLayout mFlContent;
    public ImageButton mImgBtnMenu;

    public BasePager(Activity mActivity) {
        this.mActivity = mActivity;
        initView();

    }

    /**
     * 初始化布局
     */
    public void initView(){
        mRootView = View.inflate(mActivity, R.layout.base_pager,null);
        mTvTilte= (TextView) mRootView.findViewById(R.id.tv_title);
        mFlContent= (FrameLayout) mRootView.findViewById(R.id.fl_base_content);

        mImgBtnMenu = (ImageButton) mRootView.findViewById(R.id.img_btn_menu);

     mImgBtnMenu.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             toggleSlidingMenu();
         }
     });

    }
    /**
     *
     * 切换SlidingMenu的状态
     * @param
     */
    private void toggleSlidingMenu() {
        MainActivity mainUi= (MainActivity) mActivity;
        SlidingMenu slidingMenu= mainUi.getSlidingMenu();
        slidingMenu.toggle();//切换状态，显示是隐藏，隐藏时显示

    }

    /**
     * 初始化数据
     */
    public void initData(){

    }
    /**
     * 设置侧边栏开启或关闭
     *
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUi = (MainActivity) mActivity;

        SlidingMenu slidingMenu = mainUi.getSlidingMenu();

        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
