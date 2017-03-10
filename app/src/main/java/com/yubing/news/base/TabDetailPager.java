package com.yubing.news.base;


import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import com.yubing.news.bean.NewsData;
import com.yubing.news.bean.TabData;
import com.yubing.news.global.GlobalContants;
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
        return view;
    }

    @Override
    public void initData() {

        getDataFromServer();
    }

    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //System.out.println(result);
                parseData(result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void parseData(String result) {
        Gson gson = new Gson();
        mTabData1 = gson.fromJson(result, TabData.class);
        //System.out.println(mTabData1);
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

           utils.display(holder.ivPic, item.listimage);
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
            return image;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
