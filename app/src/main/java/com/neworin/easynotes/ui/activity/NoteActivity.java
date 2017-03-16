package com.neworin.easynotes.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.ActivityNoteLayoutBinding;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DateUtil;
import com.neworin.easynotes.utils.GenerateSequenceUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by NewOrin Zhang on 2017/3/13.
 * E-mail : NewOrinZhang@Gmail.com
 */

public class NoteActivity extends BaseAppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private ActivityNoteLayoutBinding mBinding;
    private String mTitle;
    private String mContent;
    private String mTime;
    private boolean isDestroy = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initView();
    }

    private void initView() {
        setToolbarTitle(getString(R.string.note_activity_title));
        getToolbar().inflateMenu(R.menu.note_menu);
        getToolbar().setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDestroy = false;
                saveNote();
                if (!isSaveNote()) {
                    finish();
                }
            }
        });
    }

    /**
     * 判断是否需要保存笔记
     *
     * @return
     */
    private boolean isSaveNote() {
        mTitle = mBinding.noteEditTitle.getText().toString();
        mContent = mBinding.noteEditContent.getText().toString();
        return !(mTitle.isEmpty() && mContent.isEmpty());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_note_layout;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        //完成
        if (item.getItemId() == R.id.note_menu_edit_done) {
            isDestroy = false;
            saveNote();
            if (!isSaveNote()) {
                showSnackBar(mBinding.getRoot(), getString(R.string.note_input_content_alert));
            }
        }
        return true;
    }

    /**
     * 保存笔记
     */
    private void saveNote() {
        if (isSaveNote()) {
            DBManager dbManager = DBManager.getInstance(this);
            Note note = new Note();
            note.setId(GenerateSequenceUtil.generateSequenceNo());
            note.setTitle(mTitle);
            note.setContent(mContent);
            note.setCreateTime(DateUtil.getNowTime());
            dbManager.getWriteDaoSession().getNoteDao().insert(note);
            if (!isDestroy) {
                setResult(Constant.NOTE_BOOK_FRAGMENT_RESULT_CODE);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (isDestroy) {
            //直接退出
            saveNote();
            EventBus.getDefault().post(new NoteBookFragmentEvent.RefreshNoteEvent());
        }
        super.onDestroy();
    }
}
