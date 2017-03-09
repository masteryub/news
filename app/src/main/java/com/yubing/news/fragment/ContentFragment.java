package com.yubing.news.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yubing.news.R;
import com.yubing.news.base.BasePager;
import com.yubing.news.base.impl.GovAffairsPager;
import com.yubing.news.base.impl.HomePager;
import com.yubing.news.base.impl.NewsCenterPager;
import com.yubing.news.base.impl.SettingPager;
import com.yubing.news.base.impl.SmartPager;

import java.util.ArrayList;

/**
 * 主页内容
 * Created by yu on 2017/3/5.
 */

public class ContentFragment extends BaseFragment {
    @ViewInject(R.id.rg_gruop)
    private RadioGroup mRgGroup;
    
    @ViewInject(R.id.vp_content)
    private ViewPager mViewPager;

    private ArrayList<BasePager> mPagerList;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        //mRgGroup = (RadioGroup) view.findViewById(R.id.rg_gruop);
        //注入View和事件
        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    public void initData() {
        //默然勾选首页
        mRgGroup.check(R.id.rb_home);
     //初始化5个子页面
        mPagerList= new ArrayList<>();
       /* for (int i=0;i<5;i++){
       BasePager pager=new BasePager(mActivity);
            mPagerList.add(pager);
        }*/
        mPagerList.add(new HomePager(mActivity));
        mPagerList.add(new NewsCenterPager(mActivity));
        mPagerList.add(new SmartPager(mActivity));
        mPagerList.add(new GovAffairsPager(mActivity));
        mPagerList.add(new SettingPager(mActivity));



        ContentAdapter mContentAdapter= new ContentAdapter();
        System.out.println(mPagerList);
        mViewPager.setAdapter(mContentAdapter);
        //监听RadioGroup 的选择事件
        mRgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0);//设置为当前页
                    break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1);
                    break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2);
                    break;
                    case R.id.rb_gova:
                        mViewPager.setCurrentItem(3);
                    break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4);
                    break;


                }
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPagerList.get(position).initData();// 获取当前被选中的页面, 初始化该页面数据
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerList.get(0).initData();
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            if(mPagerList!=null){
                return mPagerList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
           // pager.initData();  ViewPager会自动加载左右两边两个页面
            // 的数据所以不能在这里初始化
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取新闻中心页面
     * @return 新闻中心页面
     */
    public NewsCenterPager getNewsCenterPager(){

        return (NewsCenterPager) mPagerList.get(1);
    }


}
