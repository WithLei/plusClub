package com.android.renly.plusclub.Module.hotnews;

import android.annotation.SuppressLint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Adapter.MyPostAdapter;
import com.android.renly.plusclub.Adapter.PostAdapter;
import com.android.renly.plusclub.Adapter.ReplyAdapter;
import com.android.renly.plusclub.Api.Bean.Post;
import com.android.renly.plusclub.Api.Bean.SimplePost;
import com.android.renly.plusclub.Api.Bean.Store;
import com.android.renly.plusclub.Api.RetrofitService;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.NetConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

import static com.android.renly.plusclub.Adapter.PostAdapter.STATE_LOAD_NOTHING;
import static com.android.renly.plusclub.Utils.ToastUtils.*;

public class HotNewsFragPresenter {
    private HotNewsFragment mView;
    /**
     * 帖子列表[新帖]
     */
    private List<Post> postList;
    /**
     * 回复列表
     */
    private List<SimplePost> replyList;
    /**
     * 我的列表
     */
    private List<SimplePost> myList;
    /**
     * 获取最大帖子页面
     */
    private int max_page_post = 0;
    private int max_page_reply = 0;
    private int max_page_my = 0;

    private PostAdapter postAdapter;
    private ReplyAdapter replyAdapter;
    private MyPostAdapter myPostAdapter;

    private Observer<String> observer;

    public HotNewsFragPresenter(HotNewsFragment mView) {
        this.mView = mView;
    }

    public void getData(boolean isRefresh) {
        initList();
        initObserver();
        getListData(1);
    }

    public void getMoreData() {

    }

    private void getListData(int page) {
        switch (currentType) {
            case TYPE_NEW:
                if (page == 1)
                    postAdapter = null;
                getPostListData(page);
                break;
            case TYPE_REPLY:
                if (page == 1)
                    replyAdapter = null;
                getReplyListData(page);
                break;
            case TYPE_MY:
                if (page == 1)
                    myPostAdapter = null;
                getMyListData(page);
                break;
        }
    }

    private void initObserver() {
        observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(String s) {
                JSONObject obj = JSON.parseObject(s);
                mView.afterGetDataSuccess(obj.getInteger("type"),obj.getString("data"));
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        };
    }

    private void initList() {
        postList = new ArrayList<>();
        replyList = new ArrayList<>();
        myList = new ArrayList<>();
    }

    /**
     * 从服务器获取帖子
     */
    @SuppressLint("CheckResult")
    private void getReplyListData(int page) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> OkHttpUtils.get()
                .url(NetConfig.BASE_USER_PLUS + App.getUid() + "/comments")
                .addParams("page", page + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastNetWorkError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!response.contains("code")) {
                            ToastNetWorkError();
                            if (replyAdapter != null)
                                replyAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                            return;
                        }

                        JSONObject dataObj = JSON.parseObject(response);
                        if (dataObj.getInteger("code") == 50011) {
                            getNewToken(page);
                        } else if (dataObj.getInteger("code") != 20000) {
                            emitter.onError(new Throwable("20000 "));
                            ToastShort("服务器出状况惹，稍等喔( • ̀ω•́ )✧");
                        } else {
                            max_page_reply = max_page_reply >= page ? max_page_reply : page;
                            JSONObject obj = new JSONObject();
                            obj.put("type",TYPE_REPLY);
                            obj.put("data",dataObj.getString("data"));
                            emitter.onNext(obj.toJSONString());
                        }
                    }
                }))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 从服务器获取我的帖子
     */
    private void getMyListData(int page) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> OkHttpUtils.get()
                .url(NetConfig.BASE_USER_PLUS + App.getUid() + "/discussions")
                .addHeader("Authorization", "Bearer " + Store.getInstance().getToken())
                .addParams("page", page + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastNetWorkError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!response.contains("code")) {
                            ToastNetWorkError();
                            if (myPostAdapter != null)
                                myPostAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                            return;
                        }

                        JSONObject dataObj = JSON.parseObject(response);
                        if (dataObj.getInteger("code") == 50011) {
                            getNewToken(page);
                        } else if (dataObj.getInteger("code") != 20000) {
                            ToastShort("服务器出状况惹，稍等喔( • ̀ω•́ )✧");
                        } else {
                            max_page_my = max_page_my >= page ? max_page_my : page;
                            JSONObject obj = new JSONObject();
                            obj.put("type",TYPE_MY);
                            obj.put("data",dataObj.getString("data"));
                            emitter.onNext(obj.toJSONString());
                        }
                    }
                }))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 从服务器获取帖子
     */
    private void getPostListData(int page) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> OkHttpUtils.get()
                .url(NetConfig.BASE_POST_PLUS)
                .addParams("page", page + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastNetWorkError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!response.contains("code")) {
                            ToastNetWorkError();
                            if (postAdapter != null)
                                postAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                            return;
                        }

                        JSONObject dataObj = JSON.parseObject(response);
                        if (dataObj.getInteger("code") != 20000) {
                            ToastShort("服务器出状况惹，稍等喔( • ̀ω•́ )✧");
                        } else {
                            max_page_post = max_page_post >= page ? max_page_post : page;
                            JSONObject obj = new JSONObject();
                            obj.put("type",TYPE_NEW);
                            obj.put("data",dataObj.getString("data"));
                            emitter.onNext(obj.toJSONString());
                        }
                    }
                }))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 获取新的Token
     */
    @SuppressLint("CheckResult")
    private void getNewToken(int page) {
        RetrofitService.getNewToken()
                .subscribe(s -> getListData(page));
    }
}
