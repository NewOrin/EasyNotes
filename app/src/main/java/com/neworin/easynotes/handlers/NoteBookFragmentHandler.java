package com.neworin.easynotes.handlers;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.neworin.easynotes.ui.BaseFragment;
import com.neworin.easynotes.ui.activity.NoteActivity;
import com.neworin.easynotes.utils.Constant;

/**
 * Created by NewOrin Zhang on 2017/3/13.
 * E-mail : NewOrinZhang@Gmail.com
 */

public class NoteBookFragmentHandler {

    private BaseFragment mBaseFragment;

    public NoteBookFragmentHandler() {
    }

    public NoteBookFragmentHandler(BaseFragment mBaseFragment) {
        this.mBaseFragment = mBaseFragment;
    }

    public void onFloatingActionClick(View v) {
        mBaseFragment.startActivityForResult(new Intent(mBaseFragment.getContext(), NoteActivity.class), Constant.NOTE_BOOK_FRAGMENT_RESULT_CODE);
    }
}
