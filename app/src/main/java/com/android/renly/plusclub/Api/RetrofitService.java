package com.android.renly.plusclub.Api;

import android.content.Context;

import com.android.renly.plusclub.Api.Api.PlusClubApi;
import com.android.renly.plusclub.Api.Bean.Store;
import com.android.renly.plusclub.Api.Bean.Weather;
import com.android.renly.plusclub.Api.Api.WeatherApi;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.NetConfig;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * '整个网络通信服务的启动控制，必须先调用初始化函数才能正常使用网络通信接口
 */
public class RetrofitService {

    private static PlusClubApi plusClubApi;
    private static WeatherApi weatherApi;

    private RetrofitService() {
        throw new AssertionError();
    }

    /**
     * 初始化网络通信服务
     */
    public static void init() {
        Cache cache = new Cache(new File(App.getContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        OkHttpClient client = new OkHttpClient.Builder().cache(cache)
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(NetConfig.BASE_PLUSCLUB)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        plusClubApi = retrofit.create(PlusClubApi.class);

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(NetConfig.GET_WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        weatherApi = retrofit.create(WeatherApi.class);
    }


    /**************************************             API             **************************************/

    /**
     * 获取天气状况
     */
    public static Observable<Weather> getWeather(String cityCode) {
        return weatherApi.getWeather(cityCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取新的Token
     */
    public static Observable<ResponseBody> getNewToken(){
        return plusClubApi.getNewToken("Bearer " + Store.getInstance().getToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取头像信息
     */
    public static Observable<ResponseBody> getUserAvatar() {
        return plusClubApi.getUserAvatar("Bearer " + Store.getInstance().getToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 登陆操作
     * @param email
     * @param pwd
     * @return
     */
    public static Observable<ResponseBody> doLogin(String email, String pwd){
        return plusClubApi.doLogin(email, pwd)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取用户个人信息
     */
    public static Observable<ResponseBody> getUserDetails() {
        return plusClubApi.getUserDetails("Bearer " + Store.getInstance().getToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
