package com.cat.lenovogaming;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

import static com.cat.lenovogaming.LanguageUtils.getLanguage;

public class LenovoGaming extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.changeLanguage(base, getLanguage(base)));
    }
}
