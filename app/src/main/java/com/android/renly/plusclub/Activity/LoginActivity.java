package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
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
        if (App.isRemeberPwdUser(this)){
            etLoginName.setText(App.getUid(this));
            etLoginPas.setText(App.getPwd(this));
        }else
            etLoginName.setText(App.getUid(this));

        etLoginName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(etLoginName.getText().toString().trim())
                        && !TextUtils.isEmpty(etLoginPas.getText().toString().trim()))
                    btnLogin.setEnabled(true);
                else
                    btnLogin.setEnabled(false);

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
                if (!TextUtils.isEmpty(etLoginName.getText().toString().trim())
                        && !TextUtils.isEmpty(etLoginPas.getText().toString().trim()))
                    btnLogin.setEnabled(true);
                else
                    btnLogin.setEnabled(false);

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        String Uid = etLoginName.getText().toString().trim();
        String pwd = etLoginPas.getText().toString().trim();
        printLog("Uid" + Uid + " pwd" + pwd);
        if (!TextUtils.isEmpty(Uid) && !TextUtils.isEmpty(pwd))
            doLogin(Uid, pwd);
    }

    private void doLogin(String uid, String pwd) {
        App.setUid(this,uid);
        App.setPwd(this,pwd);
        if (cbRemUser.isChecked())
            App.setRemeberPwdUser(this,true);
        printLog("登录成功");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
