package com.neworin.easynotes.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neworin.easynotes.R;

/**
 * Created by NewOrin Zhang
 * 02/16/2017
 * Project: EasyNotes
 * Email: neworin@163.com
 */

public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        afterCreate(savedInstanceState);
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);

    public void showSnackBar(View view, String msg) {
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackBarWithAction(View view, String msg, String actionMsg, View.OnClickListener listener) {
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).setAction(actionMsg,listener).show();
    }

    public View getRootView() {
        return mRootView;
    }

    public void setRootView(View rootView) {
        mRootView = rootView;
    }

    /**
     * 显示加载对话框
     */
    protected void showProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.show();
    }

    /**
     * 关闭加载对话框
     */
    protected void closeProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }
}
