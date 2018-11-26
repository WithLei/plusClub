package com.android.renly.plusclub.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebViewActivity extends BaseActivity {
    private Unbinder unbinder;
    @BindView(R.id.webview)
    WebView webView;

    private String title;
    private String url;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initData() {
        getUrl();
        initWebView();
    }

    @Override
    protected void initView() {
        initSlidr();
        initToolBar(true,title);
        webView.loadUrl(url);
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        webView.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        webSettings.setDisplayZoomControls(false);
    }

    private void getUrl() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
