package com.android.renly.plusclub.Bean;


public class SimplePost {
    /**
     * 帖子ID
     */
    private long id;
    /**
     * 帖子标题
     */
    private String discussion_title;

    private String title;
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

    public SimplePost() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDiscussion_title() {
        return discussion_title;
    }

    public void setDiscussion_title(String discussion_title) {
        this.discussion_title = discussion_title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
