package com.ruslan_hlushen.cleanroom.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.ruslan_hlushen.cleanroom.network.ApiFactory;
import com.ruslan_hlushen.cleanroom.network.ApiInterface;

/**
 * Created by Ruslan on 31.01.2017.
 */

public class BaseActivity extends AppCompatActivity {

    private boolean canClick = false;


    protected boolean isCanClick() { return canClick; }


    protected void setCanClick(boolean canClick) {

        if (canClick) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            int orientation = getRequestedOrientation();
            int rotation = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
            }

            setRequestedOrientation(orientation);
        }

        this.canClick = canClick;
    }


    protected void setCanClickAndProgressBar(boolean state, ProgressBar progressBar) {

        progressBar.setVisibility((state ? View.GONE : View.VISIBLE));
        setCanClick(state);
    }


    protected ApiInterface getApiInterface() { return ApiFactory.create(); }
}