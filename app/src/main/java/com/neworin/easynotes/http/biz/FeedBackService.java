package com.neworin.easynotes.http.biz;

import com.neworin.easynotes.http.Response;
import com.neworin.easynotes.model.FeedBack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by NewOrin Zhang on 2017/5/25.
 * Project : com.neworin.easynotes.http.biz
 * Description:
 */

public interface FeedBackService {

    @POST("/feedback/add")
    Call<Response> addFeedBack(@Body FeedBack feedBack);
}
