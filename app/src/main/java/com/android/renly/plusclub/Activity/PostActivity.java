package com.android.renly.plusclub.Activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PostActivity extends BaseActivity {
    @BindView(R.id.myToolBar)
    FrameLayout myToolBar;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    private Unbinder unbinder;
    private String title;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post;
    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra("Title");
    }

    @Override
    protected void initView() {
        initToolBar(true,title);
        initSlidr();
        initSlidingLayout();
    }

    private void initSlidingLayout() {
        slidingLayout.setAnchorPoint(0.7f);
        slidingLayout.setPanelState(PanelState.COLLAPSED);
        slidingLayout.setFadeOnClickListener(view -> {
            mAnimator = new ObjectAnimator().ofFloat(myToolBar,"translationY",myToolBar.getTranslationY(),0);
            mAnimator.start();
            isShow = true;
            slidingLayout.setPanelState(PanelState.COLLAPSED);
            printLog("触发点击");
        });
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.SimplePanelSlideListener(){
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                super.onPanelSlide(panel, slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                if (newState == PanelState.DRAGGING){
                    ppState = previousState;
                    return;
                }
                if (newState == PanelState.HIDDEN
                        || newState == PanelState.COLLAPSED
                        ||(previousState == PanelState.DRAGGING && newState == PanelState.ANCHORED && ppState == PanelState.EXPANDED))
                    animToolBar(false);
                else
                    animToolBar(true);
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
    }

    @Override
    public void onBackPressed() {
        if (slidingLayout != null &&
                (slidingLayout.getPanelState() == PanelState.EXPANDED || slidingLayout.getPanelState() == PanelState.ANCHORED)) {
            slidingLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    Animator mAnimator;
    boolean isShow = true;
    public void animToolBar(boolean isUP){
        if (mAnimator != null && mAnimator.isRunning()){
            printLog("return 状态");
            return;
        }

        if (isUP && isShow){
            // 向上滑动
            isShow = false;
            printLog("向上滑动");
            mAnimator = new ObjectAnimator().ofFloat(myToolBar,"translationY",myToolBar.getTranslationY(),-myToolBar.getHeight());
            mAnimator.start();
        }else if (!isUP && !isShow){
            // 向下滑动
            isShow = true;
            printLog("向下滑动");
            mAnimator = new ObjectAnimator().ofFloat(myToolBar,"translationY",myToolBar.getTranslationY(),0);
            mAnimator.start();
        }
    }

}
