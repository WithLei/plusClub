package com.android.renly.plusclub.injector.components;

import com.android.renly.plusclub.module.schedule.ScheduleFragment;
import com.android.renly.plusclub.injector.PerFragment;
import com.android.renly.plusclub.injector.modules.ScheduleFragModule;

import dagger.Component;

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = ScheduleFragModule.class)
public interface ScheduleFragComponent {
    void inject(ScheduleFragment fragment);
}
