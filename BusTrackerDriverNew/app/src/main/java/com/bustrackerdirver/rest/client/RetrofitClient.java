package com.bustrackerdirver.rest.client;

import com.bustrackerdirver.rest.api.BusLogRetrofitAPI;
import com.bustrackerdirver.rest.api.TimeRowRetrofitAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String API_URL = "http://localhost:8080/";

    public static BusLogRetrofitAPI getBusLogApi() {
        return getInstance().create(BusLogRetrofitAPI.class);
    }

    public static TimeRowRetrofitAPI getTimeRowApi() {
        return getInstance().create(TimeRowRetrofitAPI.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
