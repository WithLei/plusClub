package com.android.renly.plusclub.module.hotnews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.module.postContent.fullscreen.PostActivity;
import com.android.renly.plusclub.adapter.MyPostAdapter;
import com.android.renly.plusclub.adapter.PostAdapter;
import com.android.renly.plusclub.adapter.ReplyAdapter;
import com.android.renly.plusclub.api.bean.Post;
import com.android.renly.plusclub.api.bean.SimplePost;
import com.android.renly.plusclub.api.bean.Store;
import com.android.renly.plusclub.api.RetrofitService;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.utils.NetConfig;
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

import static com.android.renly.plusclub.adapter.BaseAdapter.*;
import static com.android.renly.plusclub.utils.toast.ToastUtils.*;

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
    int max_page_post = 0;
    int max_page_reply = 0;
    int max_page_my = 0;

    private PostAdapter postAdapter;
    private ReplyAdapter replyAdapter;
    private MyPostAdapter myPostAdapter;

    private Observer<String> observer;

    private static final int TYPE_NEW = 101;
    private static final int TYPE_REPLY = 102;
    private static final int TYPE_MY = 103;

    public HotNewsFragPresenter(HotNewsFragment mView) {
        this.mView = mView;
    }

    public void getData(boolean isRefresh, Context context, int currentType) {
        if (!isRefresh) {
            initList();
            initObserver();
            getListData(1, currentType);
        } else {
            if (postList == null)
                mView.initData(context);
            getListData(1, mView.currentType);
            switch (currentType) {
                case TYPE_NEW:
                    max_page_post = 1;
                    break;
                case TYPE_REPLY:
                    max_page_reply = 1;
                    break;
                case TYPE_MY:
                    max_page_my = 1;
                    break;
            }
        }
    }

    // 第一次加载的行为
    private void initAdapter(int type, Context context) {
        switch (type) {
            case TYPE_NEW:
                postAdapter = new PostAdapter(context, postList);
                mView.rv.setAdapter(postAdapter);
                postAdapter.setOnItemClickListener((v, position) -> {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("PostJsonObject", JSON.toJSONString(postList.get(position)));
                    intent.putExtra("isNormalPost", true);
                    context.startActivity(intent);
                });
                if (mView.new_loadnothing) {
                    postAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                    mView.new_loadnothing = false;
                }
                break;
            case TYPE_REPLY:
                replyAdapter = new ReplyAdapter(context, replyList);
                mView.rv.setAdapter(replyAdapter);
                replyAdapter.setOnItemClickListener((view, pos) -> {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("id", replyList.get(pos).getDiscussion_id());
                    intent.putExtra("isNormalPost", false);
                    context.startActivity(intent);
                });
                // 初次加载
                if (mView.reply_loadnothing) {
                    replyAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                    mView.reply_loadnothing = false;
                }
                break;
            case TYPE_MY:
                myPostAdapter = new MyPostAdapter(context, myList);
                mView.rv.setAdapter(myPostAdapter);
                myPostAdapter.setOnItemClickListener((view, pos) -> {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("id", myList.get(pos).getId());
                    intent.putExtra("isNormalPost", false);
                    context.startActivity(intent);
                });
                if (mView.my_loadnothing) {
                    myPostAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                    mView.my_loadnothing = false;
                }
                break;
        }
        mView.setLoadMoreListener(type);
    }

    void afterGetDataSuccess(String data, int type, Context context) {
        initListData(data, type);
        // 处理第一次刷新和后续刷新
        switch (type) {
            case TYPE_NEW:
                if (postAdapter == null)
                    initAdapter(type, context);
                else
                    postAdapter.notifyDataSetChanged();
                break;
            case TYPE_REPLY:
                if (replyAdapter == null)
                    initAdapter(type, context);
                else
                    replyAdapter.notifyDataSetChanged();
                break;
            case TYPE_MY:
                if (myPostAdapter == null)
                    initAdapter(type, context);
                else
                    myPostAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 初始化帖子列表数据
     */
    private void initListData(String JsonDataArray, int type) {
        if (mView.isPullDownRefresh) {
            // 处理下拉刷新的请求
            switch (type) {
                case TYPE_NEW:
                    postList.clear();
                    if (postAdapter != null)
                        postAdapter.changeLoadMoreState(STATE_LOADING);
                    break;
                case TYPE_REPLY:
                    replyList.clear();
                    if (replyAdapter != null)
                        replyAdapter.changeLoadMoreState(STATE_LOADING);
                    break;
                case TYPE_MY:
                    myList.clear();
                    if (myPostAdapter != null)
                        myPostAdapter.changeLoadMoreState(STATE_LOADING);
                    break;
            }
        }
        JSONObject jsonObject = JSON.parseObject(JsonDataArray);
        // 尾页处理
//        printLog("current " + jsonObject.getInteger("current_page") + " last " + jsonObject.getInteger("last_page"));
        if (jsonObject.getInteger("current_page") > jsonObject.getInteger("last_page")
                || (jsonObject.getInteger("current_page") == 1 && jsonObject.getInteger("current_page").equals(jsonObject.getInteger("last_page")))) {
            switch (type) {
                case TYPE_NEW:
                    if (postAdapter != null) {
                        postAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                        return;
                    } else {
                        mView.new_loadnothing = true;
                    }
                    break;
                case TYPE_REPLY:
                    if (replyAdapter != null) {
                        replyAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                        return;
                    } else {
                        mView.reply_loadnothing = true;
                    }
                    break;
                case TYPE_MY:
                    if (myPostAdapter != null) {
                        myPostAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                        return;
                    } else {
                        mView.my_loadnothing = true;
                    }
                    break;
            }
        }
        JSONArray array = JSON.parseArray(jsonObject.getString("data"));
        switch (type) {
            case TYPE_NEW:
                for (int i = 0; i < array.size(); i++)
                    postList.add(JSON.parseObject(array.getString(i), Post.class));
                break;
            case TYPE_REPLY:
                for (int i = 0; i < array.size(); i++)
                    replyList.add(JSON.parseObject(array.getString(i), SimplePost.class));
                break;
            case TYPE_MY:
                for (int i = 0; i < array.size(); i++)
                    myList.add(JSON.parseObject(array.getString(i), SimplePost.class));
                break;
        }

    }

    void getListData(int page, int currentType) {
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
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(String s) {
                JSONObject obj = JSON.parseObject(s);
                mView.afterGetDataSuccess(obj.getInteger("type"), obj.getString("data"));
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
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
                            getNewToken(page, TYPE_REPLY);
                        } else if (dataObj.getInteger("code") != 20000) {
                            emitter.onError(new Throwable("20000 "));
                            ToastShort("服务器出状况惹，稍等喔( • ̀ω•́ )✧");
                        } else {
                            max_page_reply = max_page_reply >= page ? max_page_reply : page;
                            JSONObject obj = new JSONObject();
                            obj.put("type", TYPE_REPLY);
                            obj.put("data", dataObj.getString("data"));
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
                            getNewToken(page, TYPE_MY);
                        } else if (dataObj.getInteger("code") != 20000) {
                            ToastShort("服务器出状况惹，稍等喔( • ̀ω•́ )✧");
                        } else {
                            max_page_my = max_page_my >= page ? max_page_my : page;
                            JSONObject obj = new JSONObject();
                            obj.put("type", TYPE_MY);
                            obj.put("data", dataObj.getString("data"));
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
                            obj.put("type", TYPE_NEW);
                            obj.put("data", dataObj.getString("data"));
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
    private void getNewToken(int page, int currentType) {
        RetrofitService.getNewToken()
                .subscribe(s -> getListData(page, currentType));
    }
}
