package com.ruslan_hlushen.cleanroom.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ruslan_hlushen.cleanroom.R;
import com.ruslan_hlushen.cleanroom.dialogs.TextAndButtonDialog;
import com.ruslan_hlushen.cleanroom.network.RxJavaSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RestorePasswordActivity extends BaseActivity {

    ProgressBar progressBar;
    EditText editText_email;
    TextView textViewSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_password);

        initViews();

        setOnClickListeners();

        setCanClick(true);
    }


    @Override
    protected void onResume() {

        super.onResume();

        RxJavaSubscriber.setContext(RestorePasswordActivity.this);
    }


    private void initViews() {

        progressBar = (ProgressBar) findViewById(R.id.activity_restore_password_progressBar);
        editText_email = (EditText) findViewById(R.id.activity_restore_pasword_editText_Email);
        textViewSend = (TextView) findViewById(R.id.activity_restore_password_textView_Send);
    }


    private void setOnClickListeners() {

        textViewSend.setOnClickListener(view -> {

            if (editText_email.getText().toString().length() > 0) {

                if (isCanClick()) {

                    callToSendNewPasswordToEmail();
                }
            } else {
                Toast.makeText(RestorePasswordActivity.this, getString(R.string.write_in_one_field), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void callToSendNewPasswordToEmail() {

        setCanClickAndProgressBar(false, progressBar);

        getApiInterface().restorePassword(editText_email.getText().toString())
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new RxJavaSubscriber<>(stringMessageFromServer -> {

                             new TextAndButtonDialog().setParams(getString(R.string.new_password_is_on_your_email), () -> finish())
                                                      .show(getSupportFragmentManager(), TextAndButtonDialog.Tag);

                         }, null, () -> setCanClickAndProgressBar(true, progressBar)));
    }
}