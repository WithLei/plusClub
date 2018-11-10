package com.android.renly.plusclub.Api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlusClubApi {
    // 更新Token
    @GET("refresh")
    Observable<ResponseBody>getNewToken(@Header("Authorization")String Authorization);

    // 获取新帖
    @GET("discussions")
    Observable<ResponseBody>getPostList(@Query("page") int page);

    // 获取回复帖子
    @GET("user/{id}/comments")
    Observable<ResponseBody> getReplyPostList(@Path("id") long id, @Query("page") int page);

    // 获取我的帖子
    @GET("user/{id}/discussions")
    Observable<ResponseBody>getMyPostList(@Header("Authorization")String Authorization, @Query("page") int page);

}
