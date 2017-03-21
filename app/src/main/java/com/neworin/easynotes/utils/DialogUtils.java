package com.neworin.easynotes.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.neworin.easynotes.R;

/**
 * Created by NewOrin Zhang on 2017/3/18.
 * E-mail : NewOrinZhang@Gmail.com
 * 对话框工具类
 */

public class DialogUtils {

    private Context mContext;

    public DialogUtils(Context context) {
        this.mContext = context;
    }

    public void showAlertDialog(String alertMsg, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.EasyNoteDialogTheme);
        builder.setTitle(alertMsg).setPositiveButton(R.string.confirm, listener).setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    /**
     * 列表显示对话框
     *
     * @param items
     * @param listener
     */
    public void showItemDialog(CharSequence[] items, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.EasyNoteDialogTheme);
        builder.setTitle(R.string.note_book_choose);
        builder.setItems(items, listener);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
