package com.android.renly.plusclub;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_main)
    TextView tvMain;

    private Unbinder unbinder;

    List<Cookie> cookies;                      //保存获取的cookie
    @Override
    public void initData() {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        PersistentCookieStore cookieStore = new PersistentCookieStore(this);
        client.setCookieStore(cookieStore);
//        initParams(params);
//        initHeaders(client);
        client.post(NetConfig.EDU_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    ToastShort(MainActivity.this,"success!");
                    try {
                        String response = new String(responseBody,"GB2312");
                        Log.e("print","success "+ response);
                        writeData(response);
                        PersistentCookieStore myCookieStore = new PersistentCookieStore(MainActivity.this);
                        cookies = myCookieStore.getCookies();
                        Log.e("print","\ncookies:" + cookies.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else
                    Log.e("print","fail" );


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ToastShort(MainActivity.this,"fail!");
                Log.e("print","fail" + error.toString());
            }
        });
    }

    private void writeData(String str) {
        try {
            File file = new File("/sdcard/Test/output.txt");
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" );
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(str.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    private void initParams(RequestParams params) {
        params.put("txtUserName","16103220237");
        params.put("TextBox2","zl11471583210");
        params.put("__VIEWSTATE","dDwtNTE2MjI4MTQ7Oz7pB/NTSIblf9AJanMrSjcqz4d8cA==");
        params.put("RadioButtonList1","%D1%A7%C9%FA");
//        params.put("txtSecretCode","276x");


//        params.put("utf8","✓");
//        params.put("commit","登录 Login");
//        params.put("authenticity_token","hoQl12wpvBuCEBZHNwSwnB1LZflcOo0gsd9kW2J5GQKVwDpmk/AubkrxRgO1f3bpEuPtMvQOM3hF4LEtFge/IA==");
    }

    private void initHeaders(AsyncHttpClient client) {
        client.addHeader("Accept","text/html, application/xhtml+xml, image/jxr, */*");
        client.addHeader("Accept-Encoding","gzip, deflate");
        client.addHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
        client.addHeader("Cache-Control","no-cache");
        client.addHeader("Connection","Keep-Alive");
//        client.addHeader("Content-Length","223");
        client.addHeader("Content-Type","application/x-www-form-urlencoded");
//        client.addHeader("Cookie","_astraeus_session=QnBDR1lYVXh6L0JJSkpVNXFWTFV0cTY4NnYwVlZKSitrdGxZVjZKSXZQQkYzVWFtM1NvK0RvNnZIOXpJME5aUzA2N0JlcTVNb0ZVb2U5cjN3SVkvbnVTbUlPZ2l1ZGhZQ21FaFNORXErZ2p2aDk3Z3pZNEdRRUdFWXhQQ0xhNlZXaEJMRWFHeGRsVy8xcDV0RE9DN0tWNHBMb1ZuRTdxVXVMZmNkNlBDc1lFPS0tTDUwT3gxamd4bndZNE1wZnNwTVZaZz09--f2ba03082014220b669679d5531965db8cf86f06; SERVERID=Server1; webvpn_username=16103220237%7C1535427216%7C985c11871defad7d0b3c93d439b42cd6d0c9f86a");
        client.addHeader("Host","jwgl.webvpn.lsu.edu.cn");
        client.addHeader("Referer","https://jwgl.webvpn.lsu.edu.cn/default2.aspx");
        client.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
    }

    @Override
    public void initView() {

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
}
