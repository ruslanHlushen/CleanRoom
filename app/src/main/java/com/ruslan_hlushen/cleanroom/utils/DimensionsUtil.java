package com.ruslan_hlushen.cleanroom.utils;

import android.content.Context;

/**
 * Created by Руслан on 01.12.2016.
 */
public class DimensionsUtil {

    private static int screenSizeWithDp;
    private static int screenSizeHeightDp;
    private static float metricDensity;


    public static void setDimensions(Context context) {

        DimensionsUtil.metricDensity = context.getResources().getDisplayMetrics().density;

        DimensionsUtil.screenSizeWithDp = context.getApplicationContext().getResources().getConfiguration().screenWidthDp;
        DimensionsUtil.screenSizeHeightDp = context.getApplicationContext().getResources().getConfiguration().screenHeightDp;

        if (DimensionsUtil.screenSizeWithDp > DimensionsUtil.screenSizeHeightDp) {
            int temp = DimensionsUtil.screenSizeHeightDp;
            DimensionsUtil.screenSizeHeightDp = DimensionsUtil.screenSizeWithDp;
            DimensionsUtil.screenSizeWithDp = temp;
        }
    }


    public static int getScreenSizeWithDp() { return screenSizeWithDp; }


    public static int getScreenSizeHeightDp() { return screenSizeHeightDp; }


    public static float getMetricDensity() { return metricDensity; }
}
