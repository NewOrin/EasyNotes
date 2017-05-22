package com.neworin.easynotes.ui.activity;

import android.databinding.DataBindingUtil;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.http.FileDownloadBizImpl;
import com.neworin.easynotes.model.EditData;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.ui.BaseNoteEditActivity;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DateUtil;
import com.neworin.easynotes.utils.FileUtil;
import com.neworin.easynotes.utils.L;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NewOrin Zhang on 2017/4/20.
 * Project : com.neworin.easynotes.ui.activity
 * Description:
 */

public class EditActivity extends BaseNoteEditActivity {

    private java.lang.String TAG = EditActivity.class.getSimpleName();

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mNote = getIntent().getExtras().getParcelable(Constant.ARG0);
        mNoteBook = getIntent().getExtras().getParcelable(Constant.ARG1);
        mDbManager = DBManager.getInstance(this);

        getToolbar().inflateMenu(R.menu.note_menu);
        getToolbar().setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        if (mNote.getTitle().equals("")) {
            setToolbarTitle(getString(R.string.note_activity_edit_title));
        } else {
            setToolbarTitle(mNote.getTitle());
        }
        mBinding.noteEditTitle.setText(mNote.getTitle());
        mBinding.noteEditTitle.setSelection(mNote.getTitle().length());
        insertDatas();
        initEvent();
    }

    /**
     * 插入数据
     */
    private void insertDatas() {
        List<EditData> datas = JSON.parseArray(mNote.getContent(), EditData.class);
        if (null == datas || datas.size() == 0) {
            getRichEdtor().setHaveContent(false);
            return;
        }
        if (datas.get(0).getImagePath() == null) {
            getRichEdtor().setHaveContent(true);
        } else {
            getRichEdtor().setHaveContent(false);
        }
        for (EditData e : datas) {
            if (e.getImagePath() == null) {
                if (!e.getInputStr().equals("")) {
                    getRichEdtor().insertContent(e.getInputStr());
                }
            } else {
                File file = new File(e.getImagePath());
                if (file.exists()) {
                    getRichEdtor().insertImage(e.getImagePath());
                } else {
                    String[] args = e.getImagePath().split("/");
                    handleNetImage(args[args.length - 1]);
                }
            }
        }
    }

    /**
     * 处理从网络获取图片
     *
     * @param fileName
     */
    private void handleNetImage(final String fileName) {
        FileDownloadBizImpl fileDownloadBiz = new FileDownloadBizImpl();
        fileDownloadBiz.downloadFile(Constant.GET_NOTE_IMAGE_URL + fileName + "/40", new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    FileUtil.writeResponseBodyToDisk(response.body(), fileName);
                    L.d(TAG, "图片" + fileName + "保存成功");
                    getRichEdtor().insertImage(Constant.FILE_SAVE_PATH + "/" + fileName);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                L.d(TAG, "图片" + fileName + "加载失败");
            }
        });
    }

    private void initEvent() {
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleExist();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            //完成
            case R.id.note_menu_edit_done:
                handleExist();
                break;
            case R.id.note_menu_camera:
                openCamera();
                break;
            case R.id.note_menu_photo:
                openSystemAlbum();
                break;
        }
        return true;
    }

    /**
     * 更新笔记
     */
    private void updateNote() {
        String content = getContentText();
        if (content == null) {
            content = "";
        }
        mDaoSession = mDbManager.getWriteDaoSession();
        long userId = 0;
        if (SharedPreferenceUtil.getString(this, Constant.USER_ID) != null) {
            userId = Long.parseLong(SharedPreferenceUtil.getString(this, Constant.USER_ID));
        }
        Note note = new Note();
        note.setId(mNote.getId());
        note.setNotebookId(mNoteBook.getId());
        note.setTitle(getTitleText());
        note.setContent(content);
        note.setUserId(userId);
        note.setStatus(Constant.STATUS_UPDATE);
        note.setCreateTime(mNote.getCreateTime());
        note.setUpdateTime(DateUtil.getNowTime());
        mDaoSession.getNoteDao().update(note);
    }

    private void handleExist() {
        updateNote();
        EventBus.getDefault().post(new NoteBookFragmentEvent.RefreshNoteEvent());
        finish();
    }

    @Override
    protected void onBackPress() {
        handleExist();
    }
}
