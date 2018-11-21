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

    public HomeFragModule(HomeFragment mView) {
        this.mView = mView;
    }

    @PerFragment
    @Provides
    public BasePresenter provideHomeFragPresenter(String cityCode) {
        return new HomeFragPresenter(cityCode, mView);
    }
}
