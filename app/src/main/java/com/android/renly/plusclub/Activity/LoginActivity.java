package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.myToolBar)
    FrameLayout myToolBar;
    @BindView(R.id.et_login_name)
    TextInputEditText etLoginName;
    @BindView(R.id.et_login_pas)
    TextInputEditText etLoginPas;
    @BindView(R.id.cb_rem_user)
    CheckBox cbRemUser;
    @BindView(R.id.btn_login)
    Button btnLogin;
    public static final int requestCode = 64;
    private Unbinder unbinder;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        if (App.isRemeberPwdUser(this)) {
            etLoginName.setText(App.getUid(this));
            etLoginPas.setText(App.getPwd(this));
            cbRemUser.setChecked(true);
        } else{
            etLoginName.setText(App.getUid(this));
            cbRemUser.setChecked(false);
        }

        btnLoginSetEnabled();

        etLoginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnLoginSetEnabled();
            }
        });

        etLoginPas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnLoginSetEnabled();
            }
        });
    }

    /**
     * 设置判断按钮是否可以点击
     */
    private void btnLoginSetEnabled() {
        if (!TextUtils.isEmpty(etLoginName.getText().toString().trim())
                && !TextUtils.isEmpty(etLoginPas.getText().toString().trim()))
            btnLogin.setEnabled(true);
        else
            btnLogin.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_toolbar_back, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.btn_login:
                String Uid = etLoginName.getText().toString().trim();
                String pwd = etLoginPas.getText().toString().trim();
                printLog("Uid" + Uid + " pwd" + pwd);
                if (!TextUtils.isEmpty(Uid) && !TextUtils.isEmpty(pwd))
                    doLogin(Uid, pwd);
                break;
        }
    }

    private void doLogin(String uid, String pwd) {
        App.setUid(this, uid);
        App.setPwd(this, pwd);
        if (cbRemUser.isChecked())
            App.setRemeberPwdUser(this, true);
        else
            App.setRemeberPwdUser(this, false);
        App.setIsLogin(this);
        MyToast.showText(this,"登录成功", Toast.LENGTH_SHORT,true);
        printLog("登录成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
