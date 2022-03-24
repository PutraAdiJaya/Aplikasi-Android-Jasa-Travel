package me.pj.travel.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    /*ALAMAT WEB SERVER*/
        public static final String WEB_URL = "http://pj-travel.com/";
        public static final String MAPS_URL = WEB_URL + "maps";
        public static final String LOKASI_URL = WEB_URL + "lokasi";
        public static final String BASE_URL = WEB_URL + "api/";
        private static Retrofit retrofit = null;

/* REQUEST CLIENT */
        public static Retrofit getClient() {

            Dispatcher dispatcher = new Dispatcher(Executors.newFixedThreadPool(20));
            dispatcher.setMaxRequests(20);
            dispatcher.setMaxRequestsPerHost(1);


            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);



            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .dispatcher(dispatcher)
                    .connectionPool(new ConnectionPool(100, 30, TimeUnit.SECONDS))
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
/*STAR REQUEST*/
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
            return retrofit;
           /* if (retrofit==null) {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            }
            return retrofit;*/
            /*
            if (retrofit==null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;*/
        }
}