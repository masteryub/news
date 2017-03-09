package com.yubing.news.bean;

import java.util.ArrayList;

/**
 * 网络分类信息的封装
 * 字段名字必须和JSON返回的字段名字一样
 * Created by yu on 2017/3/7.
 */

public class NewsData {
    public int retcode;
    public ArrayList<NewsMenuData> data;

    /**
     * 侧边栏数据对象
     */
    public class NewsMenuData {
        public String id;
        public String title;
        public int type;
        public String url;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }

    }

    /**
     * 新闻页面下11个字页面的数据对象
     */
    public class NewsTabData {
        public String id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }



    @Override
    public String toString() {
        return "NewsData{" +
                "retcode=" + retcode +
                ", data=" + data +
                '}';
    }
}
