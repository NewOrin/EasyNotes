package com.neworin.easynotes.handlers;

import android.view.View;

import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.ui.BaseFragment;

/**
 * Created by NewOrin Zhang on 2017/3/13.
 * E-mail : NewOrinZhang@Gmail.com
 */

public class NoteBookFragmentHandler {

    private BaseFragment mBaseFragment;
    private NoteBook mNoteBook;

    public NoteBookFragmentHandler() {
    }

    public NoteBookFragmentHandler(BaseFragment mBaseFragment, NoteBook noteBook) {
        this.mBaseFragment = mBaseFragment;
        this.mNoteBook = noteBook;
    }

    public void onFloatingActionClick(View v) {

    }
}
