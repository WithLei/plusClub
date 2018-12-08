package com.android.renly.plusclub.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.android.renly.plusclub.App;
import com.android.renly.plusclub.local.DataBase.SQLiteHelper;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by free2 on 16-6-20.
 * 计算和清除缓存
 */
public class DataManager {

    public static String getTotalCacheSize(Context context) {
        long cacheSize = 0;
        try {
            cacheSize = getFolderSize(context.getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 这里就已经很大了
        Log.e("print","cachesize 1. == " + cacheSize);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                cacheSize += getFolderSize(context.getExternalCacheDir());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清除本应用所有的数据
     */
    public static void cleanApplicationData(Context context, String... filepath) {
        SharedPreferences perUserInfo = context.getSharedPreferences(App.MY_SP_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = perUserInfo.edit();
        boolean isRemember = perUserInfo.getBoolean(App.IS_REMEBER_PWD_USER, false);
        String userEmail = perUserInfo.getString(App.USER_EMAIL_KEY, "");
        String userPass = perUserInfo.getString(App.USER_PWD_KEY, "");

        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanSharedPreference(context);
        cleanFiles(context);

        cleanDatabaseByName(context, SQLiteHelper.DATABASE_NAME);

        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }

        // 先清理shp数据，然后把之前保存的用户名和密码保存
        editor.clear();
        editor.apply();
        editor.putBoolean(App.IS_REMEBER_PWD_USER, isRemember);
        editor.putString(App.USER_EMAIL_KEY, userEmail);
        editor.putString(App.USER_PWD_KEY, userPass);
        editor.apply();
    }

    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();

            if (fileList == null) {
                return 0;
            }
            for (File aFileList : fileList) {
                // 如果下面还有文件
                if (aFileList == null) {
                    continue;
                }
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     */
    private static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


    /***
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     */
    private static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /***
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     */
    private static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库
     */
    private static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files
     */
    private static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    private static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除
     */
    private static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /***
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                Log.e("file",item.getName());
                item.delete();
            }
        }
    }
}
