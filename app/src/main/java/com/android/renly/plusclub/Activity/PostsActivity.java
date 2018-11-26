package com.android.renly.plusclub.Activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.adapter.PostAdapter;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.api.bean.Post;
import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.utils.NetConfig;
import com.android.renly.plusclub.module.postContent.main.PostFragment;
import com.android.renly.plusclub.listener.LoadMoreListener;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.utils.DimmenUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.android.renly.plusclub.adapter.BaseAdapter.*;

public class PostsActivity extends BaseActivity
        implements LoadMoreListener.OnLoadMoreListener {
    @BindView(R.id.myToolBar)
    FrameLayout myToolBar;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    @BindView(R.id.rv_post)
    RecyclerView rvPost;
    @BindView(R.id.sv_postcontent)
    ScrollView svPostcontent;
    @BindView(R.id.swiperefresh_post)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_hotnews_showlogin)
    TextView tvHotnewsShowlogin;

    private Unbinder unbinder;

    /**
     * 当前专区分类
     */
    private String currentCategory;
    /**
     * 帖子区的标题
     */
    private String title;
    /**
     * 帖子列表
     */
    private List<Post> postList = new ArrayList<>();
    /**
     * 获取最大帖子页面
     */
    private int max_page = 0;
    /**
     * Panel Fragment管理器
     */
    private FragmentManager fragmentManager;
    /**
     * Panel Fragment池
     */
