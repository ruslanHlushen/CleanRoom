package com.ruslan_hlushen.cleanroom.utils.custom_edit_listeners;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Ruslan on 26.01.2017.
 */

public class AfterTextChangedDelay implements TextWatcher {

    private static final int minDelay = 750;
    private final Handler delayHandler = new Handler();
    private AfterTextChangedListener afterTextChangedListener;
    private int millisecondsDelay;
    private Runnable delayRunnable = () -> afterTextChangedListener.afterTextChanged();


    public AfterTextChangedDelay(int millisecondsDelay, AfterTextChangedListener afterTextChangedListener) {

        this.millisecondsDelay = ((millisecondsDelay < minDelay) ? minDelay : millisecondsDelay);

        this.afterTextChangedListener = afterTextChangedListener;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }


    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }


    @Override
    public void afterTextChanged(Editable editable) {

        if (afterTextChangedListener != null) {

            delayHandler.removeCallbacks(delayRunnable);
            delayHandler.postDelayed(delayRunnable, millisecondsDelay);
        }
    }
}
