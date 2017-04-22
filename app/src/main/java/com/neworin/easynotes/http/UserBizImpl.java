package com.neworin.easynotes.http;

import com.neworin.easynotes.http.biz.UserService;
import com.neworin.easynotes.model.User;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by NewOrin Zhang on 2017/4/21.
 * Project : com.neworin.easynotes.http
 * Description:
 */

public class UserBizImpl {

    private final String TAG = UserBizImpl.class.getSimpleName();

    /**
     * 用户登录
     *
     * @param user
     * @param callback
     */
    public void login(User user, Callback<Response> callback) {
        UserService service = ServiceGenerator.createService(UserService.class);
        Call<Response> call = service.login(user);
        call.enqueue(callback);
    }

    /**
     * 用户注册
     *
     * @param user
     * @param callback
     */
    public void register(User user, Callback<Response> callback) {
        UserService service = ServiceGenerator.createService(UserService.class);
        Call<Response> call = service.register(user);
        call.enqueue(callback);
    }
}
