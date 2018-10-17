package com.android.renly.plusclub.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Activity.LabActivity;
import com.android.renly.plusclub.Activity.LoginActivity;
import com.android.renly.plusclub.Activity.PostActivity;
import com.android.renly.plusclub.Activity.UserDetailActivity;
import com.android.renly.plusclub.Adapter.ForumAdapter;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Bean.Forum;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.squareup.picasso.Picasso;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.ci_home_img)
    CircleImageView ciHomeImg;
    @BindView(R.id.tv_home_title)
    TextView tvHomeTitle;
    @BindView(R.id.iv_home_search)
    ImageView ivHomeSearch;
    Unbinder unbinder;
    @BindView(R.id.refresh_layout)
    CircleRefreshLayout refreshLayout;
    private ExpandableStickyListHeadersListView list;

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
        forumList.add(new Forum("灌水专区", R.drawable.icon_mine_collect, 0));
        forumList.add(new Forum("摄影天地", R.drawable.icon_mine_friend, 0));
        forumList.add(new Forum("校园生活", R.drawable.icon_mine_info, 1));
        forumList.add(new Forum("失物招领", R.drawable.icon_mine_friend, 1));
        forumList.add(new Forum("技术博客", R.drawable.icon_mine_history, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));
        forumList.add(new Forum("学习交流", R.drawable.icon_mine_friend, 2));

        headers = new String[]{
                "休闲娱乐",
                "梨园生活",
                "学术交流"
        };
        list = getActivity().findViewById(R.id.list);
        list.setPadding(0, 0, 0, 0);
        StickyListHeadersAdapter adapter = new ForumAdapter(getContext(), forumList, headers);
        list.setAdapter(adapter);
        list.setOnItemClickListener((adapterView, view, pos, l) -> {
            Intent intent = new Intent(getActivity(), PostActivity.class);
            intent.putExtra("Title",forumList.get(pos).getTitle());
            startActivity(intent);
        });
        list.setOnHeaderClickListener((l, header, itemPosition, headerId, currentlySticky) -> {
            if (list.isHeaderCollapsed(headerId)) {
                list.expand(headerId);
            } else {
                list.collapse(headerId);
            }
        });

    }

    private void initView() {
        refreshLayout.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void completeRefresh() {
            }

            @Override
            public void refreshing() {
                handler.sendEmptyMessageDelayed(REFRESH_END, 3000);
            }
        });
    }

    @Override
    public void ScrollToTop() {
        if (forumList != null && forumList.size() > 0)
            list.scrollTo(0, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
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
                    intent.putExtra("userid",App.getUid(getActivity()));
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(intent, LoginActivity.requestCode);
                }
                getActivity().overridePendingTransition(R.anim.translate_in,R.anim.translate_out);
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
                    refreshLayout.finishRefreshing();
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
                .addHeader("Authorization","Bearer " + App.getToken(getActivity()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("getUserAvator onError" + e.getMessage());
                        ToastNetWorkError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!response.contains("code")){
                            ToastNetWorkError();
                            return;
                        }
                        JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 50011){
                            getNewToken();
                        }else if (jsonObject.getInteger("code") != 20000){
                            ToastShort("服务器出状况惹，再试试( • ̀ω•́ )✧");
                            printLog("getInfoError" + response);
                        }else{
                            JSONObject obj = JSON.parseObject(jsonObject.getString("result"));
                            String avatarSrc = obj.getString("avatar");
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("avatar",avatarSrc);
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
                .addHeader("Authorization","Bearer " + App.getToken(getActivity()))
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
                            App.setToken(getContext(),obj.getString("result"));
                            getUserAvator();
                        }
                    }
                });
    }
}
