package com.android.renly.plusclub.Bean;

import com.android.renly.plusclub.Utils.DateUtils;

import java.text.ParseException;
import java.util.Date;

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
    private String user_id;
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
    private String name;
    /**
     * 楼数
     */
    private long floorCount;
    /**
     * 发布时间
     */
    private Date postTime;
    /**
     * 评论内容
     */
    private String content;

    public Comment() {
        super();
    }

    /**
     * 测试用构造器
     */
    public Comment(String name,String postTime, String content) {
        this.name = name;
        this.floorCount = floorCount;
        try {
            this.postTime = DateUtils.stringToDate(postTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFloorCount() {
        return floorCount;
    }

    public void setFloorCount(long floorCount) {
        this.floorCount = floorCount;
    }

    public Date getPostTime() {
        return postTime;
    }

    public String getPostTimeString(){
        return DateUtils.dateToString(postTime);
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public void setPostTime(String postTime) {
        try {
            this.postTime = DateUtils.stringToDate(postTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
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
