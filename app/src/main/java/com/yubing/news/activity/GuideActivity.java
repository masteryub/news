package com.yubing.news.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.yubing.news.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu on 2017/3/5.
 */

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mVpGuide;
    private Button mBtnStart;
    private static final int[] mImageIds=new int[]{R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};
    private List<ImageView> mImageViewList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
        initListener();
    }
    private void initView() {
        mVpGuide = (ViewPager) findViewById(R.id.vp_guide);
        mBtnStart = (Button) findViewById(R.id.btn_start);

    }
    /**
     * 初始化界面准备数据
     */
    private void initData() {
        mImageViewList = new ArrayList<>();
        for(int i=0;i<mImageIds.length;i++){
            ImageView image= new ImageView(this);
            image.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(image);
        }
        GuideAdapter guideAdapter= new GuideAdapter();
        mVpGuide.setAdapter(guideAdapter);
    }
    private void initListener() {
        mBtnStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
    startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            if(mImageViewList!=null){
                return mImageViewList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            //等于才会展示页面
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));

            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
