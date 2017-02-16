package com.ruslan_hlushen.cleanroom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.ruslan_hlushen.cleanroom.R;
import com.ruslan_hlushen.cleanroom.SharedPreferencesManager;
import com.ruslan_hlushen.cleanroom.models.User;
import com.ruslan_hlushen.cleanroom.network.RxJavaSubscriber;
import com.ruslan_hlushen.cleanroom.utils.DimensionsUtil;
import com.ruslan_hlushen.cleanroom.utils.MyVkUtil;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EnterActivity extends BaseActivity {

    EditText user_textViewToEnter, passwordEditTextToEnter;
    ImageView imageViewLogo;
    CardView button_CardView;
    TextView forgotPassword_textView, registration_textView;
    CheckBox checkBox;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        setParams();

        initViews();

        setOnClickListeners();
    }


    @Override
    protected void onResume() {

        super.onResume();

        RxJavaSubscriber.setContext(EnterActivity.this);
        checkWhereGo();
    }


    private void initViews() {

        user_textViewToEnter = (EditText) findViewById(R.id.login_EditTextToEnter);
        passwordEditTextToEnter = (EditText) findViewById(R.id.password_EditTextToEnter);
        imageViewLogo = (ImageView) findViewById(R.id.enter_activity_imageView_Logo);
        button_CardView = (CardView) findViewById(R.id.button_cardView);
        forgotPassword_textView = (TextView) findViewById(R.id.forgotPassword_textView);
        registration_textView = (TextView) findViewById(R.id.registration_textView);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        progressBar = (ProgressBar) findViewById(R.id.activity_enter_progressBar);

        setCanClick(true);
        setCheckBoxState();
    }


    private void setParams() {

        DimensionsUtil.setDimensions(EnterActivity.this);
        SharedPreferencesManager.setMyToken(EnterActivity.this, FirebaseInstanceId.getInstance().getToken());
    }


    private void checkWhereGo() {

        if (SharedPreferencesManager.getRememberMe(EnterActivity.this) && (!isSharPrefEmpty())) {

            goToMainActivity();
        }
    }


    private void goToMainActivity() {

        Intent intent = new Intent(EnterActivity.this, MainActivity.class);
        startActivity(intent);
    }


    private void setCheckBoxState() {

        checkBox.setSelected(SharedPreferencesManager.getRememberMe(EnterActivity.this));
    }


    private void setOnClickListeners() {

        button_CardView.setOnClickListener(view -> {

            if (String.valueOf(user_textViewToEnter.getText()).equals("")
                || String.valueOf(passwordEditTextToEnter.getText()).equals("")) {

                Toast.makeText(EnterActivity.this, getString(R.string.write_in_all_fields), Toast.LENGTH_LONG).show();

            } else if (isCanClick()) {

                setCanClickAndProgressBar(false, progressBar);
                callToAuthorization();
            }
        });


        registration_textView.setOnClickListener(view -> {

            Intent registerIntent = new Intent(EnterActivity.this, RegistrationActivity.class);
            startActivity(registerIntent);
        });


        forgotPassword_textView.setOnClickListener(view -> {

            Intent registerIntent = new Intent(EnterActivity.this, RestorePasswordActivity.class);
            startActivity(registerIntent);
        });
    }


    private void checkWhereToGoOnAuthorization(Integer groupVkId) {

        if (isSharPrefEmpty()) {

            SharedPreferencesManager.setMyHostelGroupVkId(EnterActivity.this, groupVkId);
            VKSdk.login(EnterActivity.this, "groups");

        } else {

            checkIsUserInVkGroup(groupVkId, SharedPreferencesManager.getMyVK_ID(EnterActivity.this));
        }
    }


    private void checkIsUserInVkGroup(Integer groupVkId, String userVkId) {

        new MyVkUtil(EnterActivity.this,
                     userVkId,
                     groupVkId,
                     (first_name, last_name, userId, hostelId) -> callToSendUserDataToSever(first_name, last_name, userId),
                     () -> setCanClickAndProgressBar(true, progressBar))
        .checkUserGroups();
    }


    private void callToAuthorization() {

        getApiInterface().authorisation(new User(user_textViewToEnter.getText().toString(),
                                                 passwordEditTextToEnter.getText().toString()))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new RxJavaSubscriber<>(integerMessageFromServer -> {

                             checkWhereToGoOnAuthorization(integerMessageFromServer.getData());

                         }, () -> {

                             SharedPreferencesManager.setRememberMe(EnterActivity.this, false);
                             setCheckBoxState();
                             setCanClickAndProgressBar(true, progressBar);
                         }));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {

                checkIsUserInVkGroup(SharedPreferencesManager.getMyHostelGroupVkId(EnterActivity.this), res.userId);
            }


            @Override
            public void onError(VKError error) {

                setCanClickAndProgressBar(true, progressBar);
                Toast.makeText(EnterActivity.this, getString(R.string.registration_impossible), Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void callToSendUserDataToSever(String first_name, String last_name, final String userVkId) {

        getApiInterface().updateUserData(new User(user_textViewToEnter.getText().toString(),
                                                  first_name,
                                                  last_name,
                                                  SharedPreferencesManager.getMyFirebaseToken(EnterActivity.this),
                                                  userVkId))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new RxJavaSubscriber<>(stringMessageFromServer -> {

                             SharedPreferencesManager.setMyVK_ID(EnterActivity.this, userVkId);
                             SharedPreferencesManager.setMySecurityToken(EnterActivity.this, stringMessageFromServer.getData());
                             SharedPreferencesManager.setRememberMe(EnterActivity.this, checkBox.isChecked());

                             goToMainActivity();

                         }, null, () -> setCanClickAndProgressBar(true, progressBar)));
    }


    private boolean isSharPrefEmpty() {

        return (SharedPreferencesManager.getMyVK_ID(EnterActivity.this) == null
                || SharedPreferencesManager.getMySecurityToken(EnterActivity.this) == null
                || SharedPreferencesManager.getMyVK_ID(EnterActivity.this).length() == 0
                || SharedPreferencesManager.getMySecurityToken(EnterActivity.this).length() == 0);
    }
}