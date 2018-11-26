package com.android.renly.plusclub.local.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.renly.plusclub.api.bean.Course;
import com.android.renly.plusclub.api.bean.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyDB {
    private Context context;
    /**
     * 浏览 课程表 表
     */
    static final String TABLE_READ_SCHEDULE = "edu_schedule_list";
    /**
     * 用户本人
     */
    static final String TABLE_USER_INFO = "user_info";

    private SQLiteDatabase db = null;

    public MyDB(Context context) {
        this.context = context;
        this.db = new SQLiteHelper(context).getWritableDatabase();
    }

    private SQLiteDatabase getDb(){
        if (this.db == null || !this.db.isOpen()) {
            this.db = new SQLiteHelper(context).getWritableDatabase();
        }
        return this.db;
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());

        return format.format(curDate);
    }

    // 处理单个点击事件 判断是更新还是插入
    public void handSingleReadSchedule(Course course){
        if (isCourseExist(course.getCid())){
            updateSchedule(course);
        }else {
            insertSchedule(course);
        }
    }

    public void handSingleReadSchedule(User user){
        if (isUserExist(user.getId())){
            updateUser(user);
        }else{
            insertUser(user);
        }
    }

    // 获取课程表的课程信息

    public List<Course>getSchedule() {
        getDb();
        List<Course>datas = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_READ_SCHEDULE;
        Cursor result = this.db.rawQuery(sql, null);
        for (result.moveToFirst();!result.isAfterLast(); result.moveToNext()) {
            int cid = result.getInt(0);
            int rows = result.getInt(1);
            int weekday = result.getInt(2);
            String courseName = result.getString(3);
            String courseTime = result.getString(4);
            String teacher = result.getString(5);
            String classRoom = result.getString(6);
            int startWeek = result.getInt(7);
            int endWeek = result.getInt(8);
            int sd_week = result.getInt(9);
            String create_time = result.getString(10);
            datas.add(new Course(rows,weekday,courseName,courseTime,teacher,classRoom,startWeek,endWeek,create_time,sd_week,cid));
        }
        result.close();
        this.db.close();
        return datas;
    }

    // 获取用户本地缓存头像
    public String getUserAvatarPath(long uid) {
        getDb();
        User user = new User();
        String avatarPath = "";
        String sql = "SELECT * FROM " + TABLE_USER_INFO;
        Cursor result = this.db.rawQuery(sql,null);
        for (result.moveToFirst();!result.isAfterLast();result.moveToNext()) {
            if (result.getLong(0) == uid){
                avatarPath = result.getString(10);
                break;
            }
        }
        result.close();
        this.db.close();
        return avatarPath;
    }

    // 获取用户信息
    public User getUser(long uid) {
        getDb();
        User user = new User();
        String sql = "SELECT * FROM " + TABLE_USER_INFO;
        Cursor result = this.db.rawQuery(sql,null);
        for (result.moveToFirst();!result.isAfterLast();result.moveToNext()) {
            if (result.getLong(0) == uid){
                user.setName(result.getString(1));
                user.setEmail(result.getString(2));
                user.setStudentid(result.getString(3));
                user.setGrades(result.getString(4));
                user.setPhone(result.getString(5));
                user.setCreated_at(result.getString(6));
                user.setUpdated_at(result.getString(7));
                user.setRole(result.getString(8));
                user.setAvatar(result.getString(9));
                user.setAvatarPath(result.getString(10));
                break;
            }
        }
        result.close();
        this.db.close();
        return user;
    }


    // 插入操作
    private void insertSchedule(Course course) {
        getDb();
        String sql = "INSERT INTO " + TABLE_READ_SCHEDULE + " (cid,crows,cweekday,courseName,courseTime,teacher,classRoom,startWeek,endWeek,sd_week,create_time)"
                 + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        String create_time = getTime();
        Object args[] = new Object[]{course.getCid(),course.getRows(),course.getWeekday(),
                course.getCourseName(),course.getCourseTime(),course.getTeacher(),
                course.getClassRoom(),course.getStartWeek(),course.getEndWeek(),course.getSd_week(),create_time};
        this.db.execSQL(sql, args);
        this.db.close();
    }

    private void insertUser(User user) {
        getDb();
        String sql = "INSERT INTO " + TABLE_USER_INFO + " (uid,uname,uemail,ustudentid,ugrades,uphone,ucreated,uupdated,urole,uavatarsrc,uavatarpath)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        Object args[] = new Object[]{user.getId(), user.getName(), user.getEmail(), user.getStudentid(),
        user.getGrades(), user.getPhone(), user.getCreated_at(),user.getUpdated_at(),
        user.getAvatar(),user.getAvatarPath()};
        this.db.execSQL(sql,args);
        this.db.close();
    }

    // 更新操作
    private void updateSchedule(Course course) {
        getDb();
        String create_time = getTime();
        String sql = "UPDATE " + TABLE_READ_SCHEDULE + " SET crows=?,cweekday=?,courseName=?,courseTime=?,teacher=?,classRoom=?,startWeek=?,endWeek=?,sd_week=?,create_time=? WHERE cid=?";
        Object args[] = new Object[]{course.getRows(),course.getWeekday(),course.getCourseName(),
                course.getCourseTime(),course.getTeacher(),course.getClassRoom(),
                course.getStartWeek(),course.getEndWeek(),course.getSd_week(),create_time,course.getCid()};
        this.db.execSQL(sql, args);
        this.db.close();
    }

    private void updateUser(User user){
        getDb();
        String sql = "UPDATE " + TABLE_USER_INFO + " SET uid=?,uname=?,uemail=?,ustudentid=?,ugrades=?,uphone=?,ucreated=?,uupdated=?,urole=?,uavatarsrc=?,uavatarpath=? WHERE uid=?";
        Object args[] = new Object[]{user.getId(),user.getName(),user.getEmail(),user.getStudentid(),
                user.getGrades(),user.getPhone(),user.getCreated_at(),user.getUpdated_at(),
                user.getRole(),user.getAvatar(),user.getAvatarPath(),user.getId()};
        this.db.execSQL(sql,args);
        this.db.close();
    }

    private boolean isCourseExist(int cid) {
        getDb();
        String sql = "SELECT cid FROM " + TABLE_READ_SCHEDULE + " where cid = ?";
        String args[] = new String[]{String.valueOf(cid)};
        Cursor result = db.rawQuery(sql, args);
        int count = result.getCount();
        result.close();
        this.db.close();
        return count != 0;
    }

    public boolean isUserExist(long uid){
        getDb();
        String sql = "SELECT uid FROM " + TABLE_USER_INFO + " where uid = ?";
        String args[] = new String[]{String.valueOf(uid)};
        Cursor result = db.rawQuery(sql, args);
        int count = result.getCount();
        result.close();
        this.db.close();
        return count != 0;
    }

    public boolean isScheduleExist(){
        getDb();
        String sql = "SELECT cid FROM " + TABLE_READ_SCHEDULE;
        Cursor result = db.rawQuery(sql,null);
        int count = result.getCount();
        Log.e("DATABASE","count == " + count);
        this.db.close();
        return count != 0;
    }

    public void clearSchedule(){
        getDb();
        String sql = "DELETE FROM " + TABLE_READ_SCHEDULE;
        this.db.execSQL(sql);
        this.db.close();
    }
    
}
