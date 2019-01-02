package com.android.renly.plusclub.module.post.postcontent.main;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.plusclub.module.post.postlist.PostsActivity;
import com.android.renly.plusclub.module.user.userdetail.UserDetailActivity;
import com.android.renly.plusclub.adapter.CommentAdapter;
import com.android.renly.plusclub.api.RetrofitService;
import com.android.renly.plusclub.App;
import com.android.renly.plusclub.api.bean.Comment;
import com.android.renly.plusclub.api.bean.Post;
import com.android.renly.plusclub.module.base.BaseActivity;
import com.android.renly.plusclub.module.base.BaseFragment;
import com.android.renly.plusclub.R;
import com.android.renly.plusclub.widget.CircleImageView;
import com.android.renly.plusclub.utils.IntentUtils;
import com.android.renly.plusclub.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.zzhoujay.richtext.RichText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.android.renly.plusclub.utils.toast.ToastUtils.*;

public class PostFragment extends BaseFragment {
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
    @BindView(R.id.tv_comment_suggest)
    TextView tvCommentSuggest;
    @BindView(R.id.load_bottom)
    LinearLayout loadBottom;
    @BindView(R.id.close_panel)
    ImageView closePanel;

    private List<Comment> commentList;
    private Post postObj;
    private long postID;
    // 输入框
    private View mInputBarView;
    private CommentAdapter adapter = null;

    private String from = "";

    @Override
    public int getLayoutid() {
        return R.layout.fragment_post;
    }

    @Override
    protected void initData(Context content) {
        getPostObj();
        getCommentListData();
        initView();
    }

    @Override
    protected void initView() {
        initHead();
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
            mInputBarView.findViewById(R.id.btn_send).setOnClickListener(view -> {
                if (!App.ISLOGIN()) {
                    ToastShort("是不是忘了登录？(ฅ′ω`ฅ)");
                    return;
                }
                if (!TextUtils.isEmpty(et.getText().toString())) {
                    postComments(et.getText().toString());
                    et.setText("");
                } else
                    ToastShort("回复内容不能为空喔(ฅ′ω`ฅ)");
            });
            getActivity().addContentView(mInputBarView, lp);
            doShowAnimation();
            isInputBarShow = true;
        }
    }

    @SuppressLint("CheckResult")
    private void postComments(String comment) {
        RetrofitService.doPostComment(comment + StringUtils.getTextTail(), postID)
                .subscribe(responseBody -> {
                    String response = responseBody.string();
                    if (!response.contains("code")) {
                        ToastNetWorkError();
                        return;
                    }
                    JSONObject jsonObject = JSON.parseObject(response);
                    if (jsonObject.getInteger("code") == 50011) {
                        getNewToken(comment);
                    } else {
                        ToastShort("发布成功");
                        ((BaseActivity) super.mContent).hideKeyBoard();
                        getCommentListData();
                    }
                }, throwable -> ToastNetWorkError());
    }

    /**
     * 获取新的Token
     */
    @SuppressLint("CheckResult")
    private void getNewToken(String comment) {
        RetrofitService.getNewToken()
                .subscribe(s -> postComments(comment));
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
        String PostJsonString = getArguments().getString("PostJsonObject");
        from = getArguments().getString("from");
        postObj = JSON.parseObject(PostJsonString, Post.class);
        postID = postObj.getId();
    }

    /**
     * 根据activity获取的POSTID从服务器获取[回复对象含User对象]详情
     */
    @SuppressLint("CheckResult")
    private void getCommentListData() {
        RetrofitService.getCommentListData(postID)
                .subscribe(responseBody -> {
                    String response = responseBody.string();
                    JSONObject obj = JSON.parseObject(response);
                    if (obj.getInteger("code") != 20000) {
                        ToastShort("获取评论失败惹，再试试( • ̀ω•́ )✧");
                        return;
                    }
                    initCommentListData(obj.getString("data"));
                    if (adapter == null)
                        initCommentList();
                    else
                        adapter.notifyDataSetChanged();
                }, throwable -> ToastNetWorkError());
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
    }

    /**
     * 初始化标题等等信息
     */
    private void initHead() {
        if (from.equals("PostActivity")) {
            closePanel.setVisibility(View.GONE);
            initMyInputBar();
        }
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
        switch (view.getId()) {
            case R.id.btn_reply_cz:
                if (et != null) {
                    et.setText("***回复@" + commentList.get(pos).getUser().getName() + "：***\n");
                    et.setSelection(et.getText().length() - 1);
                }
                break;
            case R.id.btn_more:
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
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

    @Override
    protected void initInjector() {

    }

    @Override
    public void onDestroyView() {
        removeMyInputBar();
        super.onDestroyView();
    }

    @OnClick({R.id.share_panel, R.id.close_panel, R.id.btn_more, R.id.article_user_image, R.id.article_username})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_panel:
                String data = "这篇文章不错，分享给你们 【" + articleTitle.getText() + " \n链接地址：http://118.24.0.78/#/forum/" + postID + "】\n来自PlusClub客户端";
                IntentUtils.sharePost(getActivity(), data);
                break;
            case R.id.close_panel:
                PostsActivity postsActivity = (PostsActivity) getActivity();
                postsActivity.hidePanel();
                break;
            case R.id.btn_more:
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
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
                intent.putExtra("userid", postObj.getUser().getId());
                startActivity(intent);
                break;
        }
    }
}
