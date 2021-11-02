package com.cat.lenovogaming.network_interface;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContentWebservice {
    String languagename = Locale.getDefault().toString();
    private static String MAIN_URL;
    private static ContentWebservice instance;
    private final ServiceInterface api;

    public ContentWebservice() {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        MAIN_URL = "https://legion-game.lenovomeaevents.com/api/"+languagename+"/";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(160, TimeUnit.SECONDS);
        httpClient.readTimeout(160, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(MAIN_URL)
                .build();

        api = retrofit.create(ServiceInterface.class);
        Log.i("Api", "" + api.toString());

    }

    public static ContentWebservice getInstance() {
//        if (instance == null) {
            instance = new ContentWebservice();
//        }
        return instance;
    }

    public ServiceInterface getApi() {
        Log.i("Service Interface", "" + api.toString());
        return api;
    }


}
