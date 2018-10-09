package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.MyToast;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.android.renly.plusclub.UI.GradeProgressView;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

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

    }

    @Override
    protected void initView() {
        initInfo();
        initSlidr();
        toolbarLayout.setTitle(username);
    }

    private void initInfo() {
        getInfo();
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

    public void getInfo() {
        OkHttpUtils.get()
                .url(NetConfig.BASE_USERDETAIL_PLUS)
                .addHeader("Authorization", "Bearer " + App.getToken(this))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("getUserAvator onError" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 20000) {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("resultObject",jsonObject.getString("result"));
                            msg.setData(bundle);
                            msg.what = GET_INFO;
                            handler.sendMessage(msg);
                        }

                    }
                });
    }

    private static final int GET_INFO = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_INFO:
                    setInfo(msg.getData().getString("resultObject"));
                    break;
            }
        }
    };

    private void setInfo(String result) {
        JSONObject obj = JSON.parseObject(result);
        Picasso.get()
                .load(obj.getString("avatar"))
                .placeholder(R.drawable.image_placeholder)
                .into(userDetailImgAvatar);
        toolbarLayout.setTitle(obj.getString("name"));
    }

}
