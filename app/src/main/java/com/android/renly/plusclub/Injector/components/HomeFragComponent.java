package com.android.renly.plusclub.injector.components;

import com.android.renly.plusclub.injector.PerFragment;
import com.android.renly.plusclub.injector.modules.HomeFragModule;
import com.android.renly.plusclub.module.home.HomeFragment;

import dagger.Component;

/**
 * HomeFragment
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = HomeFragModule.class)
public interface HomeFragComponent {
    void inject(HomeFragment fragment);
}
