package com.kibzdev.globalfarm.rest;


import com.kibzdev.globalfarm.BuildConfig;
import com.kibzdev.globalfarm.utils.AppUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = BuildConfig.BASE_URL;
    private static Retrofit retrofit = null;
    private static Retrofit retrofit2 = null;


    //----------------------------------------------------------------------------------------------

    final static ConnectionPool connectionPool = new ConnectionPool(5, 60, TimeUnit.SECONDS);

    final static OkHttpClient httpClient = new OkHttpClient.Builder()
            .readTimeout(250, TimeUnit.SECONDS)
            .connectTimeout(250, TimeUnit.SECONDS)
            .connectionPool(connectionPool)
            .retryOnConnectionFailure(true)
            .build();

    public static Retrofit getClientPooling() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }

    //----------------------------------------------------------------------------------------------


}