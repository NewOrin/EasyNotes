package com.neworin.easynotes.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

import com.neworin.easynotes.R;

/**
 * Created by NewOrin Zhang on 2017/3/18.
 * E-mail : NewOrinZhang@Gmail.com
 * 对话框工具类
 */

public class DialogUtils {

    private Context mContext;
    private EditText mEditText;

    public DialogUtils(Context context) {
        this.mContext = context;
    }

    public void showAlertDialog(String alertMsg, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(items, listener);
        builder.create().show();
    }

    /**
     * 带有EditText对话框
     *
     * @param titleId
     * @param listener
     */
    public void showEditTextDialog(int titleId, DialogInterface.OnClickListener listener) {
        mEditText = new EditText(mContext);
        mEditText.setTextColor(Color.BLACK);
        mEditText.setSingleLine(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(titleId);
        builder.setView(mEditText);
        builder.setPositiveButton(R.string.confirm, listener);
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    /**
     * 创建显示详情对话框
     *
     * @param view
     */
    public void showInfoDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setPositiveButton(R.string.confirm, null);
        builder.create().show();
    }
    /**
     * 获取EditText值
     *
     * @return
     */
    public String getEditText() {
        if (null != mEditText) {
            return mEditText.getText().toString();
        }
        return "";
    }
}
