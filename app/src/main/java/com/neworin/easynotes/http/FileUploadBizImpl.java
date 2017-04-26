package com.neworin.easynotes.http;

import com.neworin.easynotes.http.biz.FileUploadService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by NewOrin Zhang on 2017/4/25.
 * Project : com.neworin.easynotes.http
 * Description:
 */

public class FileUploadBizImpl {

    private String TAG = FileUploadBizImpl.class.getSimpleName();

    public void uploadFile(String path, String desc,Callback<Response> callback) {
        File file = new File(path);
        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        //添加描述
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), desc);
        FileUploadService service = ServiceGenerator.createService(FileUploadService.class);
        //执行请求
        Call<Response> call = service.uploadFiles(description, body);
        call.enqueue(callback);
    }
}
