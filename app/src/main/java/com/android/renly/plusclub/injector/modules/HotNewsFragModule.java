package com.android.renly.plusclub.injector.modules;

import com.android.renly.plusclub.injector.PerFragment;
import com.android.renly.plusclub.module.hotnews.HotNewsFragPresenter;
import com.android.renly.plusclub.module.hotnews.HotNewsFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class HotNewsFragModule {
    private final HotNewsFragment mView;

    public HotNewsFragModule(HotNewsFragment mView) {
        this.mView = mView;
    }

    @PerFragment
    @Provides
    public HotNewsFragPresenter provideHotNewsFragPresenter() {
        return new HotNewsFragPresenter(mView);
    }
}
