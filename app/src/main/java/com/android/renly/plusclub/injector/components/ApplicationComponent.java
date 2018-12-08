package com.android.renly.plusclub.injector.components;

import android.content.Context;

import com.android.renly.plusclub.injector.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Context getContext();

}
