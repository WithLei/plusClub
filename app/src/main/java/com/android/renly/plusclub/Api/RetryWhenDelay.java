package com.android.renly.plusclub.Api;

import android.content.Context;

import io.reactivex.ObservableSource;
import rx.Observable;
import rx.functions.Func1;

/**
 * 重新获取Token
 */
public class RetryWhenDelay implements
        Func1<Observable<? extends Throwable>, ObservableSource<?>> {
    private final int MAX_RETRIES;
    private final int RETRY_DELAY_MILLS;
    private Context context;
    private int retryCount;

    public RetryWhenDelay(int MAX_RETRIES, int RETRY_DELAY_MILLS, Context context) {
        this.MAX_RETRIES = MAX_RETRIES;
        this.RETRY_DELAY_MILLS = RETRY_DELAY_MILLS;
        this.context = context;
    }


    @Override
    public ObservableSource<?> call(Observable<? extends Throwable> observable) {
        return null;
    }
}
