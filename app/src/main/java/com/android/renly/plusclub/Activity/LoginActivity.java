package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.tv_login)
    TextView tvLogin;

    private Unbinder unbinder;
    private List<Cookie>cookies;

    private String url = NetConfig.MY_URL + "16103220233";
    private String Cookie = "Hm_lvt_e02b6048e478dd6d580d748a0884e3d7=1528951645; ASP.NET_SessionId=etcxg0551xnv3i45r1jqoamp; jwgl=20111125";

    @Override
    public void initData() {
        //Hm_lvt_e02b6048e478dd6d580d748a0884e3d7=1528951645; ASP.NET_SessionId=etcxg0551xnv3i45r1jqoamp; jwgl=20111125
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        initParams(params);
        initHeaders(client);
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode != 200){
                    Log.e("print","Fail! statusCode != 200");
                    return;
                }
                String response = null;
                try {
                    response = new String(responseBody,"GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.e("print","Success!" + response);
                writeData(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("print","Fail!");
            }
        });
    }

    private void writeData(String str) {
        try {
            File file = new File("/sdcard/Test/output2.txt");
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

    private void initHeaders(AsyncHttpClient client) {
        client.addHeader("Accept","text/html, application/xhtml+xml, image/jxr, */*");
        client.addHeader("Accept-Encoding","gzip, deflate");
        client.addHeader("Accept-Language","zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3");
        client.addHeader("Connection","Keep-Alive");
        client.addHeader("Cookie",Cookie);
        client.addHeader("Host","jwgl.webvpn.lsu.edu.cn");
        client.addHeader("Referer",url);
        client.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");

    }

    private void initParams(RequestParams params) {
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
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
