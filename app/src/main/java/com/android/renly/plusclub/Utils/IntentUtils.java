package com.android.renly.plusclub.Utils;

import android.content.Context;
import android.content.Intent;

public class IntentUtils {

    public static void shareApp(Context context, String data) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, data);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, "分享Plus客户端到:"));
    }
}
