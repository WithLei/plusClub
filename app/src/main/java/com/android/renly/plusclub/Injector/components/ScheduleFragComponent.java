package com.android.renly.plusclub.Injector.components;

import com.android.renly.plusclub.Module.schedule.ScheduleFragment;
import com.android.renly.plusclub.Injector.PerFragment;
import com.android.renly.plusclub.Injector.modules.ScheduleFragModule;

import dagger.Component;

@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = ScheduleFragModule.class)
public interface ScheduleFragComponent {
    void inject(ScheduleFragment fragment);
}
