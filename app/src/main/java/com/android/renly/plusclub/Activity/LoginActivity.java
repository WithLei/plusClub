package com.android.renly.plusclub.Activity;

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
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

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

    private static final int LOGIN_SUCCESS = 2;
    private static final int LOGIN_FAIL = 4;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOGIN_SUCCESS:
                    String uid = msg.getData().getString("uid");
                    String pwd = msg.getData().getString("pwd");
                    String token = msg.getData().getString("token");
                    afterLoginSuccess(uid, pwd, token);
                    break;
                case LOGIN_FAIL:
                    afterLoginFail();
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
    }

    private void initText() {
        if (App.isRemeberPwdUser(this)) {
            etLoginName.setText(App.getUid(this));
            etLoginPas.setText(App.getPwd(this));
            cbRemUser.setChecked(true);
        } else{
            etLoginName.setText(App.getUid(this));
            cbRemUser.setChecked(false);
        }
        etLoginName.setSelection(etLoginName.getText().length());
        etLoginPas.setSelection(etLoginPas.getText().length());

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

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forgetPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String Uid = etLoginName.getText().toString().trim();
                String pwd = etLoginPas.getText().toString().trim();
                if (!TextUtils.isEmpty(Uid) && !TextUtils.isEmpty(pwd))
                    doLogin(Uid, pwd);
                break;
            case R.id.tv_register:
                gotoActivity(RegisterActivity.class);
                break;
            case R.id.tv_forgetPwd:
                gotoActivity(FindpwdActivity.class);
                break;
        }
    }

    private void doLogin(String uid, String pwd) {
        OkHttpUtils.post()
                .url(NetConfig.BASE_LOGIN_PLUS)
                .addParams("email",uid)
                .addParams("password",pwd)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("doLogin onError");
                        ToastLong("网络错误请重试，如多次失败来戳我们修复~");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj = JSON.parseObject(response);
                        int statusCode = obj.getInteger("code");
                        String token = obj.getString("result");
                        printLog("code:" + statusCode + " result:" + token);

                        if (statusCode == 20000){
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("uid",uid);
                            bundle.putString("pwd",pwd);
                            bundle.putString("token",token);
                            msg.setData(bundle);
                            msg.what = LOGIN_SUCCESS;
                            handler.sendMessage(msg);
                        }else{
                            handler.sendEmptyMessage(LOGIN_FAIL);
                        }
                    }
                });

    }

    private void afterLoginFail() {
        MyToast.showText(this,"账号或密码错误", Toast.LENGTH_SHORT,false);
    }

    private void afterLoginSuccess(String uid, String pwd, String token) {
        App.setUid(this, uid);
        App.setPwd(this, pwd);
        App.setToken(this,token);
        if (cbRemUser.isChecked())
            App.setRemeberPwdUser(this, true);
        else
            App.setRemeberPwdUser(this, false);
        App.setIsLogin(this);
        MyToast.showText(this,"登录成功", Toast.LENGTH_SHORT,true);
        printLog("登录成功");
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
