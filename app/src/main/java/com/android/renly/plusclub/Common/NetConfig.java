package com.android.renly.plusclub.Common;

public class NetConfig {
    // VPN 登陆官网
    public static final String vpnSigninURL = "https://webvpn.lsu.edu.cn/users/sign_in";

    // 教务系统登陆基地址 - 外/内网都可登陆
    public static final String BASE_EDU_RS = "https://jwgl.webvpn.lsu.edu.cn/default2.aspx";

    // 教务系统登陆基地址 - 内网登陆
    public static final String BASE_EDU_IN = "https://jwgl.webvpn.lsu.edu.cn/default2.aspx";

    // 教务系统主页基地址 - 外/内网都可登陆
    public static final String BASE_EDU_HOST_ME = "https://jwgl.webvpn.lsu.edu.cn/xs_main.aspx?xh=";

    // 验证码获取
    public static final String CHECKIMG_URL = "https://jwgl.webvpn.lsu.edu.cn/CheckCode.aspx";

    // 二次get请求登出 URL
    public static final String GET_LOGOUT_URL = "https://jwgl.webvpn.lsu.edu.cn/logout.aspx";
}
