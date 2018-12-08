package com.android.renly.plusclub.injector.components;

import com.android.renly.plusclub.injector.PerFragment;
import com.android.renly.plusclub.injector.modules.HotNewsFragModule;
import com.android.renly.plusclub.module.hotnews.HotNewsFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = HotNewsFragModule.class)
public interface HotNewsFragComponent {
    void inject(HotNewsFragment fragment);
}
