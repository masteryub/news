package com.yubing.news.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yubing.news.R;
import com.yubing.news.activity.MainActivity;
import com.yubing.news.base.impl.NewsCenterPager;
import com.yubing.news.bean.NewsData;

import java.util.ArrayList;

/**
 * 侧边栏
 * Created by yu on 2017/3/5.
 */

public class LeftMenuFragment extends BaseFragment {
    @ViewInject(R.id.lv_list)
    private ListView lvList;
    private ArrayList<NewsData.NewsMenuData> mMenuList;
    private int mCurrentpos;//标记谁被点击了
    private MenuAdapter menuAdapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ViewUtils.inject(this, view);


        return view;
    }

    @Override
    public void initData() {
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentpos = i;
                //刷新一下再调一次getview
                menuAdapter.notifyDataSetChanged();

                setCurrentMenuDetailPager(mCurrentpos);

                toggleSlidingMenu();//隐藏

            }
        });
    }

    /**
     *
     * 切换SlidingMenu的状态
     * @param
     */
    private void toggleSlidingMenu() {
   MainActivity mainUi= (MainActivity) mActivity;
        SlidingMenu slidingMenu= mainUi.getSlidingMenu();
        slidingMenu.toggle();//切换状态，显示是隐藏，隐藏时显示

    }

    /**
     * 设置当前菜单详情页
     *
     * @param position
     */
    private void setCurrentMenuDetailPager(int position) {
        MainActivity mainUi = (MainActivity) mActivity;
//获取主页面fragment
        ContentFragment fragment = mainUi.getContentFragment();
        //获取新闻中心页面
        NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
        //设置当前菜单页详情
        newsCenterPager.setCurrentMenuDetailpager(position);

    }


    /**
     * 通过这个方法把要用的数据传过来
     * 设置网络数据
     */
    public void setMenuData(NewsData mNewsData) {
        //System.out.println(mNewsData);
        mMenuList = mNewsData.data;
        menuAdapter = new MenuAdapter();
        lvList.setAdapter(menuAdapter);

    }

    class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mMenuList != null) {
                return mMenuList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return mMenuList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(mActivity, R.layout.list_menu_item, null);
            TextView mTvMlTitle = (TextView) v.findViewById(R.id.tv_lm_title);
            NewsData.NewsMenuData newsMenuData = mMenuList.get(i);

            mTvMlTitle.setText(newsMenuData.title);
            //当前绘制的View是否被选中
            if (mCurrentpos == i) {
                //显示红色
                mTvMlTitle.setTextColor(Color.RED);
            } else {
                //显示白色
                mTvMlTitle.setTextColor(Color.WHITE);
            }
            return v;

        }
    }

}
