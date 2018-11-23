package com.android.renly.plusclub.Injector.modules;

import com.android.renly.plusclub.Module.mine.MineFragment;
import com.android.renly.plusclub.Injector.PerFragment;
import com.android.renly.plusclub.Module.mine.MineFragPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class MineFragModule {
    private final MineFragment mView;

    public MineFragModule(MineFragment mView){
        this.mView = mView;
    }

    @PerFragment
    @Provides
    public MineFragPresenter provideMineFragPresenter(){
        return new MineFragPresenter(mView);
    }
}
