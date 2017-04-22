package com.neworin.easynotes.http.biz;

import com.neworin.easynotes.http.Response;
import com.neworin.easynotes.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by NewOrin Zhang on 2017/4/21.
 * Project : com.neworin.easynotes.http.biz
 * Description:
 */

public interface UserService {
    @POST("/user/login")
    Call<Response> login(@Body User user);

    @POST("/user/register")
    Call<Response> register(@Body User user);
}
