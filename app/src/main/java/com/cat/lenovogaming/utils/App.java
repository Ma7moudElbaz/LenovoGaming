package com.cat.lenovogaming.utils;

import android.content.Context;

import java.util.Locale;


import androidx.annotation.NonNull;

import com.akexorcist.localizationactivity.ui.LocalizationApplication;

public class App extends LocalizationApplication {

    @NonNull
    @Override
    public Locale getDefaultLanguage(@NonNull Context context) {
        return Locale.ENGLISH;
    }
}
