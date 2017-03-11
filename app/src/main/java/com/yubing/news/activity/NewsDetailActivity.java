package com.yubing.news.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.yubing.news.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by yu on 2017/3/11.
 */

public class NewsDetailActivity extends AppCompatActivity {

    @InjectView(R.id.btn_back)
    ImageButton mBtnBack;
    @InjectView(R.id.btn_share)
    ImageButton mBtnShare;
    @InjectView(R.id.btn_size)
    ImageButton mBtnSize;
    @InjectView(R.id.wv_web)
    WebView mWebView;
    @InjectView(R.id.pb_progress)
    ProgressBar mPbProgress;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {

        String url= getIntent().getStringExtra("url");
    }

    @OnClick({R.id.btn_back, R.id.btn_share, R.id.btn_size})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                break;
            case R.id.btn_share:
                break;
            case R.id.btn_size:
                break;
        }
    }
}
