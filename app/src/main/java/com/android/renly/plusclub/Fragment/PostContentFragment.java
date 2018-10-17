package com.android.renly.plusclub.Fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.Activity.PostActivity;
import com.android.renly.plusclub.Activity.UserDetailActivity;
import com.android.renly.plusclub.Adapter.CommentAdapter;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.Bean.Comment;
import com.android.renly.plusclub.Bean.Post;
import com.android.renly.plusclub.Common.BaseFragment;
import com.android.renly.plusclub.Common.NetConfig;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.UI.CircleImageView;
import com.android.renly.plusclub.Utils.IntentUtils;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class PostContentFragment extends BaseFragment {
    @BindView(R.id.article_title)
    TextView articleTitle;
    @BindView(R.id.main_window)
    RelativeLayout mainWindow;
    @BindView(R.id.article_user_image)
    CircleImageView articleUserImage;
    @BindView(R.id.article_username)
    TextView articleUsername;
    @BindView(R.id.bt_lable_lz)
    TextView btLableLz;
    @BindView(R.id.article_post_time)
    TextView articlePostTime;
    @BindView(R.id.btn_more)
    ImageView btnMore;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    Unbinder unbinder;
    @BindView(R.id.tv_comment_suggest)
    TextView tvCommentSuggest;
    @BindView(R.id.load_bottom)
    LinearLayout loadBottom;

    private List<Comment> commentList;
    private Post postObj;
    private long postID;
    private String PostJsonString;
    // 输入框
    private View mInputBarView;
    private CommentAdapter adapter = null;

    private static final int GET_COMMENTDATA = 2;
    private static final int REFRESH_COMMENTLIST = 4;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_COMMENTDATA:
                    initCommentListData(msg.getData().getString("CommentJsonObj"));
                    if (adapter == null)
                        initCommentList();
                    else
                        adapter.notifyDataSetChanged();
                    break;
                case REFRESH_COMMENTLIST:
                    getCommentListData();
                    break;
            }
        }
    };

    @Override
    public int getLayoutid() {
        return R.layout.fragment_postcontent;
    }

    @Override
    protected void initData(Context content) {
        getPostObj();
        getCommentListData();
        initView();
    }

    private void initView() {
        initHead();
//        initCommentList();
    }

    /**
     * 初始化底部输入框
     */
    private EditText et;
    public void initMyInputBar() {
        if (!isInputBarShow) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.BOTTOM;
            mInputBarView = LayoutInflater.from(getContext()).inflate(R.layout.my_input_bar, null);
            et = mInputBarView.findViewById(R.id.ed_comment);
            mInputBarView.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!App.ISLOGIN(getActivity())) {
                        ToastShort("是不是忘了登录？(ฅ′ω`ฅ)");
                        return;
                    }
                    postComments(et.getText().toString());
                    et.setText("");
                }
            });
            getActivity().addContentView(mInputBarView, lp);
            doShowAnimation();
            isInputBarShow = true;
        }
    }

    private void postComments(String comment) {
        OkHttpUtils.post()
                .url(NetConfig.BASE_POSTCOMMENT_PLUS)
                .addHeader("Authorization", "Bearer " + App.getToken(getActivity()))
                .addParams("body", comment)
                .addParams("discussion_id", postID + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
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
                            getNewToken(comment);
                        } else {
                            printLog(response);
                            ToastShort("发布成功");
                            hideKeyboard();
                            handler.sendEmptyMessage(REFRESH_COMMENTLIST);
                        }
                    }
                });
    }

    /**
     * 获取新的Token
     */
    private void getNewToken(String comment) {
        OkHttpUtils.post()
                .url(NetConfig.BASE_GETNEWTOKEN_PLUS)
                .addHeader("Authorization", "Bearer " + App.getToken(getActivity()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("PostContentFragment getNewToken onError");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getInteger("code") != 20000) {
                            printLog("PostContentFragment getNewToken() onResponse获取Token失败,重新登陆");
                        } else {
                            App.setToken(getContext(), obj.getString("result"));
                            postComments(comment);
                        }
                    }
                });
    }

    /**
     * 移除输入框
     */
    private boolean isInputBarShow = false;

    public void removeMyInputBar() {
        if (mInputBarView != null && isInputBarShow) {
            doHideAnimation();
            isInputBarShow = false;
        }
    }

    /**
     * 从activity 获取帖子对象
     */
    private void getPostObj() {
        PostJsonString = getArguments().getString("PostJsonObject");
        postObj = JSON.parseObject(PostJsonString, Post.class);
        postID = postObj.getId();
    }

    /**
     * 根据acitivity获取的POSTID从服务器获取[回复对象含User对象]详情
     */
    private void getCommentListData() {
        OkHttpUtils.get()
                .url(NetConfig.BASE_POST_PLUS + "/" + postID)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastShort("网络出状况咯ヽ(#`Д´)ﾉ");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getInteger("code") != 20000) {
                            ToastShort("获取评论失败惹，再试试( • ̀ω•́ )✧");
                        } else {
                            Message msg = new Message();
                            msg.what = GET_COMMENTDATA;
                            Bundle bundle = new Bundle();
                            bundle.putString("CommentJsonObj", obj.getString("data"));
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                });
    }

    private void initCommentListData(String CommentJsonObj) {
        if (commentList == null)
            commentList = new ArrayList<>();
        JSONObject postObj = JSON.parseObject(CommentJsonObj);
        List<Comment> tempList = JSON.parseArray(postObj.getString("comments"), Comment.class);
        if (commentList.size() != tempList.size()) {
            for (int i = commentList.size(); i < tempList.size(); i++)
                commentList.add(tempList.get(i));
        }
        if (commentList.size() == 0) {
            tvCommentSuggest.setVisibility(View.VISIBLE);
            loadBottom.setVisibility(View.GONE);
        } else {
            rvComment.setVisibility(View.VISIBLE);
            tvCommentSuggest.setVisibility(View.GONE);
            loadBottom.setVisibility(View.VISIBLE);
        }
//        JSONObject obj = JSON.parseObject(PostJsonString);
//        commentList = JSON.parseArray(obj.getString("comments"),Comment.class);
    }

    /**
     * 初始化标题等等信息
     */
    private void initHead() {
        articleTitle.setText(postObj.getTitle());
        articleUsername.setText(postObj.getUser().getName());
        articlePostTime.setText(postObj.getCreated_at());
//        content.setText(postObj.getBody());
        RichText.fromMarkdown(postObj.getBody()).into(content);
        Picasso.get()
                .load(postObj.getUser().getAvatar())
                .placeholder(R.drawable.image_placeholder)
                .into(articleUserImage);
    }

    private void initCommentList() {
        adapter = new CommentAdapter(getContext(), commentList, postObj.getUser_id());
        adapter.setOnItemClickListener(listener);
        rvComment.setAdapter(adapter);
        rvComment.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        // 解决数据加载不全的问题
        rvComment.setNestedScrollingEnabled(false);
        rvComment.setHasFixedSize(true);
        //解决数据加载完成后，没有停留在顶部的问题
        rvComment.setFocusable(false);

        rvComment.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        // 调整draw缓存,加速recyclerview加载
        rvComment.setItemViewCacheSize(20);
        rvComment.setDrawingCacheEnabled(true);
        rvComment.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    Animator showAnimator, hideAnimator;

    private void doShowAnimation() {
        showAnimator = new ObjectAnimator().ofFloat(mInputBarView, "alpha", 0f, 1f);
        showAnimator.start();
    }

    private void doHideAnimation() {
        hideAnimator = new ObjectAnimator().ofFloat(mInputBarView, "alpha", 1f, 0f);
        hideAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mInputBarView.setVisibility(View.GONE);
                ((ViewGroup) mInputBarView.getParent()).removeView(mInputBarView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        hideAnimator.start();
    }

    private CommentAdapter.OnItemClickListener listener = (view, pos) -> {
        switch (view.getId()){
            case R.id.btn_reply_cz:
                if (et != null){
                    et.setText("***回复@" + commentList.get(pos).getUser().getName() + "：***\n");
                    et.setSelection(et.getText().length()-1);
                }
                break;
            case R.id.btn_more:
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()){
                        case R.id.tv_edit:
                            break;
                        case R.id.tv_copy:
                            String user = commentList.get(pos).getUser().getName();
                            String content = commentList.get(pos).getBody();
                            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            if (cm != null) {
                                cm.setPrimaryClip(ClipData.newPlainText(null, content));
                                ToastShort("已复制" + user + "的评论");
                            }
                            break;
                        case R.id.tv_remove:
                            break;
                    }
                    return true;
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_post_more, popup.getMenu());

                // 判断是不是本人
                popup.getMenu().removeItem(R.id.tv_edit);

                // 如果有管理权限,则显示删除
                popup.getMenu().removeGroup(R.id.menu_manege);

                popup.show();
                break;
        }
    };

    @Override
    public void ScrollToTop() {
        rvComment.scrollToPosition(0);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
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
        removeMyInputBar();
        unbinder.unbind();
    }

    @OnClick({R.id.share_panel, R.id.close_panel, R.id.btn_more, R.id.article_user_image, R.id.article_username})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_panel:
                String data = "这篇文章不错，分享给你们 【" + articleTitle.getText() + " \n链接地址：http://118.24.0.78/#/forum/" + postID + "】\n来自PlusClub客户端";
                IntentUtils.sharePost(getActivity(), data);
                break;
            case R.id.close_panel:
                PostActivity postActivity = (PostActivity) getActivity();
                postActivity.hidePanel();
                break;
            case R.id.btn_more:
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()){
                        case R.id.tv_edit:
                            break;
                        case R.id.tv_copy:
                            String user = articleUsername.getText().toString();
                            String s = content.getText().toString();
                            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            if (cm != null) {
                                cm.setPrimaryClip(ClipData.newPlainText(null, s));
                                ToastShort("已复制" + user + "的评论");
                            }
                            break;
                        case R.id.tv_remove:
                            break;
                    }
                    return true;
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_post_more, popup.getMenu());

                // 判断是不是本人
                popup.getMenu().removeItem(R.id.tv_edit);

                // 如果有管理权限,则显示删除
                popup.getMenu().removeGroup(R.id.menu_manege);

                popup.show();
                break;
            case R.id.article_user_image:
            case R.id.article_username:
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra("userid",postObj.getUser().getId());
                startActivity(intent);
                break;
        }
    }
}
