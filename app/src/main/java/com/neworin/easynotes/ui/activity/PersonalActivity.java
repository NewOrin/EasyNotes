package com.neworin.easynotes.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;

import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.ActivityPersonalLayoutBinding;
import com.neworin.easynotes.event.SlideMenuEvent;
import com.neworin.easynotes.http.FileUploadBizImpl;
import com.neworin.easynotes.http.Response;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DialogUtils;
import com.neworin.easynotes.utils.ImageUtil;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by NewOrin Zhang on 2017/4/22.
 * Project : com.neworin.easynotes.ui.activity
 * Description:
 */
@RuntimePermissions
public class PersonalActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ActivityPersonalLayoutBinding mBinding;
    private static final int CAMERA_CODE = 1;
    private static final int GALLERY_CODE = 2;
    private static final int CROP_CODE = 3;
    private String[] mChooseItems = {"拍照", "图库"};
    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_layout;
    }

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        getToolbar().setTitle(R.string.personal_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        initEvent();
        setAvatar();
    }

    private void initEvent() {
        mBinding.personalChangeAvatarLayout.setOnClickListener(this);
        mBinding.personalExitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBinding.personalChangeAvatarLayout) {
            showChooseDialog();
        } else if (v == mBinding.personalExitBtn) {
        }
    }

    /**
     * 拍照选择
     */
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void chooseFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CODE);
    }

    /**
     * 从相册选择图片
     */
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_CODE:
                //用户点击了取消
                if (data == null) return;
                Bundle extras = data.getExtras();
                if (null != extras) {
                    //获得拍照图片
                    Bitmap bm = extras.getParcelable("data");
                    Uri uri = ImageUtil.saveBitmap(bm);
                    //图像裁剪
                    startImageZoom(uri);
                }
                break;
            case GALLERY_CODE:
                if (null == data) return;
                //从相册选择图片后返回的Uri
                Uri uri = convertUri(data.getData());
                startImageZoom(uri);
                break;
            case CROP_CODE:
                if (null == data) return;
                Bundle bundle = data.getExtras();
                if (null != bundle) {
                    //获取裁剪后的图像
                    Bitmap bm = bundle.getParcelable("data");
                    ImageUtil.saveLogo(this, bm);
                    showProgressDialog();
                    mBinding.personalAvatarImage.setImageBitmap(bm);
                    uploadAvatar();
                    EventBus.getDefault().post(new SlideMenuEvent.RefreshAvatarEvent());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置头像
     */
    private void setAvatar() {
        String avatar_path = SharedPreferenceUtil.getString(this, Constant.USER_AVATAR_URL);
        if (avatar_path != null && !avatar_path.equals("")) {
            Bitmap bitmap = BitmapFactory.decodeFile(avatar_path);
            mBinding.personalAvatarImage.setImageBitmap(bitmap);
        }
    }

    /**
     * 上传头像到服务器
     */
    private void uploadAvatar() {
        String path = SharedPreferenceUtil.getString(this, Constant.USER_AVATAR_URL);
        if (path != null && !path.equals("")) {
            FileUploadBizImpl uploadBiz = new FileUploadBizImpl();
            uploadBiz.uploadFile(path, SharedPreferenceUtil.getString(this, Constant.USER_EMAIL), new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    closeProgressDialog();
                    showSnackBar(mBinding.getRoot(),getString(R.string.upload_avatar_success));
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    closeProgressDialog();
                    showSnackBar(mBinding.getRoot(),getString(R.string.upload_avatar_failed));
                }
            });
        }
    }

    /**
     * 将content类型的Uri转化为文件类型的Uri
     *
     * @param uri
     * @return
     */
    private Uri convertUri(Uri uri) {
        InputStream is;
        try {
            //Uri ----> InputStream
            is = getContentResolver().openInputStream(uri);
            //InputStream ----> Bitmap
            Bitmap bm = BitmapFactory.decodeStream(is);
            //关闭流
            is.close();
            return ImageUtil.saveBitmap(bm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过Uri传递图像信息以供裁剪
     *
     * @param uri
     */
    private void startImageZoom(Uri uri) {
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_CODE);
    }

    /**
     * 更换图片选择对话框
     */
    private void showChooseDialog() {
        DialogUtils dialogUtils = new DialogUtils(this);
        dialogUtils.showItemDialog(mChooseItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    PersonalActivityPermissionsDispatcher.chooseFromCameraWithCheck(PersonalActivity.this);
                } else {
                    PersonalActivityPermissionsDispatcher.chooseFromGalleryWithCheck(PersonalActivity.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PersonalActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
