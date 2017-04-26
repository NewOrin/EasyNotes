package com.neworin.easynotes.http.biz;

import com.neworin.easynotes.http.Response;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by NewOrin Zhang on 2017/4/25.
 * Project : com.neworin.easynotes.http.biz
 * Description:
 */
public interface FileUploadService {


    @Multipart
    @POST("/file/file_upload")
    Call<Response> uploadFiles(@Part("description") RequestBody description, @Part MultipartBody.Part file);
}
