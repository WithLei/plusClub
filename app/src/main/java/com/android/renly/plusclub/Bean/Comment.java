package com.android.renly.plusclub.Bean;

import com.android.renly.plusclub.Utils.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * 单条评论
 */

public class Comment {
    /**
     * 用户头像链接
     */
    private String avatorSrc;
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

    public Comment(String avatorSrc, String name, long floorCount, Date postTime, String content) {
        this.avatorSrc = avatorSrc;
        this.name = name;
        this.floorCount = floorCount;
        this.postTime = postTime;
        this.content = content;
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

    public String getAvatorSrc() {
        return avatorSrc;
    }

    public void setAvatorSrc(String avatorSrc) {
        this.avatorSrc = avatorSrc;
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

    /**
     * 获取从发布到现在的时间
     * @return
     */
    public String getFromNowOnTime(){
        long x =  System.currentTimeMillis() - postTime.getTime();
        if (x < 0) {
            return "error";
        }

        // 一分钟内
        x /= 60000;
        if (x < 1)
            return "刚刚";

        // 一小时内
        if (x/60 < 1)
            return x%60 + "分钟前";
        x /= 60;

        // 24小时内
        if (x/24 < 1)
            return x%24 + "小时前";
        x /= 24;

        // 一年内
        if (x/365 < 1)
            return x%365 + "天前";

        return x/365 + "年前";
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
}
