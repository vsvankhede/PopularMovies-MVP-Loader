package com.vstechlab.popularmovies.data.source.remote;

import android.support.annotation.NonNull;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class SignedInterceptor implements Interceptor {
    private final String API_KEY;

    public SignedInterceptor(@NonNull String apiKey) {
        this.API_KEY = apiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        HttpUrl.Builder authorizedUrlBuilder = oldRequest.httpUrl().newBuilder()
                .scheme(oldRequest.httpUrl().scheme())
                .host(oldRequest.httpUrl().host());

        authorizedUrlBuilder.addQueryParameter(MoviesAPI.PARAM_API_KEY, API_KEY);

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();
        return chain.proceed(newRequest);
    }
}
