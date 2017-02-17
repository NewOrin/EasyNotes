package com.neworin.easynotes;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neworin.easynotes.databinding.FragmentNoteBookBinding;


public class NoteBookFragment extends BaseFragment {

    private FragmentNoteBookBinding binding;
    private java.lang.String TAG = NoteBookFragment.class.getSimpleName();
    private View mView;

    public NoteBookFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_note_book, container, false);
        binding = DataBindingUtil.bind(mView);
        return mView;
    }
}