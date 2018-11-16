package com.android.renly.plusclub.Api.Api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GithubApi {
    // 获取版本信息
    @GET
    Observable<ResponseBody> getVersion(@Url String url);
}
