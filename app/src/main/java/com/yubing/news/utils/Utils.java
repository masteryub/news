package com.yubing.news.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu on 2017/3/6.
 */

public class Utils {
    public static List<Integer> windowXY(Context cxt){
        List<Integer> list= new ArrayList<>();
        WindowManager wm= (WindowManager) cxt.getSystemService(Context.WINDOW_SERVICE);
        int whdth=wm.getDefaultDisplay().getWidth();
        int heigth= wm.getDefaultDisplay().getHeight();
        list.add(whdth);
        list.get(heigth);
        return list;
    }
}
