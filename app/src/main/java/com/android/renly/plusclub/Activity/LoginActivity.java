package com.android.renly.plusclub.Activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Api.RetrofitService;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.R;

import java.io.IOException;

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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            btnLoginSetEnabled();
        }
    };
    private Unbinder unbinder;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }
    private static final int SHOW_SOFTINPUT = 16;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_SOFTINPUT:
                    showSoftInput();
                    break;
            }
        }
    };


    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        initToolBar(true,"登陆账号");
        initSlidr();
        initText();

        btnLoginSetEnabled();
        handler.sendEmptyMessageDelayed(SHOW_SOFTINPUT, 1000);
    }

    private void initText() {
        if (App.isRemeberPwdUser(this)) {
            etLoginName.setText(App.getEmail(this));
            etLoginPas.setText(App.getPwd(this));
            cbRemUser.setChecked(true);
        } else{
            etLoginName.setText(App.getEmail(this));
            cbRemUser.setChecked(false);
        }
        etLoginName.setSelection(etLoginName.getText().length());
        etLoginPas.setSelection(etLoginPas.getText().length());

        etLoginName.addTextChangedListener(textWatcher);

        etLoginPas.addTextChangedListener(textWatcher);
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

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forgetPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String email = etLoginName.getText().toString().trim();
                String pwd = etLoginPas.getText().toString().trim();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd))
                    doLogin(email, pwd);
                break;
            case R.id.tv_register:
                gotoActivity(SignActivity.class);
                break;
            case R.id.tv_forgetPwd:
                gotoActivity(FindpwdActivity.class);
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void doLogin(String email, String pwd) {
        RetrofitService.doLogin(email, pwd)
                .subscribe(responseBody -> {
                    JSONObject obj = null;
                    try {
                        obj = JSON.parseObject(responseBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int statusCode = obj.getInteger("code");
                    String token = obj.getString("result");
                    printLog("code:" + statusCode + " result:" + token);

                    if (statusCode == 20000){
                        afterLoginSuccess(email, pwd, token);
                    }else{
                        afterLoginFail();
                    }
                });
    }

    private void afterLoginFail() {
        MyToast.showText(this,"账号或密码错误", Toast.LENGTH_SHORT,false);
    }

    private void afterLoginSuccess(String email, String pwd, String token) {
        SharedPreferences sp = getSharedPreferences(App.MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(App.USER_EMAIL_KEY, email);
        editor.putString(App.USER_PWD_KEY, pwd);
        editor.putString(App.USER_TOKEN_KEY, token);
        editor.putBoolean(App.IS_REMEBER_PWD_USER,cbRemUser.isChecked());
        editor.putBoolean(App.IS_LOGIN, true);
        editor.apply();
        MyToast.showText(this,"登录成功", Toast.LENGTH_SHORT,true);
        printLog("登录成功");

        getUserInfo();
    }

    /**
     * 登陆成功后获取用户信息
     */
    @SuppressLint("CheckResult")
    private void getUserInfo() {
        RetrofitService.getUserDetails()
                .subscribe(responseBody -> {
                    JSONObject jsonObject = JSON.parseObject(responseBody.string());
                    if (jsonObject.getInteger("code") != 20000){
                        ToastShort("服务器出状况惹，再试试( • ̀ω•́ )✧");
                    }else{
                        afterGetUserInfo(jsonObject.getString("result"));
                    }
                });
    }

    private void afterGetUserInfo(String jsonObj){
        JSONObject obj = JSON.parseObject(jsonObj);
        SharedPreferences sp = getSharedPreferences(App.MY_SP_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(App.USER_UID_KEY, obj.getLong("id"));
        editor.putString(App.USER_NAME_KEY, obj.getString("name"));
        editor.putString(App.USER_ROLE_KEY, obj.getString("role"));
        editor.apply();

        setResult(RESULT_OK);
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        handler.removeCallbacksAndMessages(null);
    }

}
