package com.android.renly.plusclub.Api.Bean;

import java.util.List;

/**
 * 帖子
 */
public class Post {
    /**
     * 帖子ID
     */
    private long id;
    /**
     * 帖子标题
     */
    private String title;
    /**
     * 发布人
     * eg:
     * &#xf2c0; 张三
     */
    private String name;
    /**
     * 头像src - 网络路径
     */
    private String avatar;
    /**
     * 头像path
     */
    private String avatarPath;
    /**
     * 帖子内容
     */
    private String body;

    private long user_id;
    private long last_user_id;
    /**
     * 发布时间
     * eg:
     * &#xf017; 2015-1-1 19:20:15
     */
    private String created_at;
    /**
     * 更新时间
     */
    private String updated_at;
    /**
     * 分类
     */
    private String categories;
    /**
     * 评论人数
     */
    private long comments_total;
    /**
     * 发布者
     */
    private User user;
    /**
     * 最后回复者
     */
    private User last_user;
    /**
     * 回复
     */
    private List<Comment> comments;
    /**
     * 浏览量
     */
    private long pageViewsCount;

    public Post() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getPageViewsCount() {
        return pageViewsCount;
    }

    public void setPageViewsCount(long pageViewsCount) {
        this.pageViewsCount = pageViewsCount;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getLast_user_id() {
        return last_user_id;
    }

    public void setLast_user_id(long last_user_id) {
        this.last_user_id = last_user_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public User getLast_user() {
        return last_user;
    }

    public void setLast_user(User last_user) {
        this.last_user = last_user;
    }

    public long getComments_total() {
        return comments_total;
    }

    public void setComments_total(long comments_total) {
        this.comments_total = comments_total;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Post[ title = " + title
                + ",name = " + name
                + ",body = " + body
                + "]";
    }
}
