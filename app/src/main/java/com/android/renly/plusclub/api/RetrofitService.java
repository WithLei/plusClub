package com.android.renly.plusclub.api;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.api.api.GithubApi;
import com.android.renly.plusclub.api.api.PlusClubApi;
import com.android.renly.plusclub.api.bean.Store;
import com.android.renly.plusclub.api.bean.Weather;
import com.android.renly.plusclub.api.api.WeatherApi;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.utils.NetConfig;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    private static GithubApi githubApi;

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

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(NetConfig.GITHUB_GET_RELEASE + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        githubApi = retrofit.create(GithubApi.class);

    }


    /**************************************             API             **************************************/

    /**
     * 获取天气状况
     */
    public static Observable<Weather> getWeather(String cityCode) {
        return weatherApi.getWeather(cityCode)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取新的Token
     */
    private static final String ERROR_TOKEN = "error_token";
    private static final String ERROR_RETRY = "error_retry";
    public static Observable<String> getNewToken() {
        return Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
                String requestBody = "";
                Request request = new Request.Builder()
                        .url(NetConfig.BASE_GETNEWTOKEN_PLUS)
                        .header("Authorization", "Bearer " + Store.getInstance().getToken())
                        .post(RequestBody.create(mediaType, requestBody))
                        .build();
                Log.e("print","发起Token请求");
                return Observable.just(client.newCall(request).execute().body().string());
            }
        })
                // Token判断
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) {
                                JSONObject obj = JSON.parseObject(s);
                                if (obj.getInteger("code") != 20000) {
                                    emitter.onError(new Throwable(ERROR_RETRY));
                                } else {
                                    String token = obj.getString("result");
                                    Store.getInstance().setToken(token);
                                    emitter.onNext(token);
                                }
                            }
                        });
                    }
                })
                // flatMap若onError进入retrywhen，否则onNext()
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    private int mRetryCount = 0;

                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if (mRetryCount++ < 3 && throwable.getMessage().equals(ERROR_TOKEN))
                                    return Observable.error(new Throwable(ERROR_RETRY));
                                mRetryCount = 0;
                                return Observable.error(throwable);
                            }
                        });
                    }
                });
    }

    /**
     * 登陆操作
     * @param email
     * @param pwd
     * @return
     */
    public static Observable<ResponseBody> doLogin(String email, String pwd){
        return plusClubApi.doLogin(email, pwd)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取用户个人信息
     */
    public static Observable<ResponseBody> getUserDetails() {
        return plusClubApi.getUserDetails("Bearer " + Store.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取app版本信息
     */
    public static Observable<ResponseBody> getRelease() {
        return githubApi.getVersion(NetConfig.GITHUB_GET_RELEASE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取帖子
     */
    public static Observable<ResponseBody> getPost(long id) {
        return plusClubApi.getPost(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送帖子
     */
    public static Observable<ResponseBody> doPost(String title, String content, String categories) {
        return plusClubApi.doPost("Bearer " + Store.getInstance().getToken(), title, content, categories)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取帖子详情列表，根据activity获取的POSTID从服务器获取[回复对象含User对象]详情
     */
    public static Observable<ResponseBody> getCommentListData(long id) {
        return plusClubApi.getCommentListData(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发表评论
     */
    public static Observable<ResponseBody> doPostComment(String comment, long discussion_id) {
        return plusClubApi.postComment("Bearer " + Store.getInstance().getToken(), comment, discussion_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取回复帖子
     */
    public static Observable<ResponseBody> getReplyPostList(long id, int page) {
        return plusClubApi.getReplyPostList(id, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
