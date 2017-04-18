package com.neworin.easynotes.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.ActivityNoteLayoutBinding;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.model.EditData;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DateUtil;
import com.neworin.easynotes.utils.FileUtil;
import com.neworin.easynotes.utils.GenerateSequenceUtil;
import com.neworin.easynotes.utils.L;
import com.neworin.easynotes.view.RichTextEditor;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by NewOrin Zhang on 2017/3/13.
 * E-mail : NewOrinZhang@Gmail.com
 * 添加笔记，编辑笔记页面
 */
@RuntimePermissions
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
    private File mCurrentPhotoFile;// 照相机拍照得到的图片

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        if (null != getIntent().getExtras().getString(Constant.ARG2)) {
            if (getIntent().getExtras().getString(Constant.ARG2).equals(Constant.NOTE_EDIT_FLAG)) {
                mIsEdit = true;
                mNote = getIntent().getExtras().getParcelable(Constant.ARG0);
            }
        }
        mNoteBook = getIntent().getExtras().getParcelable(Constant.ARG1);
        mDbManager = DBManager.getInstance(this);
        if (mIsEdit) {
            initEditView();
            getRichEdtor().setHaveContent(true);
        } else {
            initAddView();
            getRichEdtor().setHaveContent(false);
        }
        getToolbar().inflateMenu(R.menu.note_menu);
        getToolbar().setOnMenuItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        initEvent();
    }

    private void initEvent(){
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
//        mBinding.noteEditContent.setText(mNote.getContent());
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
        switch (item.getItemId()) {
            //完成
            case R.id.note_menu_edit_done:
                List<EditData> dataList = getRichEdtor().buildEditData();
                for (EditData ed : dataList) {
                    L.d(ed.toString());
                }
//                if (mIsEdit) {
//                    finish();
//                } else {
//                    mIsDestroy = false;
//                    saveNote();
//                    if (!isSaveNote()) {
//                        showSnackBar(mBinding.getRoot(), getString(R.string.note_input_content_alert));
//                    }
//                }
                break;
            case R.id.note_menu_camera:
                NoteActivityPermissionsDispatcher.openCameraWithCheck(this);
//                openCamera();
                break;
            case R.id.note_menu_photo:
                openSystemAlbum();
                break;
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
        return "";
//        return mBinding.noteEditContent.getText().toString();
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

    private RichTextEditor getRichEdtor() {
        return mBinding.noteRichEditor;
    }

    /**
     * 打开系统相册
     */
    private void openSystemAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.OPEN_SYSTEM_ALBUM_RESULT_CODE);
    }

    /**
     * 打开相机
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openCamera() {
        if (FileUtil.mkDirs(Constant.CAMERA_PHOTO_DIR)) {
            mCurrentPhotoFile = new File(Constant.CAMERA_PHOTO_DIR, FileUtil.getPhotoFileName());
            Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, Constant.OPEN_CAMERA_RESULT_CODE);
        } else {
            showSnackBar(mBinding.getRoot(), getString(R.string.note_make_dirs_failed));
        }
    }

    public Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constant.OPEN_SYSTEM_ALBUM_RESULT_CODE) {
            Uri uri = data.getData();
            insertBitmap(FileUtil.getRealFilePath(uri, this));
        } else if (requestCode == Constant.OPEN_CAMERA_RESULT_CODE) {
            insertBitmap(mCurrentPhotoFile.getAbsolutePath());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        NoteActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 添加图片到RichEditor
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath) {
        getRichEdtor().insertImage(imagePath);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForCamera() {
        showSnackBar(mBinding.getRoot(), getString(R.string.note_get_permission_failed));
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForCamera() {
        showSnackBar(mBinding.getRoot(), getString(R.string.note_show_grant_permission_hint));
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForCamera(final PermissionRequest request) {
        showSnackBar(mBinding.getRoot(), getString(R.string.note_get_permission_failed));
    }
}
