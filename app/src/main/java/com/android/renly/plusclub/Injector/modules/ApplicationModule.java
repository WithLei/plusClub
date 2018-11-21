package com.android.renly.plusclub.Injector.modules;

import android.content.Context;

import com.android.renly.plusclub.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final App mApplication;

    public ApplicationModule(App application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }
}
