package com.android.renly.plusclub.Api.Api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlusClubApi {
    // 更新Token
    @POST("refresh")
    Observable<ResponseBody>getNewToken(@Header("Authorization")String Authorization);

    // 获取头像
    @GET("get_user_details")
    Observable<ResponseBody>getUserAvatar(@Header("Authorization")String Authorization);

    // 登陆操作
    @POST("login")
    Observable<ResponseBody>doLogin(@Query("email")String email, @Query("password")String password);

    // 获取新帖
    @GET("discussions")
    Observable<ResponseBody>getPostList(@Query("page") int page);

    // 获取回复帖子
    @GET("user/{id}/comments")
    Observable<ResponseBody> getReplyPostList(@Path("id") long id, @Query("page") int page);

    // 获取我的帖子
    @GET("user/{id}/discussions")
    Observable<ResponseBody>getMyPostList(@Header("Authorization")String Authorization, @Query("page") int page);

    // 获取用户个人信息(个人可见)
    @GET("get_user_details")
    Observable<ResponseBody>getUserDetails(@Header("Authorization")String Authorization);
}
