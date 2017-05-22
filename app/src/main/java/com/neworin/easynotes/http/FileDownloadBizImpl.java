package com.neworin.easynotes.http;

import com.neworin.easynotes.http.biz.FileDownloadService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by NewOrin Zhang on 2017/5/22.
 * Project : com.neworin.easynotes.http
 * Description:
 */

public class FileDownloadBizImpl {

    public void downloadFile(String url, Callback<ResponseBody> callback) {
        FileDownloadService fileDownloadService = ServiceGenerator.createService(FileDownloadService.class);
        Call<ResponseBody> call = fileDownloadService.downloadFile(url);
        call.enqueue(callback);
    }
}
