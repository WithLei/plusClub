package com.android.renly.plusclub.Api.Bean;

/**
 * 单条评论
 */

public class Comment {
    /**
     * 评论ID
     */
    private long id;
    /**
     * 评论内容
     */
    private String body;
    /**
     * 用户id
     */
    private long user_id;
    /**
     * 帖子id
     */
    private long discussion_id;
    /**
     * 发布时间
     */
    private String created_at;
    /**
     * 更新时间
     */
    private String updated_at;
    /**
     * 用户名
     */
    private User user;
    /**
     * 楼数
     */
    private long floorCount;

    public Comment() {
        super();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(long floorCount) {
        this.floorCount = floorCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getDiscussion_id() {
        return discussion_id;
    }

    public void setDiscussion_id(long discussion_id) {
        this.discussion_id = discussion_id;
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
}
