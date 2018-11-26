package com.android.renly.plusclub.utils;

public class NetConfig {
    public static final String User_Agent_KEY = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";
    // VPN 登陆官网
    public static final String vpnSigninURL = "https://webvpn.lsu.edu.cn/users/sign_in";

    // 教务系统登陆基地址 - 外/内网都可登陆
    public static final String BASE_EDU_PLUS = "https://jwgl.webvpn.lsu.edu.cn/default2.aspx";

    // 教务系统登陆基地址 - 内网登陆
    public static final String BASE_EDU_IN = "http://jwgl.lsu.edu.cn/default2.aspx";

    // 教务系统主页基地址 - 外/内网
    public static final String BASE_EDU_HOST_ME = "https://jwgl.webvpn.lsu.edu.cn/xs_main.aspx?xh=";

    // 验证码获取 - 外/内网
    public static final String CHECKIMG_URL_EDU = "https://jwgl.webvpn.lsu.edu.cn/CheckCode.aspx";

    // 验证码获取 - 内网
    public static final String CHECKING_URL_IN = "http://jwgl.lsu.edu.cn/CheckCode.aspx";

    // 二次get请求登出 URL - 外/内网
    public static final String GET_LOGOUT_URL_EDU = "https://jwgl.webvpn.lsu.edu.cn/logout.aspx";

    // 二次get请求登出 URL - 内网
    public static final String GET_LOGOUT_URL_IN = "https://jwgl.lsu.edu.cn/logout.aspx";

    // 查询各种信息基地址 - 外/内网
    public static final String BASE_EDU_GETINFO_EDU = "https://jwgl.webvpn.lsu.edu.cn/xskbcx.aspx";

    // 查询各种信息基地址 - 内网
    public static final String BASE_EDU_GETINFO_IN = "https://jwgl.lsu.edu.cn/xskbcx.aspx";

    // 信息门户登陆主页 - 外/内网
    public static final String BASE_ECARD_PLUS = "https://ca.webvpn.lsu.edu.cn/zfca/login";

    // 信息门户登陆主页 - 内网
    public static final String BASE_ECARD_IN = "http://ca.lsu.edu.cn/zfca/login";

    // 信息门户登陆请求 - 外/内网
    public static final String ECARD_LOGIN_PLUS = "https://ca.webvpn.lsu.edu.cn/zfca/login";

    // 获取天气api
    public static final String GET_WEATHER_URL = "http://www.weather.com.cn/data/cityinfo/";

    // PlusClub api基地址
    public static final String BASE_PLUSCLUB = "http://118.24.0.78/api/";

    // plusClub 项目地址
    public static final String PLUSCLUB_ITEM = "https://github.com/WithLei/plusClub";

    // 获取PlusClub realease api
    public static final String GITHUB_GET_RELEASE = "https://api.github.com/repos/WithLei/plusClub/releases/latest";

    // PlusClub 登陆
    public static final String BASE_LOGIN_PLUS = BASE_PLUSCLUB + "login";

    // PlusClub 注册
    public static final String BASE_REGISTER_PLUS = BASE_PLUSCLUB + "register";

    // PlusClub 获取用户个人信息（个人可见）
    public static final String BASE_USERDETAIL_PLUS = BASE_PLUSCLUB + "get_user_details";

    // PlusClub 获取用户信息（所有人可见）
    public static final String BASE_USER_PLUS = BASE_PLUSCLUB + "user/";

    /**
     * PlusClub 获取所有帖子 使用：url + "?page=" + page  eg:host/discussions?page=1
     * PlusClub 获取帖子回复详情 使用：url + "/"+ Post.getId()  eg:host/discussions/68
     * PlusClub 发送帖子 使用：url + "?" + title + body + categories#分类
     */
    public static final String BASE_POST_PLUS = BASE_PLUSCLUB + "discussions";

    /**
     * 获取新的Token
     */
    public static final String BASE_GETNEWTOKEN_PLUS = BASE_PLUSCLUB + "refresh";

    /**
     * 修改密码
     */
    public static final String BASE_RESETPWD_PLUS = BASE_USER_PLUS + "reset";

    /**
     * 发表评论
     */
    public static final String BASE_POSTCOMMENT_PLUS = BASE_PLUSCLUB + "comments";

}
