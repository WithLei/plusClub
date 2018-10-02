package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.android.renly.plusclub.UI.GradeProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserDetailActivity extends BaseActivity {
    @BindView(R.id.user_detail_img_avatar)
    CircleImageView userDetailImgAvatar;
    @BindView(R.id.grade_progress)
    GradeProgressView gradeProgress;
    @BindView(R.id.progress_text)
    TextView progressText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.main_window)
    CoordinatorLayout mainWindow;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    public static final int requestCode = 128;

    private Unbinder unbinder;
    private String username = "";
    private String imageUrl = "";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected void initData() {
        username = getIntent().getStringExtra("userName");
        imageUrl = getIntent().getStringExtra("avatarUrl");
    }

    @Override
    protected void initView() {
        initSlidr();
        toolbarLayout.setTitle(username);
    }

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

    @OnClick(R.id.btn_logout)
    public void onViewClicked() {
        onLogout();
    }

    /**
     * 点击退出登录按钮后
     */
    private void onLogout() {
        App.setIsLogout(this);
        setResult(RESULT_OK);
        MyToast.showText(this,"退出登录成功", Toast.LENGTH_SHORT,true);
        finishActivity();
    }
}
