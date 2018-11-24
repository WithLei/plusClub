package com.android.renly.plusclub.Injector.modules;

import com.android.renly.plusclub.Injector.PerFragment;
import com.android.renly.plusclub.Module.hotnews.HotNewsFragPresenter;
import com.android.renly.plusclub.Module.hotnews.HotNewsFragment;

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
