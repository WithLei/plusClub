package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.api.bean.Store;
import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.utils.toast.MyToast;
import com.android.renly.plusclub.utils.NetConfig;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class SignActivity extends BaseActivity {
    @BindView(R.id.myToolBar)
    FrameLayout myToolBar;
    @BindView(R.id.et_sign_email)
    TextInputEditText etSignEmail;
    @BindView(R.id.et_sign_name)
    TextInputEditText etSignName;
    @BindView(R.id.et_sign_stuid)
    TextInputEditText etSignStuid;
    @BindView(R.id.et_sign_course)
    TextInputEditText etSignCourse;
    @BindView(R.id.et_sign_phone)
    TextInputEditText etSignPhone;
    @BindView(R.id.et_sign_pwd)
    TextInputEditText etSignPwd;
    @BindView(R.id.et_sign_repwd)
    TextInputEditText etSignRepwd;
    @BindView(R.id.iv_toolbar_menu)
    ImageView ivToolbarMenu;
    @BindView(R.id.til_sign_pwd)
    TextInputLayout tilSignPwd;
    @BindView(R.id.til_sign_repwd)
    TextInputLayout tilSignRepwd;
    private Unbinder unbinder;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        initToolBar(true, "用户注册");
        addToolbarMenu(R.drawable.ic_check_black_24dp).setOnClickListener(view -> submit());
        setSubmitBtn(false);
        addTextWatcher();
        initSlidr();
    }

    /**
     * 监控输入信息是否正确
     */
    TextWatcher watcher = new TextWatcher() {
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
    };

    private void addTextWatcher() {
        etSignEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                EmailCheck();
            }
        });

        etSignName.addTextChangedListener(watcher);
        etSignStuid.addTextChangedListener(watcher);
        etSignCourse.addTextChangedListener(watcher);
        etSignPhone.addTextChangedListener(watcher);
        etSignPwd.addTextChangedListener(watcher);
        etSignRepwd.addTextChangedListener(watcher);

    }

    private void setSubmitBtn(boolean setEnable) {
        if (setEnable) {
            ivToolbarMenu.setClickable(true);
            ivToolbarMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp));
        } else {
            ivToolbarMenu.setClickable(false);
            ivToolbarMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_gray_24dp));
        }
    }

    private void checkInput() {
        String name = etSignName.getText().toString();
        String stuid = etSignStuid.getText().toString();
        String course = etSignCourse.getText().toString();
        String phone = etSignPhone.getText().toString();
        String newPwd = etSignPwd.getText().toString();
        String newPwd2 = etSignRepwd.getText().toString();
        if (TextUtils.isEmpty(name.trim())) {
            etSignName.setError("姓名不能为空");
            setSubmitBtn(false);
            return;
        }
        if (TextUtils.isEmpty(stuid.trim())) {
            etSignStuid.setError("学号不能为空");
            setSubmitBtn(false);
            return;
        }
        if (TextUtils.isEmpty(course.trim())) {
            etSignCourse.setError("班级不能为空");
            setSubmitBtn(false);
            return;
        }
        if (TextUtils.isEmpty(phone.trim())) {
            etSignPhone.setError("联系方式不能为空");
            setSubmitBtn(false);
            return;
        }
        if (TextUtils.isEmpty(newPwd.trim())) {
            tilSignPwd.setError("密码不能为空");
            setSubmitBtn(false);
            return;
        }

        if (newPwd.length() < 6) {
            tilSignPwd.setError("密码太短");
            setSubmitBtn(false);
        } else if (!StringUtils.checkSecurity(newPwd)) {
            tilSignPwd.setError("密码中必须包含数字、字母");
            setSubmitBtn(false);
        } else if (!Objects.equals(newPwd, newPwd2)) {
            tilSignPwd.setError(null);
            tilSignRepwd.setError("两次输入的密码不一致");
            setSubmitBtn(false);
        } else {
            tilSignRepwd.setError(null);
            setSubmitBtn(true);
        }
    }

    private void EmailCheck() {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(etSignEmail.getText().toString());
            flag = matcher.matches();
        } catch (Exception e) {
            etSignEmail.setError("请输入正确的邮箱地址");
        }
        if (!flag)
            etSignEmail.setError("请输入正确的邮箱地址");
    }

    /**
     * 提交注册申请
     */
    private void submit() {
        OkHttpUtils.post()
                .url(NetConfig.BASE_REGISTER_PLUS)
                .addParams("email",etSignEmail.getText().toString().trim())
                .addParams("name",etSignName.getText().toString().trim())
                .addParams("studentid",etSignStuid.getText().toString().trim())
                .addParams("grades",etSignCourse.getText().toString().trim())
                .addParams("phone",etSignPhone.getText().toString().trim())
                .addParams("password",etSignPwd.getText().toString().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("submit onError");
                        ToastShort("注册失败，邮箱已注册");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj = JSON.parseObject(response);
                        if (!response.contains("code")){
                            ToastNetWorkError();
                        }else if (obj.getInteger("code") == 20000){
                            Store.getInstance().setToken(obj.getString("result"));
                            MyToast.showText(SignActivity.this,"注册成功",true);
                            hideKeyBoard();
                            finishActivity();
                        }
                    }
                });
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
