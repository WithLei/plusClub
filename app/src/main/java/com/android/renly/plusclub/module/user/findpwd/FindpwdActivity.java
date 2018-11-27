package com.android.renly.plusclub.module.user.findpwd;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.FrameLayout;

import com.android.renly.plusclub.listener.MyTextWatcher;
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
        etFindpwdEmail.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterMyTextChanged(Editable editable) {
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
