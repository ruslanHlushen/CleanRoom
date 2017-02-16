package com.ruslan_hlushen.cleanroom.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ruslan_hlushen.cleanroom.R;
import com.ruslan_hlushen.cleanroom.activities.EnterActivity;
import com.ruslan_hlushen.cleanroom.activities.MainActivity;
import com.ruslan_hlushen.cleanroom.models.MyNotification;
import com.ruslan_hlushen.cleanroom.receivers.BadAnswerReceiver;
import com.ruslan_hlushen.cleanroom.receivers.GoodAnswerReceiver;
import com.ruslan_hlushen.cleanroom.utils.JsonUtil;

import org.json.JSONObject;

/**
 * Created by Руслан on 05.12.2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String EXTRA_notificationData = "notificationData";
    public static final String EXTRA_notification_server_id = "notification_server_id";
    public static final String EXTRA_notification_phone_id = "notification_phone_id";

    public static final int NOTIFICATION_ANSWER_ID = 10019911;
    public static final int NOTIFICATION_NEW_ID = 11945799;
    public static final int NOTIFICATION_INFO_ID = 8815456;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            MyNotification myNotification = new JsonUtil<MyNotification>().jsonToObject(new JSONObject(remoteMessage.getData()),
                                                                                        MyNotification.class.getName());

            if (myNotification.getNotificationType() == MyNotification.answerNotificationType) {

                sendAnswerNotification(myNotification, myNotification.getId());

            } else if (myNotification.getNotificationType() == MyNotification.newNotificationType) {

                if (MainActivity.isInFocus()) {

                    sendBroadcastToMainActivity(myNotification);

                } else {

                    sendNewNotification(myNotification, myNotification.getId());
                }

            } else if (myNotification.getNotificationType() == MyNotification.infoNotificationType) {

                sendInfoNotification(myNotification, myNotification.getId());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void sendBroadcastToMainActivity(MyNotification myNotification) {

        Intent intent = new Intent(MainActivity.EXTRA_broadcastAction);
        intent.putExtra(EXTRA_notificationData, myNotification);
        sendBroadcast(intent);
    }


    private void sendNewNotification(MyNotification myNotification, int notification_id) {

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(ns);

        Notification notification = new Notification(R.drawable.ic_launcher, null, System.currentTimeMillis());

        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_new);

        notification.contentView = notificationView;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationView.setTextViewText(R.id.notification_new_text,
                                         myNotification.getMessageWithFloor(getString(R.string.floor_text_for_notification)));
        notificationView.setTextViewText(R.id.notification_new_sender_login, myNotification.getUserLogin());
        notificationView.setTextViewText(R.id.notification_new_time,
                                         myNotification.getStringDateWithTime(getString(R.string.time_unknown)));

        setOnClickNewAndInfoNotificationListener(notificationView);

        notificationManager.notify(NOTIFICATION_NEW_ID + notification_id, notification);
    }


    private void sendAnswerNotification(MyNotification myNotification, int notification_id) {

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(ns);

        Notification notification = new Notification(R.drawable.ic_launcher, null, System.currentTimeMillis());

        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_answer);

        notification.contentView = notificationView;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationView.setTextViewText(R.id.notification_answer_text,
                                         myNotification.getMessageWithFloor(getString(R.string.floor_text_for_notification)));

        setOnClickAnswerNotificationListeners(notificationView, notification_id);

        notificationManager.notify(NOTIFICATION_ANSWER_ID + notification_id, notification);
    }


    private void sendInfoNotification(MyNotification myNotification, int notification_id) {

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(ns);

        Notification notification = new Notification(R.drawable.ic_launcher, null, System.currentTimeMillis());

        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_new);

        notification.contentView = notificationView;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationView.setTextViewText(R.id.notification_new_text, myNotification.getMessage());
        notificationView.setTextViewText(R.id.notification_new_sender_login, myNotification.getUserLogin());
        notificationView.setTextViewText(R.id.notification_new_time,
                                         myNotification.getStringDateWithTime(getString(R.string.time_unknown)));

        setOnClickNewAndInfoNotificationListener(notificationView);

        notificationManager.notify(NOTIFICATION_INFO_ID + notification_id, notification);
    }


    private void setOnClickNewAndInfoNotificationListener(RemoteViews notificationView) {

        Intent appIntent = new Intent(this, EnterActivity.class);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingAppIntent = PendingIntent.getActivity(this, 0, appIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_new_linearLayout, pendingAppIntent);
    }


    private void setOnClickAnswerNotificationListeners(RemoteViews notificationView, int notification_id) {

        Intent appIntent = new Intent(this, EnterActivity.class);
        appIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingAppIntent = PendingIntent.getActivity(this, 0, appIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.notification_answer_linearLayout, pendingAppIntent);


        Intent goodIntent = new Intent(this, GoodAnswerReceiver.class);
        goodIntent.putExtra(EXTRA_notification_server_id, notification_id);
        goodIntent.putExtra(EXTRA_notification_phone_id, NOTIFICATION_ANSWER_ID + notification_id);
        PendingIntent pendingGoodIntent = PendingIntent.getBroadcast(this,
                                                                     notification_id,
                                                                     goodIntent,
                                                                     PendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.notification_answer_goodAnswer, pendingGoodIntent);


        Intent badIntent = new Intent(this, BadAnswerReceiver.class);
        badIntent.putExtra(EXTRA_notification_server_id, notification_id);
        badIntent.putExtra(EXTRA_notification_phone_id, NOTIFICATION_ANSWER_ID + notification_id);
        PendingIntent pendingBadIntent = PendingIntent.getBroadcast(this,
                                                                    notification_id,
                                                                    badIntent,
                                                                    PendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.notification_answer_badAnswer, pendingBadIntent);
    }
}