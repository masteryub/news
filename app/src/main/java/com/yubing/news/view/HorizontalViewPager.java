package com.yubing.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 *11个子页面的事件拦截
 * Created by yu on 2017/3/9.
 */

public class HorizontalViewPager extends ViewPager {
    public HorizontalViewPager(Context context) {
        super(context);
    }

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //让父控件永远不拦截
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //事件分发   用父控件去请求父控件及祖宗控件不要拦截事件
        if(getCurrentItem()!=0){
            getParent().requestDisallowInterceptTouchEvent(true);
        }else{
            getParent().requestDisallowInterceptTouchEvent(false);
        }


        return super.dispatchTouchEvent(ev);
    }
}
