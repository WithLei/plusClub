package com.android.renly.plusclub.injector.components;

import com.android.renly.plusclub.injector.PerFragment;
import com.android.renly.plusclub.injector.modules.MineFragModule;
import com.android.renly.plusclub.module.mine.MineFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = MineFragModule.class)
public interface MineFragComponent {
    void inject(MineFragment fragment);
}
