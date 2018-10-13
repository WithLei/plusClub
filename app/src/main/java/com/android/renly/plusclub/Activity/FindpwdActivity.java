package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class FindpwdActivity extends BaseActivity {
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
//        OkHttpUtils.post()
//                .url(NetConfig.BASE_RESETPWD_PLUS)
//                .addHeader("Authorization", App.getToken(this))
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//
//                    }
//                });

    }

    private void checkInput() {
        String newPwd = newPass.getText().toString();
        String newPwd2 = newPass2.getText().toString();
        if (TextUtils.isEmpty(newPwd)) {
            newPassC2.setError("新密码不能为空");
            return;
        }

        if (newPwd.length() < 6) {
            newPassC.setError("密码太短");
        } else if (!checkSecurity(newPwd)) {
            newPassC.setError("密码中必须包含数字、字母");
        } else if (!Objects.equals(newPwd, newPwd2)) {
            newPassC2.setError("两次输入的密码不一致");
        } else {
            newPassC2.setError(null);
        }
    }

    /**
     * 检查密码中是否含有数字和字母
     *
     * @param pwd
     * @return
     */
    private boolean checkSecurity(String pwd) {
        String p = "", p2 = "", p3 = "";
        Pattern pa1 = Pattern.compile("\\d+");
        Pattern pa2 = Pattern.compile("[a-z]+");
        Pattern pa3 = Pattern.compile("[A-Z]+");
        Matcher m1 = pa1.matcher(pwd);
        Matcher m2 = pa2.matcher(pwd);
        Matcher m3 = pa3.matcher(pwd);
        if (m1.find()) {
            p = m1.group();
        } else if (m2.find()) {
            p2 = m2.group();
        } else if (m3.find()) {
            p3 = m3.group();
        }

        return !(p.length() == pwd.length() || p2.length() == pwd.length() || p3.length() == pwd.length());
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