package com.cat.lenovogaming;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.cat.lenovogaming.events.EventsActivity;
import com.cat.lenovogaming.home.MainActivity;
import com.cat.lenovogaming.login_register_forgot.LoginActivity;
import com.cat.lenovogaming.news_all.NewsActivity;
import com.cat.lenovogaming.products_all.ProductsActivity;
import com.cat.lenovogaming.profile.ProfileActivity;
import com.cat.lenovogaming.promotions.PromotionsActivity;
import com.cat.lenovogaming.tournaments.TournamentsActivity;
import com.cat.lenovogaming.utils.LangUtils;

import java.util.Locale;

public class SideNavigationHandler {
    Context context;
    int clickedItemId;
    MainActivity activity;

    public SideNavigationHandler(Context context, int clickedItemId) {
        this.context = context;
        this.clickedItemId=clickedItemId;
    }





    public void navigate(){

        activity = (MainActivity)context;
        if (clickedItemId == R.id.nav_home) {

        } else if (clickedItemId == R.id.nav_show_case) {
            Intent i = new Intent(context, WebBrowser.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("url","https://legion-game.lenovomeaevents.com/walk");
            context.startActivity(i);
        } else if (clickedItemId == R.id.nav_news) {
            Intent i = new Intent(context, NewsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (clickedItemId == R.id.nav_products) {
            Intent i = new Intent(context, ProductsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (clickedItemId == R.id.nav_events) {
            Intent i = new Intent(context, EventsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (clickedItemId == R.id.nav_promotions) {
            Intent i = new Intent(context, PromotionsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (clickedItemId == R.id.nav_tournaments) {
            Intent i = new Intent(context, TournamentsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (clickedItemId == R.id.nav_login) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else if (clickedItemId == R.id.nav_logout) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }else if (clickedItemId == R.id.nav_profile) {
            Intent i = new Intent(context, ProfileActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }else if (clickedItemId == R.id.nav_language) {

            if (Locale.getDefault().toString().equals("en")) {
                activity.setLanguage("ar");
                LangUtils.setAppLang(context,"ar");
            } else {
                activity.setLanguage("en");
                LangUtils.setAppLang(context,"en");
            }
//            Baa.setLanguage("ar");
//            if (LanguageUtils.getLanguage(context).equals("en")){
//                LocaleHelper.changeLanguage(context, "ar");
//            }
//            else {
//                LocaleHelper.changeLanguage(context, "en");
//            }
        }
    }
    public void recreateTask(final Context context) {
        final PackageManager pm = context.getPackageManager();
        final Intent intent = pm.getLaunchIntentForPackage(context.getPackageName());
        final ComponentName componentName = intent.getComponent();
        final Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }
}
