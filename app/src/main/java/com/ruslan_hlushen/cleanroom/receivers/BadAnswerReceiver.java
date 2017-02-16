package com.ruslan_hlushen.cleanroom.receivers;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Руслан on 27.11.2016.
 */
public class BadAnswerReceiver extends ParentAnswerReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        myOnReceive(context, intent, false);
    }
}
