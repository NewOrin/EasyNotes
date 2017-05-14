package com.neworin.easynotes.utils;

import android.os.Environment;

/**
 * Created by NewOrin Zhang on 2017/2/20.
 * E-Mail : NewOrinZhang@Gmail.com
 */

public class Constant {
    public static final int SLIDE_ITEM_SETTINGS = -10;
    public static final int SLIDE_ITEM_EDIT = -20;
    public static final int SLIDE_ITEM_RECYCLE = -30;
    public static final String DB_NAME = "easynote_db";

    public static final int NOTE_BOOK_FRAGMENT_RESULT_CODE = 1000;
    public static final int EDIT_BOOK_RESULT_CODE = 1001;
    public static final int OPEN_SYSTEM_ALBUM_RESULT_CODE = 1002;
    public static final int OPEN_CAMERA_RESULT_CODE = 1003;
    public static final int CONTENT_STR_FLAG = 1004;
    public static final int CONTENT_IMAGE_FLAG = 1005;

    public static final String ARG0 = "arg0";
    public static final String ARG1 = "arg1";
    public static final String ARG2 = "arg2";

    public static final String NOTE_ADD_FLAG = "note_add_flag";
    public static final String NOTE_EDIT_FLAG = "note_edit_flag";

    public static final String CAMERA_PHOTO_DIR = Environment.getExternalStorageDirectory() + "/DCIM/Camera";
    public static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/easynotes/caches";
    public static final String FILE_SAVE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/easynotes/files";

    public static final String SHARED_PREF_NAME = "easynote_shred_pref";

    public static final String SCREEN_WIDTH = "screen_width";
    public static final String SCREEN_HEIGHT = "screen_height";

    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_NICKNAME = "nickname";
    public static final String USER_AVATAR_URL = "user_avatar_url";
    public static final String USER_SYNC_TIME = "user_sync_time";


    public static final String BASE_API_URL = "http://192.168.31.208:8080";
    public static final String GET_AVATAR_URL = BASE_API_URL + "/file/get_user_avatar/";


    public static final int STATUS_ADD = 2; //新增
    public static final int STATUS_DELETE = -1; //删除
    public static final int STATUS_UPDATE = 1; //更新
    public static final int STATUS_COMPLETED = 9;//同步完成
    public static final int STATUS_FAILED = -9;//同步失败
}
