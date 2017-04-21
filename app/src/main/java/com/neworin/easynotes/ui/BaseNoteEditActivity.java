package com.neworin.easynotes.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.ActivityNoteLayoutBinding;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.model.EditData;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.FileUtil;
import com.neworin.easynotes.view.RichTextEditor;

import java.io.File;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by NewOrin Zhang on 2017/4/20.
 * Project : com.neworin.easynotes.ui
 * Description:
 */

@RuntimePermissions
public abstract class BaseNoteEditActivity extends BaseAppCompatActivity implements Toolbar.OnMenuItemClickListener {
    protected Note mNote;
    protected NoteBook mNoteBook;
    protected ActivityNoteLayoutBinding mBinding;
    protected DBManager mDbManager;
    protected DaoSession mDaoSession;
    protected File mCurrentPhotoFile;// 照相机拍照得到的图片

    @Override
    protected int getLayoutId() {
        return R.layout.activity_note_layout;
    }

    abstract protected void initView();

    abstract protected void onBackPress();

    /**
     * 检查是否授予拍照权限
     */
    protected void checkCamera() {
        BaseNoteEditActivityPermissionsDispatcher.openCameraWithCheck(this);
    }

    /**
     * 检查是否获取读取内存权限
     */
    protected void checkAlbum() {
        BaseNoteEditActivityPermissionsDispatcher.openSystemAlbumWithCheck(this);
    }

    /**
     * 打开系统相册
     */
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void openSystemAlbum() {
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
        BaseNoteEditActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 获取内容
     *
     * @return
     */
    protected String getContentText() {
        List<EditData> dataList = getRichEdtor().buildEditData();
        if (dataList.size() == 1 && dataList.get(0).getImagePath() == null && dataList.get(0).getInputStr().equals("")) {
            return null;
        }
        return JSON.toJSONString(dataList);
    }

    /**
     * 获取标题
     *
     * @return
     */
    protected String getTitleText() {
        return mBinding.noteEditTitle.getText().toString();
    }

    /**
     * 添加图片到RichEditor
     *
     * @param imagePath
     */
    protected void insertBitmap(String imagePath) {
        getRichEdtor().insertImage(imagePath);
    }

    protected RichTextEditor getRichEdtor() {
        return mBinding.noteRichEditor;
    }

    @Override
    public void onBackPressed() {
        onBackPress();
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
