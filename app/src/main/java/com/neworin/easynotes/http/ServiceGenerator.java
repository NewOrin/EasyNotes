package com.neworin.easynotes.http;

import com.neworin.easynotes.utils.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NewOrin Zhang on 2017/4/21.
 * Project : com.neworin.easynotes.utils.http
 * Description:
 */

public class ServiceGenerator<T> {
    private static OkHttpClient httpClient = new OkHttpClient();
    private static Retrofit.Builder builder =
            new Retrofit.Builder().baseUrl(Constant.BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <T> T createService(Class serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return (T) retrofit.create(serviceClass);
    }
}
