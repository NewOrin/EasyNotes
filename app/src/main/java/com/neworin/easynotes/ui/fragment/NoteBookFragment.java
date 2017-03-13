package com.neworin.easynotes.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neworin.easynotes.BR;
import com.neworin.easynotes.R;
import com.neworin.easynotes.adapter.RecyclerViewCommonAdapter;
import com.neworin.easynotes.databinding.FragmentNoteBookBinding;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.ui.BaseFragment;
import com.neworin.easynotes.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class NoteBookFragment extends BaseFragment {

    private FragmentNoteBookBinding mBinding;
    private java.lang.String TAG = NoteBookFragment.class.getSimpleName();
    private View mView;

    public NoteBookFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_note_book, container, false);
        mBinding = DataBindingUtil.bind(mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Note> noteList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            noteList.add(new Note(i, "今天喝排骨汤", DateUtil.getNowDate()));
        }
        RecyclerViewCommonAdapter<Note> adapter = new RecyclerViewCommonAdapter<>(getActivity(), noteList, R.layout.item_note_fragment_layout, BR.note);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.noteBookFgRecyclerview.setLayoutManager(linearLayoutManager);
        mBinding.setAdapter(adapter);
    }
}