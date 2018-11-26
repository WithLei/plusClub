package com.android.renly.plusclub.injector.modules;

import com.android.renly.plusclub.module.schedule.ScheduleFragment;
import com.android.renly.plusclub.injector.PerFragment;
import com.android.renly.plusclub.module.schedule.ScheduleFragPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ScheduleFragModule {
    private final ScheduleFragment mView;

    public ScheduleFragModule(ScheduleFragment mView) {
        this.mView = mView;
    }

    @PerFragment
    @Provides
    public ScheduleFragPresenter provideScheduleFragPresenter() {
        return new ScheduleFragPresenter(mView);
    }
}
