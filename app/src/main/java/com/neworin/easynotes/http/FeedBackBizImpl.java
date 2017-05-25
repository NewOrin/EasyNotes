package com.neworin.easynotes.http;

import com.neworin.easynotes.http.biz.FeedBackService;
import com.neworin.easynotes.model.FeedBack;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by NewOrin Zhang on 2017/5/25.
 * Project : com.neworin.easynotes.http
 * Description:
 */

public class FeedBackBizImpl {

    public void submitFeedBack(FeedBack feedBack, Callback callback) {
        FeedBackService service = ServiceGenerator.createService(FeedBackService.class);
        Call<Response> call = service.addFeedBack(feedBack);
        call.enqueue(callback);
    }
}
