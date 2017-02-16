package com.ruslan_hlushen.cleanroom.Firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ruslan_hlushen.cleanroom.SharedPreferencesManager;

/**
 * Created by Руслан on 05.12.2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        SharedPreferencesManager.setMyToken(getApplicationContext(), FirebaseInstanceId.getInstance().getToken());
    }
}
