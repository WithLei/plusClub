package com.android.renly.plusclub.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Activity.PostActivity;
import com.android.renly.plusclub.Adapter.MyPostAdapter;
import com.android.renly.plusclub.Adapter.PostAdapter;
import com.android.renly.plusclub.Adapter.ReplyAdapter;
import com.android.renly.plusclub.Api.Bean.Store;
import com.android.renly.plusclub.Api.RetrofitService;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Bean.Post;
import com.android.renly.plusclub.Bean.SimplePost;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.Listener.LoadMoreListener;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.BatchRadioButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.ResponseBody;

import static com.android.renly.plusclub.Adapter.PostAdapter.STATE_LOADING;
import static com.android.renly.plusclub.Adapter.PostAdapter.STATE_LOAD_FAIL;
import static com.android.renly.plusclub.Adapter.PostAdapter.STATE_LOAD_NOTHING;

public class HotNewsFragment extends BaseFragment
        implements LoadMoreListener.OnLoadMoreListener {
    @BindView(R.id.btn_1)
    BatchRadioButton btn1;
    @BindView(R.id.btn_2)
    BatchRadioButton btn2;
    @BindView(R.id.btn_3)
    BatchRadioButton btn3;
    @BindView(R.id.tv_hotnews_showlogin)
    TextView tvHotnewsShowlogin;
    @BindView(R.id.rv_hotnews)
    RecyclerView rv;
    @BindView(R.id.swiperefresh_hotnews)
    SwipeRefreshLayout refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.btn_change)
    RadioGroup btnChange;

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

    private MyHandler handler  = new MyHandler(getActivity());
    class MyHandler extends Handler{
        private WeakReference<Activity> weakReference;
        public MyHandler(Activity mActivity){
            weakReference = new WeakReference<>(mActivity);
            printLog("hotnewsFragment myHandler()");
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Activity mActivity = weakReference.get();
            printLog("handleMessage01");
            if (mActivity != null){
                printLog("handleMessage02 ");
                switch (msg.what) {
                }
            }
        }
    }


    @Override
    public int getLayoutid() {
        return R.layout.fragment_hotnews;
    }

    @Override
    protected void initData(Context content) {
        if (!App.ISLOGIN()) {
            tvHotnewsShowlogin.setText("登陆后就可以看了喔 ٩(๑❛ᴗ❛๑)۶");
            tvHotnewsShowlogin.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
            return;
        }
        tvHotnewsShowlogin.setText("刷新中...");
        postList = new ArrayList<>();
        replyList = new ArrayList<>();
        myList = new ArrayList<>();
        initObserver();
        getData(1);
        initView();
    }

    private Observer<String> observer;
    private void initObserver() {
        observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(String s) {
                JSONObject obj = JSON.parseObject(s);
                afterGetDataSuccess(obj.getInteger("type"),obj.getString("data"));
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        };
    }

    @Override
    protected void initView() {
        initRadioGroup();
        initRefreshLayout();
        initRecyclerView();
    }

    /**
     * 初始化单选条
     */
    private int currentType = TYPE_NEW;
    private static final int TYPE_NEW = 101;
    private static final int TYPE_REPLY = 102;
    private static final int TYPE_MY = 103;

    private void initRadioGroup() {
        btnChange.setOnCheckedChangeListener((radioGroup, id) -> {
            int nowState = -1;
            switch (id) {
                case R.id.btn_1:
                    nowState = TYPE_NEW;
                    break;
                case R.id.btn_2:
                    nowState = TYPE_REPLY;
                    break;
                case R.id.btn_3:
                    nowState = TYPE_MY;
                    break;
            }
            if (nowState != currentType) {
                currentType = nowState;
                refreshLayout.setRefreshing(true);
                doRefresh();
            }
        });
    }

    /**
     * 初始化recylerView的一些属性
     */
    private RecyclerView.LayoutManager mLayoutManager;
    private LoadMoreListener loadMoreListener;

    private void initRecyclerView() {
        // 设置监听事件
        if (loadMoreListener == null) {
            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            rv.setLayoutManager(mLayoutManager);
            loadMoreListener = new LoadMoreListener((LinearLayoutManager) mLayoutManager, this, 5);
        }
        rv.addOnScrollListener(loadMoreListener);

        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        // 调整draw缓存,加速recyclerview加载
        rv.setItemViewCacheSize(20);
        rv.setDrawingCacheEnabled(true);
        rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    }

    /**
     * 执行刷新操作
     */
    public void doRefresh() {
        if (!App.ISLOGIN()) {
            tvHotnewsShowlogin.setText("登陆后就可以看了喔 ٩(๑❛ᴗ❛๑)۶");
            tvHotnewsShowlogin.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
            return;
        }
        if (postList == null)
            initData(getmActivity());
        isPullDownRefresh = true;
        new Thread() {
            @Override
            public void run() {
                getActivity().runOnUiThread(() -> {
                    tvHotnewsShowlogin.setText("刷新中...");
                    tvHotnewsShowlogin.setVisibility(View.VISIBLE);
                });
            }
        }.start();

        getData(1);
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

    private void getData(int page) {
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
     * 下拉刷新样式
     * isRefresh
     * true 有刷新请求
     * false 无刷新请求
     */
    private boolean isPullDownRefresh = false;

    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
        refreshLayout.setOnRefreshListener(() -> new Thread() {
            @Override
            public void run() {
                doRefresh();
            }
        }.start());
    }

    /**
     * 初始化帖子列表数据
     */
    private void initListData(String JsonDataArray, int type) {
        if (isPullDownRefresh) {
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
        || (jsonObject.getInteger("current_page") == 1 && jsonObject.getInteger("current_page").equals(jsonObject.getInteger("last_page"))) ){
            switch (type) {
                case TYPE_NEW:
                    if (postAdapter != null) {
                        postAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                        return;
                    } else {
                        new_loadnothing = true;
                    }
                    break;
                case TYPE_REPLY:
                    if (replyAdapter != null) {
                        replyAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                        return;
                    } else {
                        reply_loadnothing = true;
                    }
                    break;
                case TYPE_MY:
                    if (myPostAdapter != null) {
                        myPostAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                        return;
                    } else {
                        my_loadnothing = true;
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


    private boolean new_loadnothing = false;
    private boolean reply_loadnothing = false;
    private boolean my_loadnothing = false;

    private void initList(int type) {
        switch (type) {
            case TYPE_NEW:
                postAdapter = new PostAdapter(getActivity(), postList);
                rv.setAdapter(postAdapter);
                postAdapter.setOnItemClickListener(pos -> {
                    Intent intent = new Intent(getActivity(), PostActivity.class);
                    intent.putExtra("PostJsonObject", JSON.toJSONString(postList.get(pos)));
                    intent.putExtra("isNormalPost", true);
                    startActivity(intent);
                });
                if (new_loadnothing) {
                    postAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                    new_loadnothing = false;
                }
                break;
            case TYPE_REPLY:
                replyAdapter = new ReplyAdapter(getActivity(), replyList);
                rv.setAdapter(replyAdapter);
                replyAdapter.setOnItemClickListener(pos -> {
                    Intent intent = new Intent(getActivity(), PostActivity.class);
                    intent.putExtra("id", replyList.get(pos).getDiscussion_id());
                    intent.putExtra("isNormalPost", false);
                    startActivity(intent);
                });
                // 初次加载
                if (reply_loadnothing) {
                    replyAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                    reply_loadnothing = false;
                }
                break;
            case TYPE_MY:
                myPostAdapter = new MyPostAdapter(getActivity(), myList);
                rv.setAdapter(myPostAdapter);
                myPostAdapter.setOnItemClickListener(pos -> {
                    Intent intent = new Intent(getActivity(), PostActivity.class);
                    intent.putExtra("id", myList.get(pos).getId());
                    intent.putExtra("isNormalPost", false);
                    startActivity(intent);
                });
                if (my_loadnothing) {
                    myPostAdapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                    my_loadnothing = false;
                }
                break;
        }
        if (type == TYPE_NEW)
            loadMoreListener = new LoadMoreListener((LinearLayoutManager) mLayoutManager, this, 5);
        else
            loadMoreListener = new LoadMoreListener((LinearLayoutManager) mLayoutManager, this, 4);
        rv.addOnScrollListener(loadMoreListener);
    }

    /**
     * 获取新的Token
     */
    @SuppressLint("CheckResult")
    private void getNewToken(int page) {
        RetrofitService.getNewToken()
                .subscribe(s -> getData(page));
    }

    private void afterGetDataSuccess(int type, String data){
        initListData(data, type);
        // 处理第一次刷新和后续刷新
        switch (type) {
            case TYPE_NEW:
                if (postAdapter == null)
                    initList(type);
                else
                    postAdapter.notifyDataSetChanged();
                break;
            case TYPE_REPLY:
                if (replyAdapter == null)
                    initList(type);
                else
                    replyAdapter.notifyDataSetChanged();
                break;
            case TYPE_MY:
                if (myPostAdapter == null)
                    initList(type);
                else
                    myPostAdapter.notifyDataSetChanged();
                break;
        }
        printLog("handleMessage03");
        isPullDownRefresh = false;
        isPullUpRefresh = false;
        tvHotnewsShowlogin.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    @Override
    public void ScrollToTop() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        handler.removeCallbacksAndMessages(null);
    }

    private boolean isPullUpRefresh = false;

    @Override
    public void onLoadMore() {
        if (isPullDownRefresh || isPullUpRefresh)
            return;
        isPullUpRefresh = true;
        switch (currentType) {
            case TYPE_NEW:
                getData(max_page_post + 1);
                break;
            case TYPE_REPLY:
                getData(max_page_reply + 1);
                break;
            case TYPE_MY:
                getData(max_page_my + 1);
                break;
        }
    }
}

