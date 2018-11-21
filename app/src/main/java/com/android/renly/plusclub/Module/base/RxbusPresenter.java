package com.android.renly.plusclub.Module.base;

import rx.functions.Action1;

public interface RxbusPresenter extends BasePresenter {

    /**
     * 注册
     * @param eventType
     * @param action
     * @param <T>
     */
    <T> void registerRxBus(Class<T> eventType, Action1<T> action);

    /**
     * 注销
     */
    void unregisterRxBus();

}
