package com.android.renly.plusclub.Activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Adapter.PostAdapter;
import com.android.renly.plusclub.Bean.Post;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.Fragment.PostContentFragment;
import com.android.renly.plusclub.Listener.LoadMoreListener;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.Utils.DimmenUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

import static com.android.renly.plusclub.Adapter.PostAdapter.STATE_LOADING;
import static com.android.renly.plusclub.Adapter.PostAdapter.STATE_LOAD_FAIL;
import static com.android.renly.plusclub.Adapter.PostAdapter.STATE_LOAD_NOTHING;

public class PostActivity extends BaseActivity implements LoadMoreListener.OnLoadMoreListener{
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

    private Unbinder unbinder;

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
    private List<PostContentFragment> fragmentPool;
    private PostContentFragment nowFragment = null;

    private PostAdapter adapter;

    private static final int GET_POST_SUCCESS = 2;
    private static final int GET_POST_FAIL = 4;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_POST_SUCCESS:
                    initPostListData(msg.getData().getString("data"));
                    // 处理第一次刷新和后续刷新
                    if (adapter == null)
                        initpostList();
                    else
                        adapter.notifyDataSetChanged();
                    //
                    if (refreshLayout.isRefreshing())
                        refreshLayout.setRefreshing(false);
                    break;
                case GET_POST_FAIL:
                    ToastShort("获取列表数据失败");
                    adapter.changeLoadMoreState(STATE_LOAD_FAIL);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post;
    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra("Title");
        fragmentManager = getSupportFragmentManager();
        getPostListData(1);
    }

    @Override
    protected void initView() {
        initToolBar(true, title);
        addToolbarMenu(R.drawable.ic_create_black_24dp).setOnClickListener(view -> {
            Intent intent = new Intent(PostActivity.this,EditAcitivity.class);
            startActivityForResult(intent, EditAcitivity.requestCode);
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
     * isRefresh
     * true 有刷新请求
     * false 无刷新请求
     */
    private boolean isRefresh = false;
    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
        refreshLayout.setOnRefreshListener(() -> new Thread(){
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
        isRefresh = true;
        getPostListData(1);
        max_page = 1;
    }

    /**
     * 从服务器获取帖子
     */
    private void getPostListData(int page) {
        OkHttpUtils.get()
                .url(NetConfig.BASE_POST_PLUS)
                .addParams("page",page+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastShort("网络出状况咯ヽ(#`Д´)ﾉ");
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        printLog(response);
                        if (!response.contains("code")){
                            ToastShort("请检查网络设置ヽ(#`Д´)ﾉ");
                            return;
                        }

                        JSONObject dataObj = JSON.parseObject(response);
                        if (dataObj.getInteger("code") != 20000){
                            ToastShort("服务器出状况惹，再试试( • ̀ω•́ )✧");
                        }else{
                            max_page = max_page >= page ? max_page : page;
                            Message msg = new Message();
                            msg.what = GET_POST_SUCCESS;
                            Bundle bundle = new Bundle();
                            bundle.putString("data",dataObj.getString("data"));
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                });
    }

    /**
     * 准备测试的帖子列表数据
     */
    private void initPostListData(String JsonDataArray) {
        if (isRefresh){
            // 下拉刷新的请求
            postList.clear();
            isRefresh = false;
            adapter.changeLoadMoreState(STATE_LOADING);
        }
        JSONObject jsonObject = JSON.parseObject(JsonDataArray);
        // 尾页处理
        if (jsonObject.getInteger("current_page").equals(jsonObject.getInteger("last_page"))){
            if (adapter != null)
                adapter.changeLoadMoreState(STATE_LOAD_NOTHING);
            return ;
        }
        JSONArray array = JSON.parseArray(jsonObject.getString("data"));
        for (int i = 0;i < array.size();i++){
            postList.add(JSON.parseObject(array.getString(i), Post.class));
            printLog(postList.get(postList.size()-1).toString());
        }
    }

    private void initpostList() {
        // 初始化fragmentPool池
        fragmentPool = new ArrayList<>();
        adapter = new PostAdapter(this, postList);
        // 设置监听事件
        rvPost.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPost.setLayoutManager(mLayoutManager);
        rvPost.addOnScrollListener(new LoadMoreListener((LinearLayoutManager)mLayoutManager,this, 5));
        adapter.setOnItemClickListener(pos -> {
            PostContentFragment fragment = new PostContentFragment();
            nowFragment = fragment;
            fragmentPool.add(fragment);
            Bundle bundle = new Bundle();
            bundle.putString("PostJsonObject",JSON.toJSONString(postList.get(pos)));
            fragment.setArguments(bundle);
            loadPanel(fragment, fragmentPool.size() == 1 ? null : fragmentPool.get(fragmentPool.size() - 2));
        });
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
        slidingLayout.setPanelHeight(DimmenUtils.dip2px(this,120));
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
                        || (previousState == PanelState.DRAGGING && newState == PanelState.ANCHORED && ppState == PanelState.EXPANDED))
                {
                    nowFragment.removeMyInputBar();
                    animToolBar(false);
                }
                else{
                    nowFragment.initMyInputBar();
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
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        if (slidingLayout != null &&
                (slidingLayout.getPanelState() == PanelState.EXPANDED
                        || slidingLayout.getPanelState() == PanelState.ANCHORED )) {
            slidingLayout.setPanelState(PanelState.COLLAPSED);
            if (nowFragment != null) {
                svPostcontent.stopNestedScroll();
                svPostcontent.scrollTo(0, 0);
            }
        } else if(slidingLayout != null && slidingLayout.getPanelState() == PanelState.COLLAPSED) {
            slidingLayout.setPanelState(PanelState.HIDDEN);
        }else{
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
        printLog("向上滑动");
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
        printLog("向下滑动");
    }

    public void hidePanel(){
        slidingLayout.setPanelState(PanelState.HIDDEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch(requestCode){
                case EditAcitivity.requestCode:
                    doRefresh();
                    hideSoftInput();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSoftInput();
    }

    private void hideSoftInput() {
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onLoadMore() {
        getPostListData(max_page+1);
    }
}
