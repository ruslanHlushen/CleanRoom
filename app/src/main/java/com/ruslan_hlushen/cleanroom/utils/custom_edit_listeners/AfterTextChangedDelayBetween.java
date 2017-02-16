package com.ruslan_hlushen.cleanroom.utils.custom_edit_listeners;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Ruslan on 27.01.2017.
 */

public class AfterTextChangedDelayBetween implements TextWatcher {

    private static final int minDelay = 750;
    private AfterTextChangedListener afterTextChangedListener;
    private int millisecondsDelay;
    private Handler delayHandler;


    public AfterTextChangedDelayBetween(int millisecondsDelay, AfterTextChangedListener afterTextChangedListener) {

        this.millisecondsDelay = ((millisecondsDelay < minDelay) ? minDelay : millisecondsDelay);

        this.afterTextChangedListener = afterTextChangedListener;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}


    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}


    @Override
    public void afterTextChanged(Editable editable) {

        if (delayHandler == null
            && afterTextChangedListener != null) {

            afterTextChangedListener.afterTextChanged();

            delayHandler = new Handler();
            delayHandler.postDelayed(() -> delayHandler = null, millisecondsDelay);
        }
    }
}
