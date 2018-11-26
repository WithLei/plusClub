package com.android.renly.plusclub.api;

import com.android.renly.plusclub.api.api.PlusClubApi;
import com.android.renly.plusclub.api.api.WeatherApi;

public class ApiFactory {
    protected static final Object monitor = new Object();
    static PlusClubApi plusClubApi = null;
    static WeatherApi weatherApi = null;

}
