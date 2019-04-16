package com.kanjidamage.app;

import android.text.Editable;
import android.text.TextWatcher;

abstract class TextWatcherHelper implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}