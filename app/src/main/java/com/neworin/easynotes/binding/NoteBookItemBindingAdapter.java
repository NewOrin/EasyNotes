package com.neworin.easynotes.binding;

import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.neworin.easynotes.cache.MyBitmapUtils;
import com.neworin.easynotes.model.EditData;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DateUtil;

import java.io.File;
import java.util.List;

/**
 * Created by NewOrin Zhang on 2017/4/21.
 * Project : com.neworin.easynotes.utils
 * Description:
 */

public class NoteBookItemBindingAdapter {

    private static String TAG = NoteBookItemBindingAdapter.class.getSimpleName();

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
        boolean isHaveImage = false;
        List<EditData> list = JSON.parseArray(content, EditData.class);
        if (null == list || list.size() == 0) {
            imageView.setVisibility(View.GONE);
            return;
        }
        MyBitmapUtils myBitmapUtils = new MyBitmapUtils();
        for (EditData e : list) {
            if (!isHaveImage) {
                if (e.getImagePath() != null) {
                    imageView.setVisibility(View.VISIBLE);
                    File file = new File(e.getImagePath());
                    if (file.exists()) {
                        Glide.with(imageView.getContext()).load(e.getImagePath()).into(imageView);
                    } else {
                        String[] args = e.getImagePath().split("/");
                        myBitmapUtils.disPlay(imageView, Constant.GET_NOTE_IMAGE_URL + args[args.length - 1] + "/20");
                    }
                    isHaveImage = true;
                }
            }
        }
        if (!isHaveImage) {
            imageView.setVisibility(View.GONE);
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

    @BindingAdapter("gridTextView")
    public static void setGridTextView(TextView textView, String content) {
        boolean isHaveImage = false;
        StringBuilder sb = new StringBuilder();
        List<EditData> list = JSON.parseArray(content, EditData.class);
        if (null == list || list.size() == 0) {
            textView.setVisibility(View.GONE);
            return;
        }
        for (EditData e : list) {
            if (e.getImagePath() == null) {
                sb.append(e.getInputStr());
            } else {
                isHaveImage = true;
            }
        }
        if (isHaveImage) {
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setTextColor(Color.BLACK);
            textView.setSingleLine(false);
        }
        textView.setText(sb.toString());
    }

    @BindingAdapter("timeTextView")
    public static void setTimeTextView(TextView textView, Note note) {
        boolean isHaveImage = false;
        String content = note.getContent();
        List<EditData> list = JSON.parseArray(content, EditData.class);
        if (null == list || list.size() == 0) {
            textView.setTextColor(Color.BLACK);
            return;
        }
        for (EditData e : list) {
            if (e.getImagePath() != null) {
                isHaveImage = true;
            }
        }
        if (note.getCreateTime() == null) {
            textView.setText(DateUtil.formatFriendly(note.getUpdateTime()));
        } else {
            textView.setText(DateUtil.formatFriendly(note.getCreateTime()));
        }
        if (isHaveImage) {
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setTextColor(Color.BLACK);
        }
    }

    @BindingAdapter("titleTextView")
    public static void setTitleTextView(TextView textview, Note note) {
        if (note.getTitle().equals("")) {
            StringBuilder sb = new StringBuilder();
            List<EditData> list = JSON.parseArray(note.getContent(), EditData.class);
            if (null == list || list.size() == 0) {
                textview.setVisibility(View.GONE);
                return;
            }
            for (EditData e : list) {
                if (e.getImagePath() == null) {
                    sb.append(e.getInputStr());
                }
            }
            textview.setText(sb.toString());
        } else {
            textview.setText(note.getTitle());
        }
    }
}
