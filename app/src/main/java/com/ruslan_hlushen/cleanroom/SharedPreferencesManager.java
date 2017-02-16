package com.ruslan_hlushen.cleanroom;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Руслан on 11.01.2017.
 */
public class SharedPreferencesManager {

    public static final String EXTRA_firebaseToken = "myFirebaseToken";
    public static final String EXTRA_myVK_ID = "myVK_ID";
    public static final String EXTRA_rememberMe = "rememberMe";
    public static final String EXTRA_securityToken = "securityToken";
    public static final String EXTRA_hostelGroupVkId = "hostelGroupVkId";


    private SharedPreferencesManager() {}


    private static SharedPreferences getSharedPreferences(Context context) {

        return context.getSharedPreferences("default_settings", Context.MODE_PRIVATE);
    }


    public static String getMyFirebaseToken(Context context) {

        return getSharedPreferences(context).getString(EXTRA_firebaseToken, "");
    }


    public static void setMyToken(Context context, String newToken) {

        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(EXTRA_firebaseToken, newToken);
        editor.commit();
    }


    public static String getMySecurityToken(Context context) {

        return getSharedPreferences(context).getString(EXTRA_securityToken, "");
    }


    public static void setMySecurityToken(Context context, String newToken) {

        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(EXTRA_securityToken, newToken);
        editor.commit();
    }


    public static void setMyHostelGroupVkId(Context context, Integer hostelGroupVkId) {

        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(EXTRA_hostelGroupVkId, hostelGroupVkId);
        editor.commit();
    }


    public static Integer getMyHostelGroupVkId(Context context) {

        return getSharedPreferences(context).getInt(EXTRA_hostelGroupVkId, -1);
    }


    public static String getMyVK_ID(Context context) {

        return getSharedPreferences(context).getString(EXTRA_myVK_ID, "");
    }


    public static void setMyVK_ID(Context context, String newVK_ID) {

        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(EXTRA_myVK_ID, newVK_ID);
        editor.commit();
    }


    public static Boolean getRememberMe(Context context) {

        return getSharedPreferences(context).getBoolean(EXTRA_rememberMe, false);
    }


    public static void setRememberMe(Context context, Boolean newRememberMe) {

        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(EXTRA_rememberMe, newRememberMe);
        editor.commit();
    }
}