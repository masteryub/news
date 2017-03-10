package com.yubing.news.base;


import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
import com.yubing.news.R;
import com.yubing.news.bean.NewsData;
import com.yubing.news.bean.TabData;
import com.yubing.news.global.GlobalContants;
import com.yubing.news.utils.Utils;

/**
 * 页签详情页
 */
public class TabDetailPager extends BaseMenuDetailPager {

    NewsData.NewsTabData mTabData;
    private String mUrl;
    private TabData mTabData1;
    @ViewInject(R.id.vp_news)
    private ViewPager mViewPager;

    private  BitmapUtils mUtils;

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
        ViewUtils.inject(this, view);

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
        System.out.println(mTabData1);

        mViewPager.setAdapter(new TopNewsAdapter());
    }


    class TopNewsAdapter extends PagerAdapter {
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
            image.setImageResource(R.drawable.topnews_item_default);
            image.setScaleType(ScaleType.FIT_XY);
            TabData.TopNewsData topNewsData= mTabData1.data.topnews.get(position);

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
