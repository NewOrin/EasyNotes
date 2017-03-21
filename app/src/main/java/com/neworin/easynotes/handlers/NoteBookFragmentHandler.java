package com.neworin.easynotes.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.ui.BaseFragment;
import com.neworin.easynotes.ui.activity.NoteActivity;
import com.neworin.easynotes.utils.Constant;

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
        Intent intent = new Intent(mBaseFragment.getContext(), NoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.ARG1, mNoteBook);
        bundle.putString(Constant.ARG2, Constant.NOTE_ADD_FLAG);
        intent.putExtras(bundle);
        mBaseFragment.startActivityForResult(intent, Constant.NOTE_BOOK_FRAGMENT_RESULT_CODE);
    }
}
