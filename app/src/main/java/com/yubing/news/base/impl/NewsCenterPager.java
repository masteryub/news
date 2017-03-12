package com.yubing.news.base.impl;

import android.app.Activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yubing.news.activity.MainActivity;
import com.yubing.news.base.BaseMenuDetailPager;
import com.yubing.news.base.BasePager;
import com.yubing.news.base.menudetail.InteractMenuDetailPager;
import com.yubing.news.base.menudetail.NewsMenuDetailPager;
import com.yubing.news.base.menudetail.PhotoMenuDetailPager;
import com.yubing.news.base.menudetail.TopicMenuDetailPager;
import com.yubing.news.bean.NewsData;
import com.yubing.news.fragment.LeftMenuFragment;
import com.yubing.news.global.GlobalContants;
import com.yubing.news.utils.CacheUtils;

import java.util.ArrayList;

/**
 * 新闻Pager
 * Created by yu on 2017/3/7.
 */

public class NewsCenterPager extends BasePager {


    private NewsData mNewsData;
    private ArrayList<BaseMenuDetailPager> mPagers;

    public NewsCenterPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        setSlidingMenuEnable(true);//设置侧边栏是否可以打开
        mTvTilte.setText("新闻");
      /*  TextView text = new TextView(mActivity);
        text.setText("新闻中心");
        text.setTextColor(Color.RED);
        text.setTextSize(24);
        text.setGravity(Gravity.CENTER);
        //向Framenlayout 添加动态布局
        mFlContent.addView(text);*/


        //获取缓存数据
    String cache= CacheUtils.getCache(GlobalContants.CATEGORIES_URL,mActivity);
        //如果为空则取从网络获取
        if(!TextUtils.isEmpty(cache)){
           parseData(cache);
        }
        //不管有没有缓存都从网络上去获取最新数据
        //先
            getDataFromServer();


    }


    /**
     * 从服务器获取数据
     *
     * @return
     */
    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        //new RequestCallBack<String>()
        //以为返回的数据为JSON可以算是一个大的字符串所以传String
        //   如果是下载文件就传File对象
        utils.send(HttpMethod.GET, GlobalContants.CATEGORIES_URL, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                //System.out.println(result);
                parseData(result);
                //设置缓存
              CacheUtils.setCache(GlobalContants.CATEGORIES_URL,result,mActivity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }


    /**
     * 解析数据
     *
     * @param result
     */
    private void parseData(String result) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsData.class);
        //System.out.println(mNewsData);
        //数据的传递  刷新侧边栏的数据
        MainActivity maiUi = (MainActivity) mActivity;

        LeftMenuFragment fragment = maiUi.getLeftMenuFragment();
        fragment.setMenuData(mNewsData);

        //准备4个菜单详情页
        ArrayList<NewsData.NewsTabData> children = mNewsData.data.get(0).children;
        mPagers = new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new NewsMenuDetailPager(mActivity, children));
        mPagers.add(new TopicMenuDetailPager(mActivity));
        mPagers.add(new PhotoMenuDetailPager(mActivity,mBtnPhoto));
        mPagers.add(new InteractMenuDetailPager(mActivity));
        //设置菜单详情页-新闻为默认当前页
        setCurrentMenuDetailpager(0);
    }

    /**
     * 设置当前菜单详情页
     */
    public void setCurrentMenuDetailpager(int position) {
        //获取当前要显示的菜单详情页
        BaseMenuDetailPager pager = mPagers.get(position);
        //删除之前的布局
        //如果不删除会不断的叠加在上一个页面上
        mFlContent.removeAllViews();
        //将布局文件设置给FramenLayout
        mFlContent.addView(pager.mRootView);
        //设置当前页的标题
        NewsData.NewsMenuData newsMenuData = mNewsData.data.get(position);
        mTvTilte.setText(newsMenuData.title);

      //初始化当前页面的数据
        pager.initData();
        if(pager instanceof PhotoMenuDetailPager){
       mBtnPhoto.setVisibility(View.VISIBLE);
        }else{
            mBtnPhoto.setVisibility(View.GONE);
        }

    }


}
