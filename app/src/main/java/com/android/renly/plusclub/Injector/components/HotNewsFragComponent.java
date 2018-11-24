package com.android.renly.plusclub.Injector.components;

import com.android.renly.plusclub.Injector.PerFragment;
import com.android.renly.plusclub.Injector.modules.HotNewsFragModule;
import com.android.renly.plusclub.Module.hotnews.HotNewsFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = HotNewsFragModule.class)
public interface HotNewsFragComponent {
    void inject(HotNewsFragment fragment);
}
