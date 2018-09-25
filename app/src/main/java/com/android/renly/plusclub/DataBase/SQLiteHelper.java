package com.android.renly.plusclub.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "plusclub.db";

    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + MyDB.TABLE_READ_SCHEDULE + "("
                + "cid INT primary key,"
                + "crows INT NOT NULL,"
                + "cweekday INT NOT NULL,"
                + "courseName VARCHAR(30) NOT NULL,"
                + "courseTime VARCHAR(30) NOT NULL,"
                + "teacher VARCHAR(30) NOT NULL,"
                + "classRoom VARCHAR(30) NOT NULL,"
                + "startWeek INT NOT NULL,"
                + "endWeek INT NOT NULL,"
                + "create_time DATETIME NOT NULL"
                + ")";
        db.execSQL(sql);
        Log.e("DATABASE", "TABLE_READ_SCHEDULE数据表创建成功");
    }

    /**
     * 数据库更新函数，当数据库更新时会执行此函数
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + MyDB.TABLE_READ_SCHEDULE;
        db.execSQL(sql);

        this.onCreate(db);
        Log.e("DATABASE", "数据库已更新");
    }
}
