package com.neworin.easynotes.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.neworin.easynotes.R;

/**
 * Created by NewOrin Zhang on 2017/4/27.
 * Project : com.neworin.easynotes.utils
 * Description:
 */

public class GlideUtils {

    public static void loadLogo(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().centerCrop().placeholder(R.drawable.ic_default_avatar).error(R.drawable.ic_default_avatar).into(imageView);
    }
}
