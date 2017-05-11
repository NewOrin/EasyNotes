package com.neworin.easynotes.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.neworin.easynotes.utils.L;
import com.neworin.easynotes.utils.MD5Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static com.neworin.easynotes.utils.Constant.CACHE_PATH;

/**
 * 三级缓存之本地缓存
 */
public class LocalCacheUtils {

    private final String TAG = LocalCacheUtils.class.getSimpleName();
    /**
     * 从本地读取图片
     *
     * @param url
     */
    public Bitmap getBitmapFromLocal(String url) {
        String fileName = null;//把图片的url当做文件名,并进行MD5加密
        try {
            fileName = MD5Encoder.encode(url);
            File file = new File(CACHE_PATH, fileName);

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从网络获取图片后,保存至本地缓存
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            String fileName = MD5Encoder.encode(url);//把图片的url当做文件名,并进行MD5加密
            File file = new File(CACHE_PATH, fileName);
            L.d(TAG, "已保存到本地 = " + file.getAbsolutePath());
            //通过得到文件的父文件,判断父文件是否存在
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            //把图片保存至本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}