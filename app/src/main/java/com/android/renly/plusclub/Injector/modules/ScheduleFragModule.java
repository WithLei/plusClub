package com.android.renly.plusclub.Injector.modules;

import com.android.renly.plusclub.Module.schedule.ScheduleFragment;
import com.android.renly.plusclub.Injector.PerFragment;
import com.android.renly.plusclub.Module.schedule.ScheduleFragPresenter;

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
