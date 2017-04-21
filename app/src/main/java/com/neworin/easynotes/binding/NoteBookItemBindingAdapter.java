package com.neworin.easynotes.binding;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.neworin.easynotes.model.EditData;

import java.util.List;

/**
 * Created by NewOrin Zhang on 2017/4/21.
 * Project : com.neworin.easynotes.utils
 * Description:
 */

public class NoteBookItemBindingAdapter {

    public static boolean isShow(String content) {
        List<EditData> list = JSON.parseArray(content, EditData.class);
        for (EditData e : list) {
            if (e.getImagePath() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置列表图片
     *
     * @param imageView
     * @param content
     */
    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String content) {
        List<EditData> list = JSON.parseArray(content, EditData.class);
        if (null == list || list.size() == 0) {
            imageView.setVisibility(View.GONE);
            return;
        }
        for (EditData e : list) {
            if (e.getImagePath() != null) {
                Glide.with(imageView.getContext()).load(e.getImagePath()).into(imageView);
                return;
            }
        }
    }

    @BindingAdapter("textView")
    public static void setTextView(TextView textView, String content) {
        StringBuilder sb = new StringBuilder();
        List<EditData> list = JSON.parseArray(content, EditData.class);
        if (null == list || list.size() == 0) {
            textView.setVisibility(View.GONE);
            return;
        }
        for (EditData e : list) {
            if (e.getImagePath() == null) {
                sb.append(e.getInputStr());
            }
        }
        textView.setText(sb.toString());
    }
}
