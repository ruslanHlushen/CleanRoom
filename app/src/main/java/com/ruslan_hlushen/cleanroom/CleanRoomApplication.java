package com.ruslan_hlushen.cleanroom;

import android.app.Application;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by Руслан on 04.12.2016.
 */
public class CleanRoomApplication extends Application {

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {

            if (newToken == null) {

            }
        }
    };


    @Override
    public void onCreate() {

        super.onCreate();

        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }
}
