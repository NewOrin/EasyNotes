package com.neworin.easynotes;

import android.app.Application;
import android.util.DisplayMetrics;

import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

/**
 * Created by NewOrin Zhang on 2017/2/23.
 * E-Mail : NewOrinZhang@Gmail.com
 */

public class EApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        SharedPreferenceUtil.putString(this, Constant.SCREEN_WIDTH, String.valueOf(dm.widthPixels));
        SharedPreferenceUtil.putString(this, Constant.SCREEN_HEIGHT, String.valueOf(dm.heightPixels));
    }
}
