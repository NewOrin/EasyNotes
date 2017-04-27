package com.neworin.easynotes.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.neworin.easynotes.model.User;

/**
 * Created by NewOrin Zhang on 2017/4/20.
 * Project : com.neworin.easynotes.utils
 * Description:
 */

public class SharedPreferenceUtil {

    private static SharedPreferences getInstance(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 存放String类型
     *
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        getInstance(context).edit().putString(key, value).apply();
    }

    /**
     * 获取String类型
     *
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        return getInstance(context).getString(key, null);
    }

    /**
     * 插入用户信息
     *
     * @param context
     * @param user
     */
    public static void insertUserInfo(Context context, User user) {
        putString(context, Constant.USER_ID, String.valueOf(user.getId()));
        putString(context, Constant.USER_EMAIL, user.getEmail());
        putString(context, Constant.USER_PASSWORD, user.getPassword());
        putString(context, Constant.USER_NICKNAME, user.getNickname());
    }

    /**
     * 清除用户信息
     *
     * @param context
     */
    public static void clearUserInfo(Context context) {
        putString(context, Constant.USER_ID, "");
        putString(context, Constant.USER_EMAIL, "");
        putString(context, Constant.USER_PASSWORD, "");
        putString(context, Constant.USER_NICKNAME, "");
    }
}
