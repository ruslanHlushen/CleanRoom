package com.ruslan_hlushen.cleanroom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ruslan_hlushen.cleanroom.R;
import com.ruslan_hlushen.cleanroom.SharedPreferencesManager;
import com.ruslan_hlushen.cleanroom.models.Hostel;
import com.ruslan_hlushen.cleanroom.models.User;
import com.ruslan_hlushen.cleanroom.network.RxJavaSubscriber;
import com.ruslan_hlushen.cleanroom.utils.GetDataUtil;
import com.ruslan_hlushen.cleanroom.utils.MyVkUtil;
import com.ruslan_hlushen.cleanroom.utils.custom_edit_listeners.AfterTextChanged;
import com.ruslan_hlushen.cleanroom.utils.custom_edit_listeners.AfterTextChangedDelay;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegistrationActivity extends BaseActivity {

    public static final String EXTRA_hostelList = "hostelList";

    AutoCompleteTextView autoCompleteTextViewHostel;
    ImageView imageRegister, imagePassword, imageRepeatPassword, imageViewLogin, imageHostel;
    EditText editText_Login, editText_repeatPassword, editText_Password, editText_Floor, editText_Email;
    ProgressBar progressBar, progressBarLogin;
    List<Hostel> hostelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initViews();

        setOnClickListeners();
        setEditValidationCheckers();

        if (savedInstanceState != null) {

            setHostelsListAdapter(savedInstanceState.getParcelableArrayList(EXTRA_hostelList));
        }
    }


    @Override
    protected void onResume() {

        super.onResume();

        RxJavaSubscriber.setContext(RegistrationActivity.this);

        if (hostelList.size() == 0) {

            callToGetAllHostels();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(EXTRA_hostelList, (ArrayList<? extends Parcelable>) hostelList);
        super.onSaveInstanceState(outState);
    }


    private void initViews() {

        progressBar = (ProgressBar) findViewById(R.id.activity_registration_progressBar);
        progressBarLogin = (ProgressBar) findViewById(R.id.activity_Registration_ProgressBarLogin);

        imageRegister = (ImageView) findViewById(R.id.activity_Registration_imageView_actionBar);
        imageViewLogin = (ImageView) findViewById(R.id.activity_Registration_imageLogin);
        imagePassword = (ImageView) findViewById(R.id.activity_Registration_imagePassword);
        imageRepeatPassword = (ImageView) findViewById(R.id.activity_Registration_imageRepeatPassword);
        imageHostel = (ImageView) findViewById(R.id.activity_Registration_imageHostel);

        autoCompleteTextViewHostel = (AutoCompleteTextView) findViewById(R.id.activity_Registration_AutoCompleteTextView_hostel);

        editText_Login = (EditText) findViewById(R.id.activity_Registration_DelayAutoCompleteTextViewLogin);

        editText_Email = (EditText) findViewById(R.id.activity_Registration_editText_Email);
        editText_Floor = (EditText) findViewById(R.id.activity_Registration_editText_Floor);
        editText_Password = (EditText) findViewById(R.id.activity_Registration_editText_Password);
        editText_repeatPassword = (EditText) findViewById(R.id.activity_Registration_editText_repeatPassword);

        setCanClick(true);
    }


    private void setEditValidationCheckers() {

        editText_Password
        .addTextChangedListener(new AfterTextChanged(() -> {

            setFieldStateImage(imagePassword,
                               (editText_Password.getText().length() > (getResources().getInteger(R.integer.min_password_length) - 1)));

            if (editText_repeatPassword.getText().toString().length() > 0) {

                setFieldStateImage(imageRepeatPassword, checkPasswords());
            }
        }));

        editText_repeatPassword
        .addTextChangedListener(new AfterTextChanged(() -> setFieldStateImage(imageRepeatPassword, checkPasswords())));

        autoCompleteTextViewHostel
        .addTextChangedListener(new AfterTextChanged(() -> {

            setFieldStateImage(imageHostel,
                               (GetDataUtil.getIdFromHostelsListByName(hostelList,
                                                                       autoCompleteTextViewHostel.getText().toString()) != null));
        }));

        editText_Login
        .addTextChangedListener(new AfterTextChangedDelay(750, () -> {

            if (editText_Login.getText().toString().length() > (getResources().getInteger(R.integer.min_login_length) - 1)) {

                progressBarLogin.setVisibility(View.VISIBLE);

                getApiInterface().checkLogin(editText_Login.getText().toString())
                                 .subscribeOn(Schedulers.io())
                                 .observeOn(AndroidSchedulers.mainThread())
                                 .subscribe(new RxJavaSubscriber<>(listMessageFromServer -> {

                                     setFieldStateImage(imageViewLogin, true);
                                 }, () -> {
                                     setFieldStateImage(imageViewLogin, false);
                                 }, () -> {
                                     progressBarLogin.setVisibility(View.GONE);
                                 }));
            } else {
                setFieldStateImage(imageViewLogin, false);
            }
        }));
    }


    private void setOnClickListeners() {

        imageRegister.setOnClickListener(view -> {

            if (isCanClick()) {

                checkingAllDataBeforeRegister();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {

                new MyVkUtil(RegistrationActivity.this,
                             res.userId,
                             GetDataUtil.getIdFromHostelsListByName(hostelList, autoCompleteTextViewHostel.getText().toString()),
                             (first_name, last_name, userId, hostelId) -> callToRegistration(first_name, last_name, userId, hostelId),
                             () -> setCanClickAndProgressBar(true, progressBar))
                .checkUserGroups();
            }


            @Override
            public void onError(VKError error) {

                setCanClickAndProgressBar(true, progressBar);
                Toast.makeText(RegistrationActivity.this, getString(R.string.registration_impossible), Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void checkingAllDataBeforeRegister() {

        if (!checkAreFieldsNotEmpty()) {

            Toast.makeText(RegistrationActivity.this, getString(R.string.write_in_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (GetDataUtil.getIdFromHostelsListByName(hostelList, autoCompleteTextViewHostel.getText().toString()) == null) {

            Toast.makeText(RegistrationActivity.this, getString(R.string.invalid_hostel), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkPasswords()) {

            Toast.makeText(RegistrationActivity.this, getString(R.string.different_passwords), Toast.LENGTH_SHORT).show();
            return;
        }

        setCanClickAndProgressBar(false, progressBar);
        VKSdk.login(RegistrationActivity.this, "groups");
    }


    private void callToRegistration(String first_name,
                                    String last_name,
                                    final String userId,
                                    final Integer hostelId) {

        getApiInterface().registration(new User(Integer.valueOf(editText_Floor.getText().toString()),
                                                hostelId,
                                                first_name,
                                                last_name,
                                                editText_Email.getText().toString(),
                                                editText_Login.getText().toString(),
                                                editText_Password.getText().toString(),
                                                SharedPreferencesManager.getMyFirebaseToken(RegistrationActivity.this),
                                                userId))
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new RxJavaSubscriber<>(stringMessageFromServer -> {

                             SharedPreferencesManager.setMyVK_ID(RegistrationActivity.this, userId);
                             SharedPreferencesManager.setMySecurityToken(RegistrationActivity.this, stringMessageFromServer.getData());

                             Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                             startActivity(intent);

                             finish();
                         }, null, () -> setCanClickAndProgressBar(true, progressBar)));
    }


    private void setFieldStateImage(ImageView imageView, boolean isGood) {

        if (isGood) {

            imageView.setImageResource(R.drawable.ic_ok);
        } else {
            imageView.setImageResource(R.drawable.ic_error);
        }
    }


    private void callToGetAllHostels() {

        setCanClick(false);

        getApiInterface().getAllHostels()
                         .subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new RxJavaSubscriber<>(listMessageFromServer -> {

                             setHostelsListAdapter(listMessageFromServer.getData());

                         }, null, () -> setCanClick(true)));
    }


    private void setHostelsListAdapter(List<Hostel> hostelList1) {

        hostelList = hostelList1;
        autoCompleteTextViewHostel.setAdapter(new ArrayAdapter<>(RegistrationActivity.this,
                                                                 android.R.layout.simple_list_item_1,
                                                                 GetDataUtil.getStringListFromHostelsList(hostelList)));
    }


    private boolean checkPasswords() {

        return (editText_Password.getText().length() > (getResources().getInteger(R.integer.min_password_length) - 1)
                && editText_Password.getText().toString().equals(editText_repeatPassword.getText().toString()));
    }


    private boolean checkAreFieldsNotEmpty() {

        return (editText_Login.getText().toString().length() > getResources().getInteger(R.integer.min_login_length) - 1
                && editText_Email.getText().toString().length() > 0
                && autoCompleteTextViewHostel.getText().toString().length() > 0
                && editText_Floor.getText().toString().length() > 0
                && editText_Password.getText().toString().length() > (getResources().getInteger(R.integer.min_password_length) - 1)
                && editText_repeatPassword.getText().toString().length() > (getResources().getInteger(R.integer.min_password_length) - 1));
    }
}