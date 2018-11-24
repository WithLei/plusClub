package com.android.renly.plusclub.Injector.components;

import com.android.renly.plusclub.Injector.PerFragment;
import com.android.renly.plusclub.Injector.modules.MineFragModule;
import com.android.renly.plusclub.Module.mine.MineFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = MineFragModule.class)
public interface MineFragComponent {
    void inject(MineFragment fragment);
}
