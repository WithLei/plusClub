package com.android.renly.plusclub.Activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.Listener.MyTextWatcher;
import com.android.renly.plusclub.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.Callback;
import com.zzhoujay.richtext.RichText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public class EduLoginActivity extends BaseActivity {

    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @BindView(R.id.tv_toolbar_title)
    TextView title;
    @BindView(R.id.et_login_name)
    TextInputEditText etMobile;
    @BindView(R.id.et_login_pas)
    TextInputEditText etPassword;
    @BindView(R.id.et_login_check)
    TextInputEditText etCheck;
    @BindView(R.id.iv_check)
    ImageView ivCheck;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_edulogin_tip)
    TextView tvEduloginTip;
    private Unbinder unbinder;

    private String user_eduid = "";
    private String user_edupwd = "";
    private String cookie = "";
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edu_login;
    }

    @Override
    protected void initData() {
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        GetCookies();
        isLogin();
    }

    @Override
    protected void initView() {
        initSlidr();
        initToolBar(true,"教务系统登陆");
        String text = "**更新课表需要重新登陆**\n此模块为爬虫模拟登陆教务系统，如想了解更多可查看[此篇博客](https://blog.csdn.net/qq_42895379/article/details/83098443)。\n如遇到系统错误，请尝试重新登陆。如多次登陆失败，请联系我们：个人 - 关于本程序 - bug反馈";
        RichText.fromMarkdown(text).into(tvEduloginTip);
        GetVerifation();
        btnLoginSetEnabled();
        etMobile.setText(App.getEduid());
        etMobile.setSelection(etMobile.getText().length());
        MyTextWatcher myTextWatcher = new MyTextWatcher() {
            @Override
            public void afterMyTextChanged(Editable editable) {
                btnLoginSetEnabled();
            }
        };
        etMobile.addTextChangedListener(myTextWatcher);
        etPassword.addTextChangedListener(myTextWatcher);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @OnClick({R.id.btn_login, R.id.iv_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String id = etMobile.getText().toString().trim();
                String pwd = etPassword.getText().toString().trim();
                String checkid = etCheck.getText().toString();

                doLogin(id, pwd, checkid);
                break;
            case R.id.iv_check:
                GetVerifation();
                break;
        }
    }

    /**
     * 设置判断按钮是否可以点击
     */
    private void btnLoginSetEnabled() {
        if (!TextUtils.isEmpty(etMobile.getText().toString().trim())
                && !TextUtils.isEmpty(etPassword.getText().toString().trim()))
            btnLogin.setEnabled(true);
        else
            btnLogin.setEnabled(false);
    }

    /**
     * 请求新的Cookie
     */
    private void GetCookies() {
        cookie = "";
        Observable.create((ObservableOnSubscribe<String>) emitter -> OkHttpUtils.get()
                .url(NetConfig.BASE_EDU_PLUS)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        Headers headers = response.headers();
                        for (int i = 0; i < headers.size(); i++) {
                            if (headers.name(i).equals("Set-Cookie"))
                                if (headers.value(i).endsWith(" Path=/"))
                                    cookie += headers.value(i).substring(0, headers.value(i).length() - 7);
                                else
                                    cookie += headers.value(i);
                        }
                        emitter.onNext(response.body().string());
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        emitter.onError(new Throwable("GetCookies onError" + call.toString()));
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("print", "GetCookies onResponse");
                    }
                }))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        GetVIEWSTATE(s);
                        //刷新验证码
                        GetVerifation();
                    }

                    @Override
                    public void onError(Throwable e) {
                        printLog(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    /**
     * 获取__VIEWSTATE
     */
    private void GetVIEWSTATE(String body) {
        String x = body.split("name=\"__VIEWSTATE\" value=\"")[1];
        x = x.split("\" />")[0];
        App.set__VIEWSTATE(x);
    }

    /**
     * 判断是否登录
     */
    private void isLogin() {
        if (sp.getString("id", "").isEmpty())
            return;
        Observable.create((ObservableOnSubscribe<String>) emitter -> OkHttpUtils.post()
                .url(NetConfig.BASE_EDU_HOST_ME)
                .addHeader("Cookie", cookie)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String responseHTML = new String(response.body().bytes(), "GB2312");
                        emitter.onNext(responseHTML);
//                        writeData("/sdcard/Test/isLoginHTML.txt", responseHTML);
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("isLogin() onError");
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        printLog("isLogin() onResponse");
                    }
                }))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        checkLoginSuccess(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 获取验证码图片
     */
    @SuppressLint("CheckResult")
    private void GetVerifation() {
        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> OkHttpUtils.post()
                .url(NetConfig.CHECKIMG_URL_EDU)
                .addHeader("Cookie", cookie)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        emitter.onError(new Throwable("EduLoginActivity GetVerifation onError"));
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        printLog("GetVerifation onResponse");
                        emitter.onNext(response);
                    }
                }))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    ivCheck.setImageBitmap(bitmap);
                    etCheck.setText("");
                }, throwable -> printLog("EduLoginActivity_GetVerifation_onError:" + throwable.getMessage()));

    }

    /**
     * 登录操作
     *
     * @param eduid
     * @param pwd
     * @param checkid
     */
    @SuppressLint("CheckResult")
    private void doLogin(String eduid, String pwd, String checkid) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> OkHttpUtils.post()
                .url(NetConfig.BASE_EDU_PLUS)
                .addParams("__VIEWSTATE", App.get__VIEWSTATE())
                .addParams("Button1", "")
                .addParams("hidPdrs", "")
                .addParams("hidsc", "")
                .addParams("lbLanguage", "")
                .addParams("RadioButtonList1", "%D1%A7%C9%FA")
                .addParams("TextBox2", pwd)
                .addParams("txtSecretCode", checkid)
                .addParams("txtUserName", eduid)
                .addHeader("Cookie", cookie)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String responseHTML = new String(response.body().bytes(), "GB2312");
                        user_eduid = eduid;
                        user_edupwd = pwd;
                        emitter.onNext(responseHTML);
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        emitter.onError(new Throwable("doLogin() onError " + call.toString() + " " + e.getMessage()));
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("print", "doLogin() onResponse");
                    }
                }))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> checkLoginSuccess(s), throwable -> printLog("EduLoginActivity_doLogin_onError:" + throwable.getMessage()));

    }

    /**
     * 检查是否登录成功
     */
    private void checkLoginSuccess(String loginresult) {
        Document doc = Jsoup.parse(loginresult);
        Elements alert = doc.select("script[language]");
        Elements success = doc.select("a[href]");
        Elements err = doc.select("p[class]");
        Elements names = doc.select("#xhxm");

        for (Element link : success) {
            // 获取所要查询的URL,这里相应地址button的名字叫成绩查询
            if (link.text().equals("等级考试查询")) {
                printLog("登陆成功");

                // 登陆成功后获取学生姓名，此处获取到的学生姓名为xx同学
                String stuName = "";
                for (Element name : names)
                    stuName = name.text();
                // 去掉尾缀
                int index = stuName.lastIndexOf("同学");
                if (index >= 0)
                    stuName = stuName.substring(0, stuName.length() - 2);
                printLog("stuName " + stuName);
                AfterSuccessLogin(stuName);
                return;
            }
        }
        for (Element link : alert) {
            //获取错误信息
            if (link.data().contains("验证码不正确")) {
                etCheck.setText("");
                ToastShort("验证码输入错误");
                printLog("验证码输入错误");
                //刷新验证码
                GetVerifation();
                return;
            } else if (link.data().contains("username不能为空")) {
                ToastShort("学号为空");
                printLog("学号为空");
                //刷新验证码
                GetVerifation();
                return;
            } else if (link.data().contains("password错误")) {
                ToastShort("学号或密码错误");
                printLog("学号或密码错误");
                //刷新验证码
                GetVerifation();
                return;
            } else if (link.data().contains("password不能为空")) {
                ToastShort("密码为空");
                printLog("密码为空");
                //刷新验证码
                GetVerifation();
                return;
            }
        }
        for (Element link : err) {
            if (link.text().equals("错误原因：系统正忙！")){
                ToastShort("错误原因：系统正忙！");
                printLog("错误原因：系统正忙！");
                GetCookies();
//                handler.sendEmptyMessage(LOGIN_FAIL_SYSTEMBUSY);
            }
        }
    }

    /**
     * 成功登陆后的操作
     */
    private void AfterSuccessLogin(String stuName) {
        MyToast.showText(this, "登陆成功", Toast.LENGTH_SHORT, true);
        SharedPreferences sp = getSharedPreferences(App.MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(App.COOKIE, cookie);
        editor.putString(App.USER_EDUID_KEY, user_eduid);
        editor.putString(App.USER_EDUPWD_KEY, user_edupwd);
        editor.putString(App.USER_EDUNAME_KEY, stuName);
        editor.apply();
        gotoActivity(EduActivity.class);
        finishActivity();
    }

}
