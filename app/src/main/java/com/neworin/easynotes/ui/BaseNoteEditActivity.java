package com.neworin.easynotes.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.neworin.easynotes.utils.GenerateSequenceUtil;
import com.neworin.easynotes.utils.L;
import com.neworin.easynotes.view.RichTextEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by NewOrin Zhang on 2017/4/20.
 * Project : com.neworin.easynotes.ui
 * Description:
 */

public abstract class BaseNoteEditActivity extends BaseAppCompatActivity implements Toolbar.OnMenuItemClickListener {
    protected Note mNote;
    protected NoteBook mNoteBook;
    protected ActivityNoteLayoutBinding mBinding;
    protected DBManager mDbManager;
    protected DaoSession mDaoSession;
    protected File mCurrentPhotoFile;// 照相机拍照得到的图片
    private String TAG = BaseNoteEditActivity.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_note_layout;
    }

    abstract protected void initView();

    abstract protected void onBackPress();

    /**
     * 打开系统相册
     */
    protected void openSystemAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.OPEN_SYSTEM_ALBUM_RESULT_CODE);
    }

    /**
     * 打开相机
     */
    protected void openCamera() {
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
        String imagePath;
        if (requestCode == Constant.OPEN_SYSTEM_ALBUM_RESULT_CODE) {
            Uri uri = data.getData();
            imagePath = saveImage(BitmapFactory.decodeFile(FileUtil.getRealFilePath(uri, this)));
            insertBitmap(imagePath);
        } else if (requestCode == Constant.OPEN_CAMERA_RESULT_CODE) {
            imagePath = saveImage(BitmapFactory.decodeFile(mCurrentPhotoFile.getAbsolutePath()));
            insertBitmap(imagePath);
        }
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

    /**
     * 将图片另存为
     *
     * @param bitmap
     * @return
     */
    public String saveImage(Bitmap bitmap) {
        File fileDir = new File(Constant.FILE_SAVE_PATH);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String filePath = Constant.FILE_SAVE_PATH + "/" + GenerateSequenceUtil.generateSequenceNo() + ".jpg";
        File imageFile = new File(filePath);
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        L.d(TAG, "图片保存路径 = " + filePath);
        return filePath;
    }
}
