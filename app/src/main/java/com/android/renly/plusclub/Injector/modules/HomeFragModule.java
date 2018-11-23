package com.android.renly.plusclub.Injector.modules;

import com.android.renly.plusclub.Injector.PerFragment;
import com.android.renly.plusclub.Module.base.BasePresenter;
import com.android.renly.plusclub.Module.home.HomeFragPresenter;
import com.android.renly.plusclub.Module.home.HomeFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeFragModule {
    private final HomeFragment mView;
    private final String cityCode;

    public HomeFragModule(HomeFragment mView, String cityCode) {
        this.mView = mView;
        this.cityCode = cityCode;
    }

    @PerFragment
    @Provides
    public HomeFragPresenter  provideHomeFragPresenter() {
        return new HomeFragPresenter(cityCode, mView);
    }
}
