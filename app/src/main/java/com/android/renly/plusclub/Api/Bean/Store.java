package com.android.renly.plusclub.api.bean;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.renly.plusclub.App;

public class Store {
    private SharedPreferences mStore;

    private Store(){
        mStore = App.getContext().getSharedPreferences(App.MY_SP_NAME, Context.MODE_PRIVATE);
    }

    public static Store getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final Store INSTANCE = new Store();
    }

    public void setToken(String token) {
        mStore.edit().putString(App.USER_TOKEN_KEY, token).apply();
    }

    public String getToken() {
        return mStore.getString(App.USER_TOKEN_KEY, "");
    }
}
