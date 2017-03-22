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
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DateUtil;
import com.neworin.easynotes.utils.GenerateSequenceUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by NewOrin Zhang on 2017/3/13.
 * E-mail : NewOrinZhang@Gmail.com
 * 添加笔记，编辑笔记页面
 */

public class NoteActivity extends BaseAppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private ActivityNoteLayoutBinding mBinding;
    private String mTitle;
    private String mContent;
    private String mTime;
    private boolean mIsDestroy = true;
    private boolean mIsEdit = false;//判断该页面是否为编辑状态
    private Note mNote;
    private NoteBook mNoteBook;
    private DBManager mDbManager;
    private DaoSession mDaoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        if (null != getIntent().getExtras().getString(Constant.ARG2)) {
            if (getIntent().getExtras().getString(Constant.ARG2).equals(Constant.NOTE_EDIT_FLAG)) {
                mIsEdit = true;
                mNote = getIntent().getExtras().getParcelable(Constant.ARG0);
            }
        }
        mNoteBook = getIntent().getExtras().getParcelable(Constant.ARG1);
        mDbManager = DBManager.getInstance(this);
        initView();
    }

    private void initView() {
        if (mIsEdit) {
            initEditView();
        } else {
            initAddView();
        }
        getToolbar().inflateMenu(R.menu.note_menu);
        getToolbar().setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsEdit) {
                    updateNote();
                } else {
                    mIsDestroy = false;
                    saveNote();
                    if (!isSaveNote()) {
                        finish();
                    }
                }
            }
        });
    }

    /**
     * 添加笔记状态下需要初始化的View
     */
    private void initAddView() {
        setToolbarTitle(getString(R.string.note_activity_title));
    }

    /**
     * 编辑状态下需要初始化的View
     */
    private void initEditView() {
        if (mNote.getTitle().equals("")) {
            setToolbarTitle(getString(R.string.note_activity_edit_title));
        } else {
            setToolbarTitle(mNote.getTitle());
        }
        mBinding.noteEditTitle.setText(mNote.getTitle());
        mBinding.noteEditTitle.setSelection(mNote.getTitle().length());
        mBinding.noteEditContent.setText(mNote.getContent());
    }

    /**
     * 判断是否需要保存笔记
     *
     * @return
     */
    private boolean isSaveNote() {
        mTitle = getTitleText();
        mContent = getContentText();
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
            if (mIsEdit) {
                finish();
            } else {
                mIsDestroy = false;
                saveNote();
                if (!isSaveNote()) {
                    showSnackBar(mBinding.getRoot(), getString(R.string.note_input_content_alert));
                }
            }
        }
        return true;
    }

    /**
     * 保存笔记
     */
    private void saveNote() {
        if (isSaveNote()) {
            mDaoSession = mDbManager.getWriteDaoSession();
            Note note = new Note();
            note.setId(GenerateSequenceUtil.generateSequenceNo());
            note.setNotebookId(mNoteBook.getId());
            note.setNoteBook(mNoteBook);
            note.setTitle(mTitle);
            note.setContent(mContent);
            note.setCreateTime(DateUtil.getNowTime());
            mDaoSession.getNoteDao().insert(note);
            mDaoSession.clear();
            if (!mIsDestroy) {
                setResult(Constant.NOTE_BOOK_FRAGMENT_RESULT_CODE);
                finish();
            }
        }
    }

    /**
     * 更新笔记
     */
    private void updateNote() {
        if (getTitleText().equals(mNote.getTitle()) && getContentText().equals(mNote.getContent())) {
            finish();
            return;
        }
        mDaoSession = mDbManager.getWriteDaoSession();
        Note note = new Note();
        note.setId(mNote.getId());
        note.setTitle(getTitleText());
        note.setContent(getContentText());
        note.setCreateTime(mNote.getCreateTime());
        note.setUpdateTime(DateUtil.getNowTime());
        mDaoSession.getNoteDao().update(note);
        finish();
    }

    /**
     * 获取标题
     *
     * @return
     */
    private String getTitleText() {
        return mBinding.noteEditTitle.getText().toString();
    }

    /**
     * 获取内容
     *
     * @return
     */
    private String getContentText() {
        return mBinding.noteEditContent.getText().toString();
    }

    @Override
    protected void onDestroy() {
        if (mIsEdit) {
            updateNote();
        } else {
            if (mIsDestroy) {
                //直接退出
                saveNote();
            }
        }
        EventBus.getDefault().post(new NoteBookFragmentEvent.RefreshNoteEvent());
        super.onDestroy();
    }
}
