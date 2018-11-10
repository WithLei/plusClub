package com.android.renly.plusclub.Api;



import com.android.renly.plusclub.Api.Bean.Weather;
import com.android.renly.plusclub.Api.Bean.WeatherInfo;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherApi {
    // 获取天气信息
    @GET("{cityCode}.html")
    Observable<Weather> getWeather(@Path("cityCode")String cityCode);
}
