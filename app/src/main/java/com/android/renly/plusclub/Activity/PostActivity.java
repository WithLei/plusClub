package com.android.renly.plusclub.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Api.RetrofitService;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.Fragment.PostFragment;
import com.android.renly.plusclub.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.ResponseBody;

public class PostActivity extends BaseActivity {
    @BindView(R.id.fragment)
    ScrollView fragment;
    private Unbinder unbinder;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post;
    }

    @Override
    protected void initData() {
        if (getIntent().getExtras().getBoolean("isNormalPost"))
            setIntentData(getIntent().getExtras().getString("PostJsonObject"));
        else
            getPost();
    }

    @Override
    protected void initView() {
        initSlidr();

    }

    @SuppressLint("CheckResult")
    private void getPost() {
        long id = getIntent().getLongExtra("id",0);
        if (id == 0)
            return;
        RetrofitService.getPost(id)
                .subscribe(responseBody -> {
                    String response = responseBody.string();
                    if (!response.contains("code")) {
                        ToastNetWorkError();
                        printLog("getPost onResponse !response.contains(\"code\")");
                        return;
                    }
                    JSONObject dataObj = JSON.parseObject(response);
                    if (dataObj.getInteger("code") != 20000) {
                        ToastShort("服务器出状况惹，稍等喔( • ̀ω•́ )✧");
                    } else {
                        setIntentData(dataObj.getString("data"));
                    }
                }, throwable -> {
                    ToastNetWorkError();
                    printLog("getPost onResponse !response.contains(\"code\")" + throwable.getMessage());
                });
    }

    private void setIntentData(String postJsonObj) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putString("PostJsonObject", postJsonObj);
        bundle.putString("from", "PostActivity");
        postFragment.setArguments(bundle);
        transaction.add(R.id.fragment, postFragment);
        transaction.commit();
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
