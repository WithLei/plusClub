package com.android.renly.plusclub.Injector.components;

import com.android.renly.plusclub.Injector.PerFragment;
import com.android.renly.plusclub.Injector.modules.HomeFragModule;
import com.android.renly.plusclub.Module.home.HomeFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = HomeFragModule.class)
public interface MineFragComponent {
    void inject(HomeFragment fragment);
}
