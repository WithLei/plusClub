package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.FrameLayout;

import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FindpwdActivity extends BaseActivity {
    @BindView(R.id.myToolBar)
    FrameLayout myToolBar;
    @BindView(R.id.et_findpwd_email)
    TextInputEditText etFindpwdEmail;
    private Unbinder unbinder;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_findpwd;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initToolBar(true, "找回密码");
        initSlidr();
        etFindpwdEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                EmailCheck();
            }
        });
    }

    private void EmailCheck() {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(etFindpwdEmail.getText().toString());
            flag = matcher.matches();
        } catch (Exception e) {
            etFindpwdEmail.setError("请输入正确的邮箱地址");
        }
        if (!flag)
            etFindpwdEmail.setError("请输入正确的邮箱地址");
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
