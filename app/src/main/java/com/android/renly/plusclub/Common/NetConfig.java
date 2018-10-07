package com.android.renly.plusclub.Common;

public class NetConfig {
    // VPN 登陆官网
    public static final String vpnSigninURL = "https://webvpn.lsu.edu.cn/users/sign_in";

    // 教务系统登陆基地址 - 外/内网都可登陆
    public static final String BASE_EDU_PLUS = "https://jwgl.webvpn.lsu.edu.cn/default2.aspx";

    // 教务系统登陆基地址 - 内网登陆
    public static final String BASE_EDU_IN = "http://jwgl.lsu.edu.cn/default2.aspx";

    // 教务系统主页基地址 - 外/内网
    public static final String BASE_EDU_HOST_ME = "https://jwgl.webvpn.lsu.edu.cn/xs_main.aspx?xh=";

    // 验证码获取 - 外/内网
    public static final String CHECKIMG_URL_RS = "https://jwgl.webvpn.lsu.edu.cn/CheckCode.aspx";

    // 验证码获取 - 内网
    public static final String CHECKING_URL_IN = "http://jwgl.lsu.edu.cn/CheckCode.aspx";

    // 二次get请求登出 URL - 外/内网
    public static final String GET_LOGOUT_URL_RS = "https://jwgl.webvpn.lsu.edu.cn/logout.aspx";

    // 二次get请求登出 URL - 内网
    public static final String GET_LOGOUT_URL_IN = "https://jwgl.lsu.edu.cn/logout.aspx";

    // 查询各种信息基地址 - 外/内网
    public static final String BASE_EDU_GETINFO_RS = "https://jwgl.webvpn.lsu.edu.cn/xskbcx.aspx";

    // 查询各种信息基地址 - 内网
    public static final String BASE_EDU_GETINFO_IN = "https://jwgl.lsu.edu.cn/xskbcx.aspx";

    // 信息门户登陆主页 - 外/内网
    public static final String BASE_ECARD_PLUS = "https://ca.webvpn.lsu.edu.cn/zfca/login";

    // 信息门户登陆主页 - 内网
    public static final String BASE_ECARD_IN = "http://ca.lsu.edu.cn/zfca/login";

    // 信息门户登陆请求 - 外/内网
    public static final String ECARD_LOGIN_PLUS = "http://ca.lsu.edu.cn/zfca/login;jsessionid=BB3E151306331D2AB17C59943A0B1D39?service=http%3A%2F%2Fportal.lsu.edu.cn%2Fportal.do";

}
