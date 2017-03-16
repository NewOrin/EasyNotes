package com.neworin.easynotes.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neworin.easynotes.BR;
import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.adapter.RecyclerViewCommonAdapter;
import com.neworin.easynotes.databinding.FragmentNoteBookBinding;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteDao;
import com.neworin.easynotes.handlers.NoteBookFragmentHandler;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.ui.BaseFragment;
import com.neworin.easynotes.utils.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class NoteBookFragment extends BaseFragment {

    private FragmentNoteBookBinding mBinding;
    private java.lang.String TAG = NoteBookFragment.class.getSimpleName();
    private View mView;
    private DBManager mDBManager;
    private List<Note> mDatas;
    private DaoSession mDaoSession;
    private NoteDao mNoteDao;
    private QueryBuilder<Note> mQB;
    private RecyclerViewCommonAdapter<Note> mAdapter;
    public NoteBookFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_note_book, container, false);
        mBinding = DataBindingUtil.bind(mView);
        EventBus.getDefault().register(this);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setHandler(new NoteBookFragmentHandler(this));
        mDBManager = DBManager.getInstance(getActivity());
        mDaoSession = mDBManager.getReadDaoSession();
        mNoteDao = mDaoSession.getNoteDao();
        mQB = mNoteDao.queryBuilder();
        mDatas = mQB.list();
        if (null != mDatas) {
            mAdapter = new RecyclerViewCommonAdapter<>(getActivity(), mDatas, R.layout.item_note_fragment_layout, BR.note);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            mBinding.noteBookFgRecyclerview.setLayoutManager(linearLayoutManager);
            mBinding.noteBookFgRecyclerview.setItemAnimator(new DefaultItemAnimator());
            mBinding.setAdapter(mAdapter);
        }
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        mAdapter.updateData(mQB.list());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.NOTE_BOOK_FRAGMENT_RESULT_CODE:
                refreshData();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDaoSession.clear();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageEvent(NoteBookFragmentEvent.RefreshNoteEvent event) {
        Log.d(TAG, "onMessageEvent刷新");
        refreshData();
    }
}