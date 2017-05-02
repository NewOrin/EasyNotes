package com.neworin.easynotes.ui.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.model.EditData;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.ui.BaseNoteEditActivity;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DateUtil;
import com.neworin.easynotes.utils.GenerateSequenceUtil;
import com.neworin.easynotes.utils.L;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by NewOrin Zhang on 2017/3/13.
 * E-mail : NewOrinZhang@Gmail.com
 * 添加笔记，编辑笔记页面
 */
public class NoteActivity extends BaseNoteEditActivity implements Toolbar.OnMenuItemClickListener {

    private String mTitle;
    private String mContent;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mNoteBook = getIntent().getExtras().getParcelable(Constant.ARG1);
        mDbManager = DBManager.getInstance(this);
        getRichEdtor().setHaveContent(false);
        getToolbar().inflateMenu(R.menu.note_menu);
        getToolbar().setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        setToolbarTitle(getString(R.string.note_activity_title));
        initEvent();
    }

    @Override
    protected void onBackPress() {
        handleExist();
    }

    private void initEvent(){
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
                List<EditData> dataList = getRichEdtor().buildEditData();
                for (EditData ed : dataList) {
                    L.d(ed.toString());
                }
                if (!isSaveNote()) {
                    showSnackBar(mBinding.getRoot(), getString(R.string.note_input_content_alert));
                } else {
                    handleExist();
                }
                break;
            case R.id.note_menu_camera:
                checkCamera();
                break;
            case R.id.note_menu_photo:
                checkAlbum();
                break;
        }
        return true;
    }

    /**
     * 保存笔记
     */
    private void saveNote() {
        if (isSaveNote()) {
            if (mContent == null) {
                mContent = "";
            }
            mDaoSession = mDbManager.getWriteDaoSession();
            Note note = new Note();
            note.setId(GenerateSequenceUtil.generateSequenceNo());
            note.setNotebookId(mNoteBook.getId());
            note.setNoteBook(mNoteBook);
            note.setTitle(mTitle);
            note.setContent(mContent);
            note.setStatus(Constant.STATUS_ADD);
            note.setCreateTime(DateUtil.getNowTime());
            if (null != SharedPreferenceUtil.getString(this, Constant.USER_ID)) {
                note.setUserId(Long.parseLong(SharedPreferenceUtil.getString(this, Constant.USER_ID)));
            }
            mDaoSession.getNoteDao().insert(note);
            mDaoSession.clear();
        }
    }


    /**
     * 执行退出操作
     */
    private void handleExist() {
        if (isSaveNote()) {
            saveNote();
        }
        EventBus.getDefault().post(new NoteBookFragmentEvent.RefreshNoteEvent());
        finish();
    }

    /**
     * 判断是否需要保存笔记
     *
     * @return
     */
    private boolean isSaveNote() {
        mTitle = getTitleText();
        mContent = getContentText();
        return !(mTitle.equals("") && mContent == null);
    }
}

