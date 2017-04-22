package com.neworin.easynotes.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by NewOrin Zhang on 2017/4/22.
 * Project : com.neworin.easynotes.utils
 * Description:
 */

public class GsonUtil {
    public static Gson getDateFormatGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public static Gson getGson() {
        return new Gson();
    }
}
