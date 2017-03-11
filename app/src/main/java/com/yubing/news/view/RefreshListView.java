package com.yubing.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yubing.news.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yu on 2017/3/9.
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
    private static final int STATE_REFRESHING = 2;// 正在刷新

    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pbProgress;

    private RotateAnimation animUp;
    private RotateAnimation animDown;

    private View mHeaderView;
    private int mMHeaderViewHeight;
    private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态
    private int mStartY=-1;

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);


        mHeaderView.measure(0, 0);
        mMHeaderViewHeight = mHeaderView.getMeasuredHeight();

        mHeaderView.setPadding(0, -mMHeaderViewHeight, 0, 0);//隐藏头布局


        initArrowAnim();
        tvTime.setText("最后刷新时间:" + getCurrentTime());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mStartY = (int) ev.getRawY();
            break;
            case MotionEvent.ACTION_MOVE:

                //确保mStartY有值
            if(mStartY==-1){
               mStartY= (int) ev.getRawY();
            }

                if (mCurrrentState == STATE_REFRESHING) {// 正在刷新时不做处理
                    break;
                }
                int endY= (int) ev.getRawY();
                int dy= endY-mStartY;//移动偏移量
                //只有下拉到第一个Item时才语序下拉


               if(dy>0&&getFirstVisiblePosition()==0){
                   int padding =dy-mMHeaderViewHeight;

                   //设置当前padding
                   mHeaderView.setPadding(0,padding,0,0);

                   if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {// 状态改为松开刷新
                       mCurrrentState = STATE_RELEASE_REFRESH;
                       refreshState();
                   } else if (padding < 0 && mCurrrentState != STATE_PULL_REFRESH) {// 改为下拉刷新状态
                       mCurrrentState = STATE_PULL_REFRESH;
                       refreshState();
                   }
                   return true;
               }
                break;
            case MotionEvent.ACTION_UP:
               mStartY=-1;//重置
                if (mCurrrentState == STATE_RELEASE_REFRESH) {
                    mCurrrentState = STATE_REFRESHING;// 正在刷新
                    mHeaderView.setPadding(0, 0, 0, 0);// 显示
                    refreshState();
                } else if (mCurrrentState == STATE_PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mMHeaderViewHeight, 0, 0);// 隐藏
                }
                break;
            default :
            break;
        }


        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉控件的布局
     */
    private void refreshState() {
        switch (mCurrrentState) {
            case STATE_PULL_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                ivArrow.clearAnimation();// 必须先清除动画,才能隐藏
                ivArrow.setVisibility(View.INVISIBLE);
                pbProgress.setVisibility(View.VISIBLE);

                if (mListener != null) {
                    mListener.onRefresh();
                }
                break;

            default:
                break;
        }
    }
    /**
     * 初始化箭头动画
     */
    private void initArrowAnim() {
        // 箭头向上动画
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        // 箭头向下动画
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);

    }

    OnRefreshListener mListener;
    private View mFooterView;
    private int mFooterViewHeight;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int i) {
        if (i == SCROLL_STATE_IDLE
                || i == SCROLL_STATE_FLING) {

            if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {// 滑动到最后
                //System.out.println("到底了.....");
                mFooterView.setPadding(0, 0, 0, 0);// 显示
                setSelection(getCount() - 1);// 改变listview显示位置

                isLoadingMore = true;

                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int i, int i1, int i2) {

    }



    public interface OnRefreshListener {
        public void onRefresh();

        public void onLoadMore();// 加载下一页数据
    }
    /*
         * 初始化脚布局
         */
    private void initFooterView() {
        mFooterView = View.inflate(getContext(),
                R.layout.refresh_listview_footer, null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏

        this.setOnScrollListener(this);
    }
    /*
     * 收起下拉刷新的控件
     */
    private boolean isLoadingMore;
    public void onRefreshComplete(boolean success) {
        if (isLoadingMore) {// 正在加载更多...
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);// 隐藏脚布局
            isLoadingMore = false;
        } else {
            mCurrrentState = STATE_PULL_REFRESH;
            tvTitle.setText("下拉刷新");
            ivArrow.setVisibility(View.VISIBLE);
            pbProgress.setVisibility(View.INVISIBLE);

            mHeaderView.setPadding(0, -mMHeaderViewHeight, 0, 0);// 隐藏

            if (success) {
                tvTime.setText("最后刷新时间:" + getCurrentTime());
            }
        }
    }

    /**
     * 获取当前时间
     */
    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    //包装一下
OnItemClickListener mItemClickListener;
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        mItemClickListener=listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parant, View view, int i, long l) {
   if(mItemClickListener!=null){
       mItemClickListener.onItemClick(parant,view,i-getHeaderViewsCount(),l);
   }
    }
}
