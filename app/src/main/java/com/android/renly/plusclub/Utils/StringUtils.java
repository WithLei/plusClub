package com.android.renly.plusclub.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * 检查密码中是否含有数字和字母
     *
     * @param pwd
     * @return
     */
    public static boolean checkSecurity(String pwd) {
        String p = "", p2 = "", p3 = "";
        Pattern pa1 = Pattern.compile("\\d+");
        Pattern pa2 = Pattern.compile("[a-z]+");
        Pattern pa3 = Pattern.compile("[A-Z]+");
        Matcher m1 = pa1.matcher(pwd);
        Matcher m2 = pa2.matcher(pwd);
        Matcher m3 = pa3.matcher(pwd);
        if (m1.find()) {
            p = m1.group();
        } else if (m2.find()) {
            p2 = m2.group();
        } else if (m3.find()) {
            p3 = m3.group();
        }

        return !(p.length() == pwd.length() || p2.length() == pwd.length() || p3.length() == pwd.length());
    }
}
