package com.hoocons.hoocons_android.Networking;

import android.util.Log;

import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hungnguyen on 6/16/17.
 */

public class NetContext {
    private static final String TAG = NetContext.class.getSimpleName();
    public static NetContext instance = new NetContext();

    private Retrofit retrofit;
    private OkHttpClient client;

    private NetContext() {
        client = new OkHttpClient
                .Builder().connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new LoggerInterceptor())
                .addInterceptor(new HeaderInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://dry-ravine-18402.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    private class LoggerInterceptor implements Interceptor {
        private static final String TAG = "LoggerInterceptor";

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            Log.d(TAG, String.format("url: %s", request.toString()));

            RequestBody body = request.body();
            if (body != null) {
                Log.d(TAG, String.format("body: %s", body.toString()));
            }

            Headers headers = request.headers();
            if (headers != null) {
                Log.d(TAG, String.format("headers: %s", headers.toString()));
            }

            Response response = chain.proceed(request);

            Log.d(TAG, String.format("response: %s", response.toString()));
            Log.d(TAG, String.format("response body: %s", getResponseString(response)));

            return response;
        }
    }

    private String getResponseString(Response response) {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();
        return buffer.clone().readString(Charset.forName("UTF-8"));
    }

    private class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String token = SharedPreferencesManager.getDefault().getUserToken();
            Request request;
            if (token != null) {
                request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", String.format("JWT %s", token))
                        .build();
                Log.e(TAG, String.format("intercept: %s", request.headers().toString()));
            } else
                request = chain.request()
                        .newBuilder().build();
            return chain.proceed(request);

        }
    }

    public <T> T create(Class<T> classz) {
        return retrofit.create(classz);
    }
}