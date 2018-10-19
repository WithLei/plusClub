package com.android.renly.plusclub.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.R;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.server_version)
    TextView serverVersion;
    @BindView(R.id.tv_about)
    TextView tvAbout;

    private Unbinder unbinder;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initSlidr();
        String text = "###开发组介绍：  \n" +
                "Android开发：[@Renly](https://github.com/WithLei) [@小时的风](https://github.com/xiaoshidefeng)  \n" +
                "Web开发&后台开发：[@Robinson](https://github.com/Robinson28years)  \n" +
                "###Bug反馈  \n" +
                "功能不断完善中，bug较多还请多多反馈......  \n" +
                "1.加入QQ交流群：113500631  \n" +
                "2.Github提交 [点击这儿](https://github.com/WithLei/plusClub)  \n" +
                "3.在 **论坛反馈** 模块进行反馈" ;
        RichText.fromMarkdown(text).into(tvAbout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_back, R.id.server_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finishActivity();
                break;
            case R.id.server_version:
                // 检测更新
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
