package com.ruslan_hlushen.cleanroom.activities;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ruslan_hlushen.cleanroom.R;
import com.ruslan_hlushen.cleanroom.SharedPreferencesManager;
import com.ruslan_hlushen.cleanroom.dialogs.TextAndButtonDialog;
import com.ruslan_hlushen.cleanroom.network.RxJavaSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class NewNotificationActivity extends BaseActivity {

    CardView button_CardView;
    EditText editText_message, editText_floor;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notification);

        initViews();

        setParams();

        setOnClickListeners();

        setCanClick(true);
    }


    @Override
    protected void onResume() {

        super.onResume();

        RxJavaSubscriber.setContext(NewNotificationActivity.this);
    }


    private void initViews() {

        button_CardView = (CardView) findViewById(R.id.newNotification_cardVew_Button);
        editText_message = (EditText) findViewById(R.id.newNotification_editText_message);
        editText_floor = (EditText) findViewById(R.id.newNotification_editText_floor);
        progressBar = (ProgressBar) findViewById(R.id.activity_newNotification_progressBar);
    }


    private void setParams() {

        button_CardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
    }


    private void setOnClickListeners() {

        button_CardView.setOnClickListener(view -> {

            if (checkAreFieldsNotEmpty()) {

                if (isCanClick()) {
                    callToNewNotification();
                }
            } else {
                Toast.makeText(NewNotificationActivity.this, getString(R.string.write_in_all_fields), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkAreFieldsNotEmpty() {

        return (editText_message.getText().toString().length() > 0
                && editText_floor.getText().toString().length() > 0);
    }


    private void callToNewNotification() {

        setCanClickAndProgressBar(false, progressBar);

        getApiInterface().newNotification(SharedPreferencesManager.getMySecurityToken(NewNotificationActivity.this),
                                          editText_message.getText().toString(),
                                          Integer.valueOf(editText_floor.getText().toString()))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new RxJavaSubscriber<>(stringResponseFromServer -> {

                             new TextAndButtonDialog().setParams(getString(R.string.your_notifications_is_send), () -> finish())
                                                      .show(getSupportFragmentManager(), TextAndButtonDialog.Tag);

                         }, null, () -> setCanClickAndProgressBar(true, progressBar)));
    }
}