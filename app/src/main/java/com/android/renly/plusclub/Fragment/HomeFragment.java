package com.android.renly.plusclub.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.renly.plusclub.Activity.LoginActivity;
import com.android.renly.plusclub.Activity.ThemeActivity;
import com.android.renly.plusclub.Activity.UserDetailActivity;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.ci_home_img)
    CircleImageView ciHomeImg;
    @BindView(R.id.tv_home_title)
    TextView tvHomeTitle;
    @BindView(R.id.iv_home_search)
    ImageView ivHomeSearch;
    @BindView(R.id.recycler_home_view)
    RecyclerView recyclerHomeView;
    Unbinder unbinder;

    @Override
    public int getLayoutid() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData(Context content) {
        if (App.ISLOGIN(getActivity()))
            ciHomeImg.setImageDrawable(getResources().getDrawable(R.mipmap.pluslogo_round));
        else
            ciHomeImg.setImageDrawable(getResources().getDrawable(R.drawable.image_placeholder));
    }

    @Override
    public void ScrollToTop() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ThemeActivity.requestCode && resultCode == RESULT_OK)
            ciHomeImg.setImageDrawable(getResources().getDrawable(R.mipmap.pluslogo_round));
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

    @OnClick({R.id.ci_home_img, R.id.iv_home_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ci_home_img:
                if (App.ISLOGIN(getActivity())){
                    Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                    intent.putExtra("userName",App.getUid(getActivity()));
                    intent.putExtra("avatarUrl","");
                    getActivity().startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(intent,LoginActivity.requestCode);
                }
                break;
            case R.id.iv_home_search:
                break;
        }
    }
}
