package com.android.renly.plusclub;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.UI.DrawableTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.util.EntityUtils;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    @BindView(R.id.logo)
    DrawableTextView logo;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.iv_clean_phone)
    ImageView ivCleanPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.clean_password)
    ImageView cleanPassword;
    @BindView(R.id.iv_show_pwd)
    ImageView ivShowPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.regist)
    TextView regist;
    @BindView(R.id.forget_password)
    TextView forgetPassword;
    @BindView(R.id.body)
    LinearLayout body;
    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.et_check)
    EditText etCheck;
    @BindView(R.id.iv_check)
    ImageView ivCheck;

    private Unbinder unbinder;

    private static final int GET_CHECK_IMAGE = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_CHECK_IMAGE:
                    ivCheck.setImageBitmap(bm);
                    break;
            }
        }
    };

    private List<Cookie> cookies;                      //保存获取的cookie
    private Bitmap bm;

    @Override
    public void initData() {

//        final AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
//        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
//        client.setCookieStore(cookieStore);
//        initParams(params);
////        initHeaders(client);
//        client.post(NetConfig.EDU_URL, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                if (statusCode == 200) {
//                    ToastShort(MainActivity.this, "success!");
//                    try {
//                        String response = new String(responseBody, "GB2312");
//                        Log.e("print", "success " );
//                        writeData(response);
//                        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainActivity.this);
//                        cookies = myCookieStore.getCookies();
//                        Log.e("print", "\ncookies:" + cookies.toString());
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                } else
//                    Log.e("print", "fail");
//
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                ToastShort(MainActivity.this, "fail!");
//                Log.e("print", "fail" + error.toString());
//            }
//        });
    }

    private void writeData(String str) {
        try {
            File file = new File("/sdcard/Test/output.txt");
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:");
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
//            raf.seek(file.length());
            raf.write(str.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    private void initParams(RequestParams params) {
        params.put("txtUserName", "16103220237");
        params.put("TextBox2", "zl11471583210");
        params.put("RadioButtonList1", "%D1%A7%C9%FA");
    }

    private void initHeaders(AsyncHttpClient client) {
        client.addHeader("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
        client.addHeader("Accept-Encoding", "gzip, deflate");
        client.addHeader("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
        client.addHeader("Cache-Control", "no-cache");
        client.addHeader("Connection", "Keep-Alive");
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
        client.addHeader("Host", "jwgl.webvpn.lsu.edu.cn");
        client.addHeader("Referer", "https://jwgl.webvpn.lsu.edu.cn/default2.aspx");
        client.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
    }

    @Override
    public void initView() {
        OkHttpUtils.post()
                .url(NetConfig.CHECKIMG_URL)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("response","fail");
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        Log.e("response","success");
                        bm = response;
                        handler.sendEmptyMessage(GET_CHECK_IMAGE);
                    }
                });

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
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

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        String mobie = etMobile.getText().toString();
        String pwd = etPassword.getText().toString();
        String checkid = etCheck.getText().toString();

        doLogin(mobie,pwd,checkid);
    }

    private void doLogin(String mobie, String pwd, String checkid) {
        OkHttpUtils.post()
                .url(NetConfig.EDU_URL)
                .addParams("txtUserName", "16103220237")
                .addParams("TextBox2", "zl11471583210")
                .addParams("txtSecretCode",checkid)
                .addParams("RadioButtonList1", "%D1%A7%C9%FA")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        Headers headers = response.headers();
                        String cookie = "";
                        for (int i = 0;i < headers.size();i++)
                            if (headers.name(i).equals("Set-Cookie"))
                                cookie += headers.value(i);
                        Log.e("print","cookie " + cookie);
                        //需要对后缀进行处理
                        //从这里开始
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("print", "onError");
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("print", "onResponse");
                    }
                });
    }

}
