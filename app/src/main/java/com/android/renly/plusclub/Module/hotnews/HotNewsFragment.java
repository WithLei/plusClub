package com.android.renly.plusclub.Module.hotnews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Injector.components.DaggerHotNewsFragComponent;
import com.android.renly.plusclub.Injector.modules.HotNewsFragModule;
import com.android.renly.plusclub.Module.base.BaseFragment;
import com.android.renly.plusclub.Listener.LoadMoreListener;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.BatchRadioButton;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import static com.android.renly.plusclub.Utils.LogUtils.printLog;
import static com.android.renly.plusclub.Utils.ToastUtils.*;

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
    @BindView(R.id.btn_change)
    RadioGroup btnChange;

    @Inject
    protected HotNewsFragPresenter mPresenter;

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
        mPresenter.getData(false, mContent, currentType);
        initView();
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
    protected int currentType = TYPE_NEW;
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
    protected RecyclerView.LayoutManager mLayoutManager;
    protected LoadMoreListener loadMoreListener;

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
        mPresenter.getData(true, mContent, currentType);
    }

    /**
     * 下拉刷新样式
     * isRefresh
     * true 有刷新请求
     * false 无刷新请求
     */
    protected boolean isPullDownRefresh = false;

    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.red_light, R.color.green_light, R.color.blue_light, R.color.orange_light);
        refreshLayout.setOnRefreshListener(() -> new Thread() {
            @Override
            public void run() {
                doRefresh();
            }
        }.start());
    }

    protected boolean new_loadnothing = false;
    protected boolean reply_loadnothing = false;
    protected boolean my_loadnothing = false;

    protected void afterGetDataSuccess(int type, String data){
        mPresenter.afterGetDataSuccess(data, type, mContent);
        isPullDownRefresh = false;
        isPullUpRefresh = false;
        tvHotnewsShowlogin.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    protected void setLoadMoreListener(int type) {
        if (type == TYPE_NEW)
            loadMoreListener = new LoadMoreListener((LinearLayoutManager) mLayoutManager, this, 5);
        else
            loadMoreListener = new LoadMoreListener((LinearLayoutManager) mLayoutManager, this, 4);
        rv.addOnScrollListener(loadMoreListener);
    }

    @Override
    public void ScrollToTop() {

    }

    @Override
    protected void initInjector() {
        DaggerHotNewsFragComponent.builder()
                .applicationComponent(App.getAppComponent())
                .hotNewsFragModule(new HotNewsFragModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                mPresenter.getListData(mPresenter.max_page_post + 1, TYPE_NEW);
                break;
            case TYPE_REPLY:
                mPresenter.getListData(mPresenter.max_page_reply + 1, TYPE_REPLY);
                break;
            case TYPE_MY:
                mPresenter.getListData(mPresenter.max_page_my + 1, TYPE_MY);
                break;
        }
    }
}

