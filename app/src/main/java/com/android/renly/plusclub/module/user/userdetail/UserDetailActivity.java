package com.android.renly.plusclub.module.user.userdetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.api.bean.Store;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.utils.toast.MyToast;
import com.android.renly.plusclub.utils.NetConfig;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.widget.CircleImageView;
import com.android.renly.plusclub.widget.GradeProgressView;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
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
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.main_window)
    CoordinatorLayout mainWindow;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    private final List<String>keys = new ArrayList<>();
    private final List<String>values = new ArrayList<>();

    public static final int requestCode = 128;
    @BindView(R.id.listView)
    ListView listView;

    private long userid;
    private String username = null;
    private String imageUrl = null;
    // 对象是否为登陆用户
    private boolean isLoginUser = false;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected void initData() {
        getUserInfo();
    }

    @Override
    protected void initView() {
        initSlidr();
        toolbarLayout.setTitle(username);
    }

    private void getUserInfo() {
        Intent intent = getIntent();
        userid = intent.getExtras().getLong("userid");
        if (TextUtils.equals(String.valueOf(userid), String.valueOf(App.getUid())))
            isLoginUser = true;
        getInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btn_logout)
    public void onViewClicked() {
        onLogout();
    }

    /**
     * 点击退出登录按钮后
     */
    private void onLogout() {
        App.setIsLogout();
        setResult(RESULT_OK);
        MyToast.showText(this, "退出登录成功", Toast.LENGTH_SHORT, true);
        finishActivity();
    }

    public void getInfo() {
        if (isLoginUser) {
            // 获取用户个人信息
            OkHttpUtils.get()
                    .url(NetConfig.BASE_USERDETAIL_PLUS)
                    .addHeader("Authorization", "Bearer " + Store.getInstance().getToken())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            printLog("UserDetailActivity getInfo getUserDetail onError" + e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (!response.contains("code")) {
                                ToastNetWorkError();
                                return;
                            }
                            JSONObject jsonObject = JSON.parseObject(response);
                            if (jsonObject.getInteger("code") == 50011){
                                getNewToken();
                            }else if (jsonObject.getInteger("code") != 20000) {
                                ToastShort("查无此用户信息");
                            } else {
                                Message msg = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("resultObject", jsonObject.getString("result"));
                                msg.setData(bundle);
                                msg.what = GET_INFO;
                                handler.sendMessage(msg);
                            }
                        }
                    });
        } else {
            // 获取他人信息
            OkHttpUtils.get()
                    .url(NetConfig.BASE_USER_PLUS + userid)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            printLog("UserDetailActivity getInfo getUserDetail2 onError " + e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (!response.contains("code")) {
                                ToastNetWorkError();
                                return;
                            }
                            JSONObject jsonObject = JSON.parseObject(response);
                            if (jsonObject.getInteger("code") != 20000) {
                                ToastShort("查无此用户信息");
                            } else {
                                Message msg = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("resultObject", jsonObject.getString("data"));
                                msg.setData(bundle);
                                msg.what = GET_INFO;
                                handler.sendMessage(msg);
                            }

                        }
                    });
        }
    }

    /**
     * 获取新的Token
     */
    private void getNewToken() {
        OkHttpUtils.post()
                .url(NetConfig.BASE_GETNEWTOKEN_PLUS)
                .addHeader("Authorization","Bearer " + Store.getInstance().getToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("HomeFragment getNewToken onError");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getInteger("code") != 20000){
                            printLog("HomeFragment getNewToken() onResponse获取Token失败,重新登陆");
                        }else{
                            Store.getInstance().setToken(obj.getString("result"));
                            getInfo();
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
        keys.add("邮箱");
        values.add(obj.getString("email"));
        keys.add("班级");

        if (isLoginUser) {
            values.add(obj.getString("grades").trim().isEmpty() ? "null" : obj.getString("grades"));
            keys.add("学号");
            values.add(obj.getString("studentid").trim().isEmpty() ? "null" : obj.getString("studentid"));
            keys.add("手机号");
            values.add(obj.getString("phone").trim().isEmpty() ? "null" : obj.getString("phone"));
            keys.add("最近发帖/回帖");
            values.add(obj.getString("updated_at").trim().isEmpty() ? "null" : obj.getString("updated_at"));
            if (obj.containsKey("role")){
                keys.add("身份");
                values.add(obj.getString("role").equals("null") ? "普通用户" : obj.getString("role"));
            }
        }else{
            values.add(obj.getString("grade") == null ? "null" : obj.getString("grade"));
        }
        keys.add("注册时间");
        values.add(obj.getString("created_at"));


        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            Map<String, String> ob = new HashMap<>();
            ob.put("key", keys.get(i));
            ob.put("value", values.get(i));
            list.add(ob);
        }
        listView.setAdapter(new SimpleAdapter(this, list, R.layout.item_sim_list, new String[]{"key", "value"}, new int[]{R.id.key, R.id.value}));
    }

}
