package com.neworin.easynotes.ui;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by NewOrin Zhang
 * 02/16/2017
 * Project: EasyNotes
 * Email: neworin@163.com
 */

public class BaseFragment extends Fragment {

    public void showSnackBar(View view,String msg) {
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackBarWithAction(View view, String msg, String actionMsg, View.OnClickListener listener) {
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).setAction(actionMsg,listener).show();
    }
}
