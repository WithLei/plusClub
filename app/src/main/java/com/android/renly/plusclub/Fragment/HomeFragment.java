package com.android.renly.plusclub.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Activity.LoginActivity;
import com.android.renly.plusclub.Activity.PostsActivity;
import com.android.renly.plusclub.Activity.UserDetailActivity;
import com.android.renly.plusclub.Adapter.ForumAdapter;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Bean.Forum;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.ci_home_img)
    CircleImageView ciHomeImg;
    @BindView(R.id.tv_home_title)
    TextView tvHomeTitle;
    @BindView(R.id.iv_home_search)
    ImageView ivHomeSearch;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.list)
    RecyclerView list;
    private Unbinder unbinder;


    private List<Forum> forumList;
    private String[] headers;

    @Override
    public int getLayoutid() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData(Context content) {
        if (App.ISLOGIN(getActivity()))
            getUserAvator();
        else
            ciHomeImg.setImageDrawable(getResources().getDrawable(R.drawable.image_placeholder));
        forumList = new ArrayList<>();
        forumList.add(new Forum("灌水专区", R.drawable.icon_mine_collect, 0, "daily"));
        forumList.add(new Forum("技术交流", R.drawable.icon_mine_friend, 0, "code"));
        forumList.add(new Forum("问答专区", R.drawable.icon_mine_info, 1, "qa"));
        forumList.add(new Forum("发展建议", R.drawable.icon_mine_friend, 1, "suggests"));
        forumList.add(new Forum("论坛反馈", R.drawable.icon_mine_history, 2, "feedback"));
        forumList.add(new Forum("校园交易", R.drawable.icon_mine_friend, 2, "transaction"));
        forumList.add(new Forum("公告活动", R.drawable.icon_mine_friend, 2, "activity"));

        headers = new String[]{
                "休闲娱乐",
                "校园生活",
                "学术交流"
        };
        initView();
    }

    private void initView() {
        ForumAdapter adapter = new ForumAdapter(getActivity(), forumList);
        adapter.setOnItemClickListener(pos -> {
            Intent intent = new Intent(getActivity(), PostsActivity.class);
            intent.putExtra("Title",forumList.get(pos).getTitle());
            intent.putExtra("category",forumList.get(pos).getCategory());
            startActivity(intent);
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        list.setClipToPadding(false);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
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
        handler.removeCallbacksAndMessages(null);
        unbinder.unbind();
    }

    @OnClick({R.id.ci_home_img, R.id.iv_home_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ci_home_img:
                if (App.ISLOGIN(getActivity())) {
                    Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                    intent.putExtra("userid", App.getUid(getActivity()));
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(intent, LoginActivity.requestCode);
                }
                getActivity().overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
                break;
            case R.id.iv_home_search:
                ToastProgramError();
                break;
        }
    }

    private static final int REFRESH_END = 2;
    private static final int GET_AVATAR = 4;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_END:

                    break;
                case GET_AVATAR:
                    Picasso.get()
                            .load(msg.getData().getString("avatar"))
                            .placeholder(R.drawable.image_placeholder)
                            .into(ciHomeImg);
                    break;
            }
        }
    };

    public void getUserAvator() {
        OkHttpUtils.get()
                .url(NetConfig.BASE_USERDETAIL_PLUS)
                .addHeader("Authorization", "Bearer " + App.getToken(getActivity()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("getUserAvator onError" + e.getMessage());
                        ToastNetWorkError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!response.contains("code")) {
                            ToastNetWorkError();
                            return;
                        }
                        JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 50011) {
                            getNewToken();
                        } else if (jsonObject.getInteger("code") != 20000) {
                            ToastShort("服务器出状况惹，再试试( • ̀ω•́ )✧");
                            printLog("getInfoError" + response);
                        } else {
                            JSONObject obj = JSON.parseObject(jsonObject.getString("result"));
                            String avatarSrc = obj.getString("avatar");
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("avatar", avatarSrc);
                            msg.setData(bundle);
                            msg.what = GET_AVATAR;
                            handler.sendMessage(msg);
                        }

                    }
                });
    }

    /**
     * 获取新的Token
     */
    private void getNewToken() {
        OkHttpUtils.post()
                .url(NetConfig.BASE_GETNEWTOKEN_PLUS)
                .addHeader("Authorization", "Bearer " + App.getToken(getActivity()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("HomeFragment getNewToken onError");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getInteger("code") != 20000) {
                            printLog("HomeFragment getNewToken() onResponse获取Token失败,重新登陆");
                        } else {
                            App.setToken(getContext(), obj.getString("result"));
                            getUserAvator();
                        }
                    }
                });
    }
}
