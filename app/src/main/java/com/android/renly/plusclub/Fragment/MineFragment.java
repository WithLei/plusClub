package com.android.renly.plusclub.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BaseFragment {
    @BindView(R.id.ci_mine_user_img)
    CircleImageView ciMineUserImg;
    @BindView(R.id.rl_mine_header)
    RelativeLayout rlMineHeader;
    @BindView(R.id.ll_mine_history)
    LinearLayout llMineHistory;
    @BindView(R.id.ll_mine_star)
    LinearLayout llMineStar;
    @BindView(R.id.ll_mine_friend)
    LinearLayout llMineFriend;
    @BindView(R.id.ll_mine_post)
    LinearLayout llMinePost;
    @BindView(R.id.lv_mine_function_list)
    ListView lvMineFunctionList;
    @BindView(R.id.ll_mine_window)
    LinearLayout llMineWindow;
    Unbinder unbinder;
    private String username, uid;
    private CircleImageView userImg;
    private TextView userName, userGrade;
    //记录上次创建时候是否登录
    private boolean isLoginLast = false;

    private final int[] icons = new int[]{
            R.drawable.ic_autorenew_black_24dp,
            R.drawable.ic_palette_black_24dp,
            R.drawable.ic_settings_24dp,
            R.drawable.ic_info_24dp,
            R.drawable.ic_menu_share_24dp,
            R.drawable.ic_favorite_white_12dp,
    };

    private final String[] titles = new String[]{
            "签到中心",
            "主题设置",
            "设置",
            "关于本程序",
            "分享手机睿思",
            "到商店评分",
    };

    @Override
    public int getLayoutid() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData(Context content) {

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
    }

    @OnClick({R.id.ci_mine_user_img, R.id.ll_mine_history, R.id.ll_mine_star, R.id.ll_mine_friend, R.id.ll_mine_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ci_mine_user_img:
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
}
