package com.neworin.easynotes.http;

import com.neworin.easynotes.http.converter.EConverterFactory;
import com.neworin.easynotes.utils.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by NewOrin Zhang on 2017/4/21.
 * Project : com.neworin.easynotes.utils.http
 * Description:
 */

public class ServiceGenerator<T> {
    private static OkHttpClient httpClient = new OkHttpClient();
    private static Retrofit.Builder builder =
            new Retrofit.Builder().baseUrl(Constant.BASE_API_URL)
                    .addConverterFactory(EConverterFactory.create());

    public static <T> T createService(Class serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return (T) retrofit.create(serviceClass);
    }
}
