package com.ruslan_hlushen.cleanroom.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ruslan_hlushen.cleanroom.Firebase.MyFirebaseMessagingService;
import com.ruslan_hlushen.cleanroom.R;
import com.ruslan_hlushen.cleanroom.SharedPreferencesManager;
import com.ruslan_hlushen.cleanroom.network.ApiFactory;
import com.ruslan_hlushen.cleanroom.network.RxJavaSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ruslan on 29.01.2017.
 */

public class ParentAnswerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) { }


    protected void myOnReceive(Context context, Intent intent, boolean status) {

        sendAnswer(context, intent, status);
        deleteNotification(context, intent);
    }


    protected void sendAnswer(Context context, Intent intent, boolean status) {

        RxJavaSubscriber.setContext(context);

        int notificationServerId = intent.getIntExtra(MyFirebaseMessagingService.EXTRA_notification_server_id, -1);

        ApiFactory.create().sendAnswerAboutNotification(SharedPreferencesManager.getMySecurityToken(context),
                                                        notificationServerId,
                                                        status)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new RxJavaSubscriber<>(stringMessageFromServer -> {

                      Toast.makeText(context, context.getString(R.string.your_answer_delivered), Toast.LENGTH_LONG).show();
                  }, null));
    }


    protected void deleteNotification(Context context, Intent intent) {

        int notificationPhoneId = intent.getIntExtra(MyFirebaseMessagingService.EXTRA_notification_phone_id, -1);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationPhoneId);
    }
}
