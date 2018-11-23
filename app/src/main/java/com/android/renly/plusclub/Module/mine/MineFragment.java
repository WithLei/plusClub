package com.android.renly.plusclub.Module.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Activity.AboutActivity;
import com.android.renly.plusclub.Activity.HomeActivity;
import com.android.renly.plusclub.Activity.LabActivity;
import com.android.renly.plusclub.Activity.LoginActivity;
import com.android.renly.plusclub.Activity.OpenSourceActivity;
import com.android.renly.plusclub.Activity.SettingActivity;
import com.android.renly.plusclub.Activity.ThemeActivity;
import com.android.renly.plusclub.Activity.UserDetailActivity;
import com.android.renly.plusclub.Api.Bean.Store;
import com.android.renly.plusclub.Api.RetrofitService;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Module.base.BaseFragment;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.android.renly.plusclub.Utils.IntentUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MineFragment extends BaseFragment
        implements AdapterView.OnItemClickListener, MineFragView {
    @BindView(R.id.ci_mine_user_img)
    CircleImageView ciMineUserImg;
    @BindView(R.id.lv_mine_function_list)
    ListView lvMineFunctionList;
    @BindView(R.id.tv_mine_user_name)
    TextView tvMineUserName;
    @BindView(R.id.tv_mine_user_email)
    TextView tvMineUserEmail;

    @Inject
    protected MineFragPresenter mPresenter;

    @Override
    public int getLayoutid() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData(Context content) {
        lvMineFunctionList.setAdapter(new SimpleAdapter(mActivity, mPresenter.getMenuList(), R.layout.item_function, new String[]{"icon", "title"}, new int[]{R.id.icon, R.id.title}));
        lvMineFunctionList.setOnItemClickListener(this);
    }

    @Override
    public void initView() {
        initInfo();
    }

    public void doRefresh() {
        initInfo();
    }

    private void initInfo() {
        if (App.ISLOGIN()) {
            tvMineUserName.setText(App.getUserName());
        } else {
            ciMineUserImg.setImageDrawable(getResources().getDrawable(R.drawable.image_placeholder));
            tvMineUserName.setText("点击头像登陆");
            tvMineUserEmail.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void ScrollToTop() {

    }

    @OnClick({R.id.ci_mine_user_img, R.id.ll_mine_history, R.id.ll_mine_star, R.id.ll_mine_friend, R.id.ll_mine_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ci_mine_user_img:
                if (!App.ISLOGIN()) {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivityForResult(intent, LoginActivity.requestCode);
                } else {
                    Intent intent = new Intent(mActivity, UserDetailActivity.class);
                    intent.putExtra("userid", App.getUid());
                    mActivity.startActivity(intent);
                }
                mActivity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
                break;
            case R.id.ll_mine_history:
                break;
            case R.id.ll_mine_star:
                break;
            case R.id.ll_mine_friend:
                break;
            case R.id.ll_mine_post:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case 0:
                // 主题设置
                Intent intent = new Intent(mActivity, ThemeActivity.class);
                mActivity.startActivityForResult(intent, ThemeActivity.requestCode);
//                mActivity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
                break;
            case 1:
                // 设置
                Intent intent1 = new Intent(mActivity, SettingActivity.class);
                mActivity.startActivityForResult(intent1, SettingActivity.requestCode);
                break;
            case 2:
                // 分享Plus客户端
                String data = "这个Plus Club客户端非常不错，分享给你们。" + NetConfig.PLUSCLUB_ITEM;
                IntentUtils.shareApp(mActivity, data);
                break;
            case 3:
                // 关于本程序
                gotoActivity(AboutActivity.class);
                break;
            case 4:
                // 热爱开源，感谢分享
                gotoActivity(OpenSourceActivity.class);
                break;
            case 5:
                // 实验室功能
                gotoActivity(LabActivity.class);
                break;
        }
    }


    protected void setInfo(String avatarSrc, String userName) {
        Picasso.get()
                .load(avatarSrc)
                .placeholder(R.drawable.image_placeholder)
                .into(ciMineUserImg);

        tvMineUserName.setText(userName);
        tvMineUserEmail.setVisibility(View.VISIBLE);
        tvMineUserEmail.setText(App.getEmail());
    }

    private HomeActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (HomeActivity) context;
    }

    @Override
    public void loadAvatar(String path) {

    }
}
