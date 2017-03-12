package com.yubing.news.base;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SearchViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import com.yubing.news.R;
import com.yubing.news.activity.NewsDetailActivity;
import com.yubing.news.bean.NewsData;
import com.yubing.news.bean.TabData;
import com.yubing.news.global.GlobalContants;
import com.yubing.news.utils.CacheUtils;
import com.yubing.news.utils.PrefUtils;
import com.yubing.news.utils.Utils;
import com.yubing.news.view.RefreshListView;

import java.util.ArrayList;

/**
 * 页签详情页
 */
public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    NewsData.NewsTabData mTabData;
    private String mUrl;
    private TabData mTabData1;
    @ViewInject(R.id.vp_news)
    private ViewPager mViewPager;
   @ViewInject(R.id.tv_newstitle)
    private TextView mTextView;//头条新闻的标题

    //头条新闻数据集合
    private ArrayList<TabData.TopNewsData> mTopnews;
    private ArrayList<TabData.TabNewsData> mNews;

    @ViewInject(R.id.lv_list)
    private RefreshListView lvList;// 新闻列表
    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;// 头条新闻位置指示器
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;

    private Handler mHandler;
    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalContants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initViews() {
        /*tvText = new TextView(mActivity);
        tvText.setText("页签详情页");
		tvText.setTextColor(Color.RED);
		tvText.setTextSize(25);
		tvText.setGravity(Gravity.CENTER);*/
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        View headerview = View.inflate(mActivity, R.layout.list_header_topnews, null);

        ViewUtils.inject(this, view);
        ViewUtils.inject(this, headerview);
        lvList.addHeaderView(headerview);
        mViewPager.setOnPageChangeListener(this);

        lvList.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
            if(mMoreUrl!= null){
                getMoreFromServer();
            }else{
                Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT).show();

               lvList.onRefreshComplete(false);//收起脚布局加载更多的布局
            }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int position, long l) {
                // 35311,34221,34234,34342
                // 在本地记录已读状态
                String ids = PrefUtils.getString(mActivity, "read_ids", "");
                String readId = mNews.get(position).id;
                if (!ids.contains(readId)) {
                    ids = ids + readId + ",";
                    PrefUtils.setString(mActivity, "read_ids", ids);
                }
                //不要qui充绘所有的Item所以不要用更新去做
               // mNewsAdapter.notifyDataSetChanged();
               changeReadState(view1);// 实现局部界面刷新, 这个view就是被点击的item布局对象

                // 跳转新闻详情页
                Intent intent = new Intent();

                intent.setClass(mActivity, NewsDetailActivity.class);

                intent.putExtra("url", mNews.get(position).url);
                mActivity.startActivity(intent);
            }
        });

        return view;
    }
    /**
     * 改变已读新闻的颜色
     */
    private void changeReadState(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextColor(Color.GRAY);
    }
    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if(!TextUtils.isEmpty(cache)){
            parseData(cache,false);
        }
        getDataFromServer();
    }

    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //System.out.println(result);
                parseData(result,false);
                lvList.onRefreshComplete(true);

                CacheUtils.setCache(mUrl,result,mActivity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                lvList.onRefreshComplete(false);
            }
        });
    }

    public void getMoreFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //System.out.println(result);
                parseData(result,true);
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                lvList.onRefreshComplete(false);
            }
        });
    }

    private void parseData(String result,boolean isMore) {
        Gson gson = new Gson();
        mTabData1 = gson.fromJson(result, TabData.class);
        //System.out.println(mTabData1);
        String more=mTabData1.data.more;
        if(!TextUtils.isEmpty(more)){
            mMoreUrl = GlobalContants.SERVER_URL+more;
        }else{
            mMoreUrl=null;
        }

        if(!isMore){
            mTopnews = mTabData1.data.topnews;

            mNews = mTabData1.data.news;

            if(mTopnews!=null){
                mViewPager.setAdapter(new TopNewsAdapter());
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);//支持快照
                //让指示器重新定位
                mIndicator.onPageSelected(0);
                mTextView.setText(mTopnews.get(0).title);
            }
            if(mNews!=null){
                mNewsAdapter = new NewsAdapter();

                lvList.setAdapter(mNewsAdapter);
            }
             //自动轮播实现
            if(mHandler==null){
                mHandler= new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        int currntItem= mViewPager.getCurrentItem();
                        if(currntItem<mTopnews.size()-1){
                            currntItem++;
                        }else{
                            currntItem=0;
                        }

                        mViewPager.setCurrentItem(currntItem);
                        //延时3秒再发消息如果不再发消息就会只走一次这样会无线循环
                        mHandler.sendEmptyMessageDelayed(0,3000);
                    }
                };
                //延时三秒发消息
                mHandler.sendEmptyMessageDelayed(0,3000);
            }
        }else{
            ArrayList<TabData.TabNewsData> news= mTabData1.data.news;
                mNews.addAll(news);
                mNewsAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabData.TopNewsData topNewsData = mTopnews.get(position);
        mTextView.setText(topNewsData.title);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 新闻列表适配器
     */
    class NewsAdapter extends BaseAdapter {

        private BitmapUtils utils;

        public NewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mNews.size();
        }

        @Override
        public Object getItem(int i) {
            return mNews.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup group) {
            ViewHolder holder;
            if(view==null){
                view=View.inflate(mActivity,R.layout.list_news_item,null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) view
                        .findViewById(R.id.iv_pic);
                holder.tvTitle = (TextView) view
                        .findViewById(R.id.tv_title);
                holder.tvDate = (TextView) view
                        .findViewById(R.id.tv_date);

                view.setTag(holder);
            }else{
                holder= (ViewHolder) view.getTag();
            }
            TabData.TabNewsData item = (TabData.TabNewsData) getItem(i);

            holder.tvTitle.setText(item.title);
            holder.tvDate.setText(item.pubdate);
            //System.out.println(item.url+"........");
           utils.display(holder.ivPic, item.listimage);

            String ids=PrefUtils.getString(mActivity,"read_ids","");
            if(ids.contains(((TabData.TabNewsData) getItem(i)).id)){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else{
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            return view;
        }
    }
    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivPic;
    }

    /**
     * 头条新闻适配器
     */
    class TopNewsAdapter extends PagerAdapter {
        private  BitmapUtils mUtils;
        public TopNewsAdapter() {
            mUtils = new BitmapUtils(mActivity);
            //默认填充图片
            mUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            if (mTabData1 != null) {
                return mTabData1.data.topnews.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(mActivity);
          //  image.setImageResource(R.drawable.topnews_item_default);
            //基于对象大小填充图片
            image.setScaleType(ScaleType.FIT_XY);
            TabData.TopNewsData topNewsData= mTopnews.get(position);

            //传递ImageView对象地址
            mUtils.display(image,topNewsData.topimage);
            container.addView(image);

            image.setOnTouchListener(new TopNewsTouchListner());
            return image;
        }
class TopNewsTouchListner implements View.OnTouchListener{

    /**
     * 头条新闻的触摸监听
     * @param view
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //删除Handler里面的所有消息和Runnable回调
                mHandler.removeCallbacksAndMessages(null);
                break;
            //按住之后取消了按久了事件就会被消耗掉
            //抬起事件就不会响应了
            case MotionEvent.ACTION_CANCEL:
                mHandler.sendEmptyMessageDelayed(0,3000);
                break;
            case MotionEvent.ACTION_UP:
                mHandler.sendEmptyMessageDelayed(0,3000);
                break;
            default :
            break;
        }

        return false;
    }
}

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
