package com.cat.lenovogaming.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LangUtils {

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
    }

    public static void setAppLang(Context context, String appLang) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("appLang", appLang);
        editor.apply();
    }

    public static String getAppLang(Context context) {
        return getSharedPreferences(context).getString("appLang", "en");
    }

}
