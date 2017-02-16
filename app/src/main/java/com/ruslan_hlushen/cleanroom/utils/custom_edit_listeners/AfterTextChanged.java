package com.ruslan_hlushen.cleanroom.utils.custom_edit_listeners;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Ruslan on 26.01.2017.
 */

public class AfterTextChanged implements TextWatcher {

    AfterTextChangedListener afterTextChangedListener;


    public AfterTextChanged(AfterTextChangedListener afterTextChangedListener) {

        this.afterTextChangedListener = afterTextChangedListener;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }


    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }


    @Override
    public void afterTextChanged(Editable editable) {

        if (afterTextChangedListener != null) {

            afterTextChangedListener.afterTextChanged();
        }
    }
}
