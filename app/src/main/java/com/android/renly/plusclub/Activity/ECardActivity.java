package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public class ECardActivity extends BaseActivity {
    @BindView(R.id.tv)
    TextView tv;

    private String lt;
    private String cookie = "";

    private static final int DO_LOGIN = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DO_LOGIN:
                    doLogin();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_ecard;
    }

    @Override
    protected void initData() {
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        doLogin();
    }

    /**
     * 获取 lt param 和 Cookie
     */
    private void getlt() {
        OkHttpUtils.get()
                .url(NetConfig.BASE_ECARD_PLUS)
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeader("Accept-Encoding","gzip, deflate, br")
                .addHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .addHeader("Cache-Control","")
                .addHeader("Connection","keep-alive")
                .addHeader("Cookie","")
                .addHeader("Host","ca.webvpn.lsu.edu.cn")
                .addHeader("Referer","https://ca.webvpn.lsu.edu.cn/zfca/logout")
                .addHeader("Upgrade-Insecure-Requests","1")
                .addHeader("User-Agent",NetConfig.User_Agent_KEY)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String responseHTML = new String(response.body().bytes(), "GB2312");
                        lt = responseHTML.split("<input type=\"hidden\" name=\"lt\" value=\"")[1];
                        lt = lt.split("\" />")[0];
                        printLog("lt = " + lt);

                        Headers headers = response.headers();
                        for (int i = 0; i < headers.size(); i++){
                            if (headers.name(i).equals("Set-Cookie"))
                                if (headers.value(i).contains("; Path=/")){
                                    printLog("full cookie = " + headers.value(i));
                                    cookie += headers.value(i).substring(0, headers.value(i).indexOf("; Path=/"));
                                }
                                else
                                    cookie += headers.value(i);
                        }
                        printLog("cookie = " + cookie);
                        handler.sendEmptyMessage(DO_LOGIN);
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("getlt onError");
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        printLog("getlt onResponse");
                    }
                });
    }

    /**
     * 模拟发送登陆请求
     */
    private void doLogin() {
        if (lt == null){
            getlt();
            return;
        }
        printLog(cookie + " " + cookie.substring(cookie.indexOf("=")+1,cookie.length()) +" " + App.getEduid(this) + " " + App.getEduPwd(this));
        OkHttpUtils.post()
                .url(NetConfig.ECARD_LOGIN_PLUS)
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .addHeader("Accept-Encoding","gzip, deflate, br")
                .addHeader("Accept-Language","en,zh-CN;q=0.9,zh;q=0.8,en-GB;q=0.7")
                .addHeader("Cache-Control","max-age=0")
                .addHeader("Origin","https://ca.webvpn.lsu.edu.cn")
                .addHeader("Upgrade-Insecure-Requests","1")
                .addHeader("Connection","keep-alive")
                .addHeader("Content-Length","199")
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .addHeader("Cookie",cookie)
                .addHeader("Host","ca.webvpn.lsu.edu.cn")
                .addHeader("Referer","https://ca.webvpn.lsu.edu.cn/zfca/login")
                .addHeader("Upgrade-Insecure-Requests","1")
                .addHeader("User-Agent",NetConfig.User_Agent_KEY)
                .addParams("_eventId","submit")
                .addParams("ip","")
                .addParams("isremenberme","0")
                .addParams("losetime","240")
                .addParams("lt",lt)
                .addParams("password",App.getEduPwd(this))
                .addParams("submit1", " ")
                .addParams("username",App.getEduid(this))
                .addParams("useValidateCode","0")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String responseHTML = new String(response.body().bytes(), "GB2312");
//                        writeData("/sdcard/Test/EcardFirst.txt", responseHTML);
                        updateCookie();
//                        writeData(getFilesDir().getAbsolutePath() + "/output/ECard.txt", responseHTML);
                        new Thread() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    tv.setText(responseHTML);
                                });
                            }
                        }.start();
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("onError " + e.getMessage());

                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        printLog("onResponse");
                    }
                });
    }

    /**
     * 登陆成功后需要更新下Cookie
     */
    private void updateCookie() {
    }

    @Override
    protected void initView() {
        initToolBar(true,"校园卡收支");
        initSlidr();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
