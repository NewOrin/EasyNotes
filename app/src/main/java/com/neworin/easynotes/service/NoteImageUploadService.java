package com.neworin.easynotes.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.greendao.gen.NoteDao;
import com.neworin.easynotes.http.FileUploadBizImpl;
import com.neworin.easynotes.http.Response;
import com.neworin.easynotes.model.EditData;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.L;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by NewOrin Zhang on 2017/5/14.
 * Project : com.neworin.easynotes.service
 * Description:
 */

public class NoteImageUploadService extends IntentService {

    private static final String TAG = NoteImageUploadService.class.getSimpleName();
    private long mUserId;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NoteImageUploadService() {
        super(TAG);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mUserId = SharedPreferenceUtil.getUserId(this);
        if (0 != mUserId) {
            List<String> pathStrList = getNeedsToUploadImage();
            FileUploadBizImpl fileUploadBiz = new FileUploadBizImpl();
            fileUploadBiz.uploadNoteImage(pathStrList, String.valueOf(mUserId), new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    L.d(TAG, "多图片上传成功");
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    L.d(TAG, "多图片上传失败" + t.getMessage());
                }
            });
        }
    }

    private List<String> getNeedsToUploadImage() {
        DBManager dbManager = DBManager.getInstance(this);
        NoteDao noteDao = dbManager.getWriteDaoSession().getNoteDao();
        List<Note> noteList = noteDao.queryBuilder().where(NoteDao.Properties.UserId.eq(mUserId), NoteDao.Properties.Status.notEq(Constant.STATUS_COMPLETED)).list();
        List<EditData> editDataList;
        List<String> imagePathStrs = new ArrayList<>();
        for (int i = 0; i < noteList.size(); i++) {
            editDataList = JSON.parseArray(noteList.get(i).getContent(), EditData.class);
            if (editDataList.size() != 0) {
                for (EditData ed : editDataList) {
                    if (ed.getImagePath() != null) {
                        imagePathStrs.add(ed.getImagePath());
                    }
                }
            }
        }
        return imagePathStrs;
    }
}
