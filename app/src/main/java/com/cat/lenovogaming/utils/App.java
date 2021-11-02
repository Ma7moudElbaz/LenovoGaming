package com.cat.lenovogaming.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;


import androidx.annotation.NonNull;

import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate;
import com.akexorcist.localizationactivity.ui.LocalizationApplication;

public class App extends LocalizationApplication {

    @NonNull
    @Override
    public Locale getDefaultLanguage(@NonNull Context context) {
        return Locale.ENGLISH;
    }
}
