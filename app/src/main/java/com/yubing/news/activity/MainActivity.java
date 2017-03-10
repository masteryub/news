package com.yubing.news.activity;

import android.content.res.Resources;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yubing.news.R;
import com.yubing.news.fragment.ContentFragment;
import com.yubing.news.fragment.LeftMenuFragment;


/**
 * Created by yu on 2017/3/5.
 */

public class MainActivity extends SlidingFragmentActivity {
    private static final String FRAGEMENT_LEFT_MENU = "fragement_left_menu";
    private static final String FRAGMENT_CONTENT = "fragment_content";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //设置侧边栏布局
        setBehindContentView(R.layout.activity_left_menu);
        //获取侧边栏对象
        SlidingMenu slidingMenu = getSlidingMenu();
        //设置为全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置预留屏幕宽度
      /* List<Integer>list=Utils.windowXY(this);
        int x=list.get(0);*/
        slidingMenu.setBehindOffset(300);

        //可以设置右边的侧边栏   再来一个布局文件 要进行左右设置
        //slidingMenu.setSecondaryMenu();
        // slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        initFragement();

    }

    private void initFragement() {
        FragmentManager fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        //用fragement 替换framalayout    最后一个参数给这个布局取一个名字来区分是哪个
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), FRAGEMENT_LEFT_MENU);
        transaction.replace(R.id.fl_content, new ContentFragment(), FRAGMENT_CONTENT);
        //提交事务
        transaction.commit();
        //要使用时可以通过tag取找
        //Fragment leftMenuFragement = fm.findFragmentByTag(FRAGEMENT_LEFT_MENU);
    }


    /**
     * @return 侧边栏对象
     */
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(FRAGEMENT_LEFT_MENU);
        return fragment;

    }

    /**
     * @return 主页对象
     */
    public ContentFragment getContentFragment() {
        FragmentManager cm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) cm.findFragmentByTag(FRAGMENT_CONTENT);
        return fragment;

    }
}
