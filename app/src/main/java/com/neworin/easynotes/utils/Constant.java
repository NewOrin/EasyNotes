package com.neworin.easynotes.utils;

import android.os.Environment;

/**
 * Created by NewOrin Zhang on 2017/2/20.
 * E-Mail : NewOrinZhang@Gmail.com
 */

public class Constant {
    public static final int SLIDE_ITEM_SETTINGS = -1;
    public static final int SLIDE_ITEM_EDIT = -2;
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

    public static final String SHARED_PREF_NAME = "easynote_shred_pref";

    public static final String SCREEN_WIDTH = "screen_width";
    public static final String SCREEN_HEIGHT = "screen_height";

    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_AVATAR = "avatar";
    public static final String USER_NICKNAME = "nickname";
    public static final String USER_AVATAR_URL = "avatar";
}
