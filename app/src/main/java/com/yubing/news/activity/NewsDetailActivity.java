package com.yubing.news.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.yubing.news.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by yu on 2017/3/11.
 */

public class NewsDetailActivity extends Activity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.inject(this);
        initData();
    }
      private void initData() {

        String url= getIntent().getStringExtra("url");

        WebSettings settings= mWebView.getSettings();
        //表示支持JS
        settings.setJavaScriptEnabled(true);
        //表示显示放大缩小按钮
        //settings.setBuiltInZoomControls(true);
        //支持双击缩放
         settings.setUseWideViewPort(true);

        mWebView.setWebViewClient(new WebViewClient(){
              //在加载的时候调用这个方法
              @Override
              public void onPageStarted(WebView view, String url, Bitmap favicon) {
                  super.onPageStarted(view, url, favicon);
                  mPbProgress.setVisibility(View.VISIBLE);
              }
              //加载结束之后调用
              @Override
              public void onPageFinished(WebView view, String url) {
                  super.onPageFinished(view, url);
                  mPbProgress.setVisibility(View.GONE);
              }
              //所有跳转的链接都会在此方法中回调
              // url就是那些被点击的网页链接
              @Override
              public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                  return super.shouldOverrideUrlLoading(view, request);
              }
          });

        /*  mWebView.setWebChromeClient(new WebChromeClient() {

              *//**
               * 进度发生变化
               *//*
              @Override
              public void onProgressChanged(WebView view, int newProgress) {
                //  System.out.println("加载进度:" + newProgress);
                  super.onProgressChanged(view, newProgress);
              }

              *//**
               * 获取网页标题
               *//*
              @Override
              public void onReceivedTitle(WebView view, String title) {
                 // System.out.println("网页标题:" + title);
                  super.onReceivedTitle(view, title);
              }
          });*/
          mWebView.loadUrl(url);//加载网页
        //mWebView.loadUrl("http://map.baidu.com/");//加载网页


    }

   @OnClick({R.id.btn_back, R.id.btn_share, R.id.btn_size})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            case R.id.btn_share:

                break;
            case R.id.btn_size:
                showChooseDialog();
                break;
        }
    }

    private int mCurrentChooseItem;// 记录当前选中的item, 点击确定前
    private int mCurrentItem = 2;// 记录当前选中的item, 点击确定后

    /**
     * 显示选择对话框
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
                "超小号字体" };
        builder.setTitle("字体设置");
        builder.setSingleChoiceItems(items, mCurrentItem,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mCurrentChooseItem = which;
                    }
                });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = mWebView.getSettings();
                switch (mCurrentChooseItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;

                    default:
                        break;
                }

                mCurrentItem = mCurrentChooseItem;
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }
}