//    private List<PostFragment> fragmentPool;
    private PostFragment lastFragment = null;
    private PostFragment currentFragment = null;

    private PostAdapter adapter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_posts;
    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra("Title");
        currentCategory = getIntent().getStringExtra("category");
        fragmentManager = getSupportFragmentManager();
        getPostListData(1);
    }

    @Override
    protected void initView() {
        initToolBar(true, title);
        addToolbarMenu(R.drawable.ic_create_black_24dp).setOnClickListener(view -> {
            if (App.ISLOGIN()) {
                Intent intent = new Intent(PostsActivity.this, EditAcitivity.class);
                intent.putExtra("category", currentCategory);
                startActivityForResult(intent, EditAcitivity.requestCode);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("需要登录")
                        .setMessage("还没有登陆哦，赶快去登陆吧！")
                        .setCancelable(true)
                        .setPositiveButton("登陆", (dialogInterface, i) -> {
                            gotoActivity(LoginActivity.class);
                        })
                        .setNegativeButton("取消", (dialogInterface, i) -> {

                        })
                        .create()
                        .show();
            }
        });
        initSlidr();
        initRefreshLayout();
        initSlidingLayout();
        initRecyclerView();
    }

    /**
     * 初始化recylerView的一些属性
     */
    private void initRecyclerView() {
        rvPost.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // 调整draw缓存,加速recyclerview加载
        rvPost.setItemViewCacheSize(20);
        rvPost.setDrawingCacheEnabled(true);
        rvPost.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    }

    /**
     * 下拉刷新样式
     * isRefresh 用于锁住方法，防止多次请求导致bug
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
     * 执行刷新操作
     */
    private void doRefresh() {
        new Thread() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    tvHotnewsShowlogin.setVisibility(View.VISIBLE);
                    refreshLayout.setVisibility(View.GONE);
                    hidePanel();
                });
            }
        }.start();
        isPullDownRefresh = true;
        adapter = null;
        getPostListData(1);
        max_page = 1;
    }

    /**
     * 获取帖子成功
     * @param s
     */
    private void afterGetPostSuccess(String s) {
        initPostListData(s);
        // 处理第一次刷新和后续刷新
        if (adapter == null)
            initpostList();
        else
            adapter.notifyDataSetChanged();

        isPullDownRefresh = false;
        isPullUpRefresh = false;
        tvHotnewsShowlogin.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.VISIBLE);
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    /**
     * 从服务器获取帖子
     */
    @SuppressLint("CheckResult")
    private void getPostListData(int page) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(NetConfig.BASE_POST_PLUS)
                    .newBuilder();
            urlBuilder.addQueryParameter("categories", currentCategory);
            urlBuilder.addQueryParameter("page", page + "");
            Request request = new Request.Builder()
                    .url(urlBuilder.build())
                    .get()
                    .build();
            emitter.onNext(client.newCall(request).execute().body().string());
        })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (!s.contains("code")) {
                        ToastShort("请检查网络设置ヽ(#`Д´)ﾉ");
                        return;
                    }

                    JSONObject dataObj = JSON.parseObject(s);
                    if (dataObj.getInteger("code") != 20000) {
                        ToastShort("服务器出状况惹，再试试( • ̀ω•́ )✧");
                    } else {
                        max_page = max_page >= page ? max_page : page;
                        afterGetPostSuccess(dataObj.getString("data"));
                    }
                }, throwable -> {

                });
    }

    /**
     * 初始化帖子列表数据
     */
    private void initPostListData(String JsonDataArray) {
        if (isPullDownRefresh) {
            // 下拉刷新的请求
            postList.clear();
            if (adapter != null)
                adapter.changeLoadMoreState(STATE_LOADING);
        }
        JSONObject jsonObject = JSON.parseObject(JsonDataArray);
        // 尾页处理
        if (jsonObject.getInteger("current_page") >= jsonObject.getInteger("last_page")) {
            if (adapter != null) {
                adapter.changeLoadMoreState(STATE_LOAD_NOTHING);
                return;
            } else
                load_nothing = true;
        }
        JSONArray array = JSON.parseArray(jsonObject.getString("data"));
        for (int i = 0; i < array.size(); i++) {
            postList.add(JSON.parseObject(array.getString(i), Post.class));
        }

    }

    private boolean load_nothing = false;

    private void initpostList() {
        // 初始化fragmentPool池
//        fragmentPool = new ArrayList<>();
        adapter = new PostAdapter(this, postList);
        // 设置监听事件
        rvPost.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPost.setLayoutManager(mLayoutManager);
        rvPost.addOnScrollListener(new LoadMoreListener((LinearLayoutManager) mLayoutManager, this, 5));
        adapter.setOnItemClickListener((view, pos) -> {
            if (pos >= postList.size())
                return;
            lastFragment = currentFragment;
            currentFragment = new PostFragment();
//            PostFragment fragment = new PostFragment();
//            nowFragment = fragment;
//            fragmentPool.add(fragment);
            Bundle bundle = new Bundle();
            bundle.putString("PostJsonObject", JSON.toJSONString(postList.get(pos)));
            bundle.putString("from", "PostsActivity");
            currentFragment.setArguments(bundle);
            loadPanel(currentFragment, lastFragment);
//            fragment.setArguments(bundle);
//            loadPanel(fragment, fragmentPool.size() == 1 ? null : fragmentPool.get(fragmentPool.size() - 2));
        });
        if (load_nothing) {
            printLog("PostsActivity: load_nothing");
            adapter.changeLoadMoreState(STATE_LOAD_NOTHING);
            load_nothing = false;
        }
    }

    /**
     * 加载panel布局
     */
    private void loadPanel(Fragment targetFragment, Fragment lastFragment) {
        slidingLayout.setPanelState(PanelState.HIDDEN);

        //加载fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (lastFragment != null)
            transaction.remove(lastFragment);
        transaction.add(R.id.sv_postcontent, targetFragment);

        transaction.commit();
        //显示
        slidingLayout.setPanelState(PanelState.COLLAPSED);
    }

    /**
     * 初始化上拉布局
     */
    private void initSlidingLayout() {
        slidingLayout.setAnchorPoint(0.7f);
        slidingLayout.setPanelHeight(DimmenUtils.dip2px(this, 120));
        slidingLayout.setPanelState(PanelState.HIDDEN);
        slidingLayout.setFadeOnClickListener(view -> {
            doDownAnimation();
            slidingLayout.setPanelState(PanelState.COLLAPSED);
        });
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.SimplePanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                super.onPanelSlide(panel, slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                if (newState == PanelState.DRAGGING) {
                    ppState = previousState;
                    return;
                }
                if (newState == PanelState.COLLAPSED && ppState == PanelState.HIDDEN)
                    return;
                if (newState == PanelState.HIDDEN
                        || newState == PanelState.COLLAPSED
                        || (previousState == PanelState.DRAGGING && newState == PanelState.ANCHORED && ppState == PanelState.EXPANDED)) {
                    currentFragment.removeMyInputBar();
                    animToolBar(false);
                } else {
                    currentFragment.initMyInputBar();
                    animToolBar(true);
                }
            }
        });
    }

    // 前前次的状态
    PanelState ppState = PanelState.COLLAPSED;

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

    @Override
    public void onBackPressed() {
        if (slidingLayout != null &&
                (slidingLayout.getPanelState() == PanelState.EXPANDED
                        || slidingLayout.getPanelState() == PanelState.ANCHORED)) {
            slidingLayout.setPanelState(PanelState.COLLAPSED);
            if (currentFragment != null) {
                svPostcontent.stopNestedScroll();
                svPostcontent.scrollTo(0, 0);
            }
        } else if (slidingLayout != null && slidingLayout.getPanelState() == PanelState.COLLAPSED) {
            slidingLayout.setPanelState(PanelState.HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    Animator upAnimator, downAnimator;
    boolean isShow = true;

    public void animToolBar(boolean isUP) {
        if (upAnimator != null && downAnimator != null &&
                (upAnimator.isRunning() || downAnimator.isRunning())) {
            return;
        }
        if (isUP && isShow) {
            // 向上滑动
            doUpAnimation();
        } else if (!isUP && !isShow) {
            // 向下滑动
            doDownAnimation();
        }
    }

    private void doUpAnimation() {
        isShow = false;
        upAnimator = new ObjectAnimator().ofFloat(myToolBar, "translationY", myToolBar.getTranslationY(), -myToolBar.getHeight());
        upAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                myToolBar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        upAnimator.start();
    }

    private void doDownAnimation() {
        isShow = true;
        downAnimator = new ObjectAnimator().ofFloat(myToolBar, "translationY", myToolBar.getTranslationY(), 0);
        downAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                myToolBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        downAnimator.start();
    }

    public void hidePanel() {
        slidingLayout.setPanelState(PanelState.HIDDEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case EditAcitivity.requestCode:
                    doRefresh();
                    hideKeyBoard();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyBoard();
    }

    private boolean isPullUpRefresh = false;

    @Override
    public void onLoadMore() {
        if (isPullDownRefresh || isPullUpRefresh)
            return;
        isPullUpRefresh = true;
        getPostListData(max_page + 1);
    }
}
