package com.android.renly.plusclub.Api.Api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlusClubApi {
    // 获取头像
    @GET("get_user_details")
    Observable<ResponseBody>getUserAvatar(@Header("Authorization")String Authorization);

    // 登陆操作
    @POST("login")
    Observable<ResponseBody>doLogin(@Query("email")String email, @Query("password")String password);

    // 获取回复帖子
    @GET("user/{id}/comments")
    Observable<ResponseBody> getReplyPostList(@Path("id") long id, @Query("page") int page);

    // 获取我的帖子
    @GET("user/{id}/discussions")
    Observable<ResponseBody>getMyPostList(@Header("Authorization")String Authorization, @Query("page") int page);

    // 获取用户个人信息(个人可见)
    @GET("get_user_details")
    Observable<ResponseBody>getUserDetails(@Header("Authorization")String Authorization);

    // 获取帖子
    @POST("discussions/{id}")
    Observable<ResponseBody>getPost(@Path("id") long id);

    // 发送帖子
    @POST("discussions")
    Observable<ResponseBody>doPost(@Header("Authorization") String Authorization,
                                   @Query("title") String title,
                                   @Query("body") String body,
                                   @Query("categories") String categories);

    // 获取帖子详情列表，根据activity获取的POSTID从服务器获取[回复对象含User对象]详情
    @GET("discussions/{id}")
    Observable<ResponseBody>getCommentListData(@Path("id") long id);

    // 发表评论
    @POST("comments")
    Observable<ResponseBody>postComment(@Header("Authorization") String Authorization,
                                        @Query("body") String body,
                                        @Query("discussion_id") long discussion_id);
}
