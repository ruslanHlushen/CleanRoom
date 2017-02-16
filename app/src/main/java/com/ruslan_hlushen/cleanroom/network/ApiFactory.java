package com.ruslan_hlushen.cleanroom.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Руслан on 30.11.2016.
 */
public class ApiFactory {

    private static ApiInterface api;


    private static void createApi() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiInterface.base_URL)
                                                  .addConverterFactory(GsonConverterFactory.create())
                                                  .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                  .client(new OkHttpClient())
                                                  .build();
        api = retrofit.create(ApiInterface.class);
    }


    public static ApiInterface create() {

        if (api == null) {
            createApi();
        }
        return api;
    }
}
