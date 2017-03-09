package com.yubing.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yu on 2017/3/9.
 */

public class TopNewsViewPager extends ViewPager {

    private int mStartX1;
    private int mStartY1;

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopNewsViewPager(Context context) {
        super(context);
    }

    //让父控件永远不拦截
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //事件分发   用父控件去请求父控件及祖宗控件不要拦截事件

        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //不拦截这样是为了保证 ACTION_MOVE 能调用
                getParent().requestDisallowInterceptTouchEvent(true);
                mStartX1 = (int) ev.getRawX();
                mStartY1 = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();
               //左右滑动
                if (Math.abs(endX - mStartX1) > Math.abs(endY - mStartY1)) {//左右滑
                    if (endX > mStartX1) {//右滑
                        if (getCurrentItem() == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);//拦截
                        }
                    } else {//左滑
                        if (getCurrentItem() == getAdapter().getCount() - 1) {//最后一个页面需要拦截
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                } else {//上下不拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}
