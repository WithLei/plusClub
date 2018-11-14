package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Api.Bean.Store;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.Utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class ChangePwdActivity extends BaseActivity {
    @BindView(R.id.myToolBar)
    FrameLayout myToolBar;
    @BindView(R.id.old_pass)
    EditText oldPass;
    @BindView(R.id.new_pass)
    EditText newPass;
    @BindView(R.id.new_pass2)
    EditText newPass2;
    @BindView(R.id.old_pass_c)
    TextInputLayout oldPassC;
    @BindView(R.id.new_pass_c)
    TextInputLayout newPassC;
    @BindView(R.id.new_pass_c2)
    TextInputLayout newPassC2;
    @BindView(R.id.iv_toolbar_menu)
    ImageView ivToolbarMenu;
    private Unbinder unbinder;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_changepwd;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initToolBar(true, "修改密码");
        addToolbarMenu(R.drawable.ic_check_black_24dp).setOnClickListener(view -> submit());
        initSlidr();
        initEditText();
    }

    private void initEditText() {
        newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkInput();
            }
        });
        newPass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkInput();
            }
        });
    }

    /**
     * 提交操作
     */
    private void submit() {
        OkHttpUtils.post()
                .url(NetConfig.BASE_RESETPWD_PLUS)
                .addHeader("Authorization", "Bearer " + Store.getInstance().getToken())
                .addParams("password", oldPass.getText().toString())
                .addParams("newpassword", newPass2.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastNetWorkError();
                        printLog(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!response.contains("code")) {
                            ToastShort("请检查网络设置ヽ(#`Д´)ﾉ");
                            return;
                        }
                        JSONObject jsonObject = JSON.parseObject(response);
                        printLog(jsonObject.getInteger("code") + "");
                        if (jsonObject.getInteger("code") == 50011) {
                            getNewToken();
                        } else if (jsonObject.getInteger("code") == 50002) {
                            ToastShort("密码输错了，检查一下吧");
                        } else {
                            MyToast.showText(ChangePwdActivity.this, jsonObject.getString("msg"), true);
                            finishActivity();
                        }
                    }
                });

    }

    /**
     * 获取新的Token
     */
    private void getNewToken() {
        OkHttpUtils.post()
                .url(NetConfig.BASE_GETNEWTOKEN_PLUS)
                .addHeader("Authorization", "Bearer " + Store.getInstance().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("ChangePwdAcitivity getNewToken onError" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getInteger("code") != 20000) {
                            printLog("ChangePwdAcitivity getNewToken() onResponse获取Token失败,重新登陆");
                        } else {
                            Store.getInstance().setToken(obj.getString("result"));
                            submit();
                        }
                    }
                });
    }

    private void checkInput() {
        ivToolbarMenu.setClickable(false);
        String newPwd = newPass.getText().toString();
        String newPwd2 = newPass2.getText().toString();
        if (TextUtils.isEmpty(newPwd)) {
            newPassC2.setError("新密码不能为空");
            return;
        }

        if (newPwd.length() < 6) {
            newPassC.setError("密码太短");
        } else if (!StringUtils.checkSecurity(newPwd)) {
            newPassC.setError("密码中必须包含数字、字母");
        } else if (!Objects.equals(newPwd, newPwd2)) {
            newPassC.setError(null);
            newPassC2.setError("两次输入的密码不一致");
        } else {
            newPassC2.setError(null);
            ivToolbarMenu.setClickable(true);
        }
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
