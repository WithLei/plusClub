package com.android.renly.plusclub.Bean;

/**
 * 帖子
 */
public class Post {
    /**
     * 帖子ID
     */
    private long p_id;
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
    private String avatorSrc;
    /**
     * 头像path
     */
    private String avatorPath;
    /**
     * 发布时间
     * eg:
     * &#xf017; 2015-1-1 19:20:15
     */
    private String postTime;
    /**
     * 浏览量
     */
    private long pageViewsCount;
    /**
     * 评论量
     */
    private long commentsCount;

    public Post(long p_id, String title, String name, String postTime, long pageViewsCount, long commentsCount) {
        this.p_id = p_id;
        this.title = title;
        this.name = name;
        this.postTime = postTime;
        this.pageViewsCount = pageViewsCount;
        this.commentsCount = commentsCount;
    }

    public long getP_id() {
        return p_id;
    }

    public void setP_id(long p_id) {
        this.p_id = p_id;
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

    public String getAvatorSrc() {
        return avatorSrc;
    }

    public void setAvatorSrc(String avatorSrc) {
        this.avatorSrc = avatorSrc;
    }

    public String getAvatorPath() {
        return avatorPath;
    }

    public void setAvatorPath(String avatorPath) {
        this.avatorPath = avatorPath;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public long getPageViewsCount() {
        return pageViewsCount;
    }

    public void setPageViewsCount(long pageViewsCount) {
        this.pageViewsCount = pageViewsCount;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }
}
