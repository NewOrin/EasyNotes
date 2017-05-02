package com.neworin.easynotes.http.biz;

import com.neworin.easynotes.http.Response;
import com.neworin.easynotes.model.NoteAndBookList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by NewOrin Zhang on 2017/4/30.
 * Project : com.neworin.easynotes.http.biz
 * Description:
 */

public interface NoteService {

    @POST("/main/sync_data")
    Call<Response> syncNoteData(@Body NoteAndBookList params);

    @POST("/main/post_data")
    Call<Response> postNoteData(@Body NoteAndBookList params);

}
