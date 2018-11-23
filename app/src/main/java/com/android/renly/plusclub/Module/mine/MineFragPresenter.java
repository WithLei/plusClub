package com.android.renly.plusclub.Module.mine;

import android.annotation.SuppressLint;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Api.Bean.Store;
import com.android.renly.plusclub.Api.RetrofitService;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.Module.base.BasePresenter;
import com.android.renly.plusclub.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MineFragPresenter implements BasePresenter {
    private final MineFragment mView;
    private final int[] icons = new int[]{
//            R.drawable.ic_autorenew_black_24dp,
            R.drawable.ic_palette_black_24dp,
            R.drawable.ic_settings_24dp,
            R.drawable.ic_menu_share_24dp,
            R.drawable.ic_info_24dp,
            R.drawable.ic_favorite_white_12dp,
            R.drawable.ic_lab_24dp,
    };

    private final String[] titles = new String[]{
//            "签到中心",
            "主题设置",
            "设置",
            "分享Plus客户端",
            "关于本程序",
            "热爱开源，感谢分享",
            "实验室功能",
    };

    public MineFragPresenter(MineFragment mView){
        this.mView = mView;
    }

    @Override
    public void getData(boolean isRefresh) {
        if (App.ISLOGIN()){
            getUserAvator();
        }
    }

    protected List<Map<String, Object>> getMenuList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < icons.length; i++) {
            Map<String, Object> ob = new HashMap<>();
            ob.put("icon", icons[i]);
            ob.put("title", titles[i]);
            list.add(ob);
        }
        return list;
    }

    @Override
    public void getMoreData() {

    }


    @SuppressLint("CheckResult")
    private synchronized void getUserAvator() {
        Observable<String> observable = RetrofitService.getNewToken();

        DisposableObserver<String> observer = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Observable.create((ObservableOnSubscribe<String>) emitter -> {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(NetConfig.BASE_USERDETAIL_PLUS)
                            .header("Authorization", "Bearer " + Store.getInstance().getToken())
                            .get()
                            .build();
                    String response = client.newCall(request).execute().body().string();
                    emitter.onNext(response);
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseString -> {
                            if (!responseString.contains("result")) {
                                Log.e("print","MineFragment_getAvatar_subscribe:获取用户信息出错 需要处理");
                                return;
                            }
                            JSONObject jsonObject = JSON.parseObject(responseString);
                            String avatarSrc = "", name = "";
                            JSONObject obj = JSON.parseObject(jsonObject.getString("result"));
                            avatarSrc = obj.getString("avatar");
                            name = obj.getString("name");
                            mView.setInfo(avatarSrc, name);
                        }, throwable -> Log.e("print","MineFragment_getAvatar_subscribe_onError:" + throwable.getMessage()));
            }

            @Override
            public void onError(Throwable e) {
                Log.e("print", "MineFragment_getUserAvator_onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
