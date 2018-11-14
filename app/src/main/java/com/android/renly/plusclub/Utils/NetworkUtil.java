package com.android.renly.plusclub.Utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Api.Bean.Store;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.NetConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class NetworkUtil {
    public static void regetToken(Context context){
        OkHttpUtils.post()
                .url(NetConfig.BASE_GETNEWTOKEN_PLUS)
                .addHeader("Authorization","Bearer " + Store.getInstance().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("print","HomeFragment getNewToken onError");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getInteger("code") != 20000){
                            Log.e("print","HomeFragment getNewToken() onResponse获取Token失败,重新登陆");
                        }else{
                            Store.getInstance().setToken(obj.getString("result"));
                        }
                    }
                });
    }
}
