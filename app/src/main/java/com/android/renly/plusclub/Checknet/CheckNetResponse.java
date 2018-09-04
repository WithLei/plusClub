package com.android.renly.plusclub.Checknet;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by free2 on 16-4-13.
 * 检查网络回调
 */
public abstract class CheckNetResponse {
    private Handler handler;
    private Looper looper = null;

    protected CheckNetResponse() {
        this(null);
    }

    private CheckNetResponse(Looper looper) {
        this.looper = (looper == null ? Looper.getMainLooper() : looper);
        handler = new ResponderHandler(this, this.looper);
    }

    private void handleMessage(Message msg) {
        String s = (String) msg.obj;
        onFinish(msg.what, s);
    }

    public abstract void onFinish(int type, String response);

    final void sendFinishMessage(int type, String s) {
        handler.sendMessage(obtainMessage(type, s));
    }

    private Message obtainMessage(int responseMessageId, Object responseMessageData) {
        return Message.obtain(handler, responseMessageId, responseMessageData);
    }

    private static class ResponderHandler extends Handler {
        private final CheckNetResponse mResponder;

        ResponderHandler(CheckNetResponse mResponder, Looper looper) {
            super(looper);
            this.mResponder = mResponder;
        }

        @Override
        public void handleMessage(Message msg) {
            mResponder.handleMessage(msg);
        }
    }
}
