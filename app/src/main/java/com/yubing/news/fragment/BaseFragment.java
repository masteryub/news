package com.yubing.news.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragement基类
 * Created by yu on 2017/3/5.
 */

public abstract class BaseFragment extends Fragment {


    public Activity mActivity;

    //fragment的创建
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();


    }
  //处理fragment的布局
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return initView();
    }
    //依附的activity创建完成
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 子类必须实现初始化布局的方法
     * @return
     */
    public abstract View initView();

    /**
     * 初始化数据不一定要实现
     */
    public void initData(){

    }
}
