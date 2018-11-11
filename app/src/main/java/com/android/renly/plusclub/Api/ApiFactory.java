package com.android.renly.plusclub.Api;

import com.android.renly.plusclub.Api.Api.PlusClubApi;
import com.android.renly.plusclub.Api.Api.WeatherApi;

public class ApiFactory {
    protected static final Object monitor = new Object();
    static PlusClubApi plusClubApi = null;
    static WeatherApi weatherApi = null;

}
