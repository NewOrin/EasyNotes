package com.neworin.easynotes.http.biz;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by NewOrin Zhang on 2017/5/22.
 * Project : com.neworin.easynotes.http.biz
 * Description:
 */

public interface FileDownloadService {

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}
