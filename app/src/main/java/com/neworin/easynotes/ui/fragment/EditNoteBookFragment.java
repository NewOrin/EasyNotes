package com.neworin.easynotes.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.neworin.easynotes.BR;
import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.adapter.RecyclerViewCommonAdapter;
import com.neworin.easynotes.databinding.FragmentEditnotebookBinding;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteBookDao;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.ui.BaseFragment;

import java.util.List;

/**
 * Created by NewOrin Zhang on 2017/3/22.
 * E-mail : NewOrinZhang@Gmail.com
 */

public class EditNoteBookFragment extends BaseFragment {

    private FragmentEditnotebookBinding mBinding;
    private DBManager mDBManager;
    private DaoSession mDaoSession;
    private NoteBookDao mNoteBookDao;
    private List<NoteBook> mNoteBookList;
    private RecyclerViewCommonAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_editnotebook;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.bind(getRootView());
        mDBManager = DBManager.getInstance(getActivity());
        initViewData();
    }

    private void initViewData() {
        mNoteBookList = mDBManager.getWriteDaoSession().getNoteBookDao().queryBuilder().list();
        mAdapter = new RecyclerViewCommonAdapter(getActivity(), mNoteBookList, R.layout.item_slide_menu_layout, BR.notebookBean);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        getRecyclerView().setLayoutManager(linearLayoutManager);
        getRecyclerView().setAdapter(mAdapter);
    }

    private RecyclerView getRecyclerView() {
        return mBinding.fragmentContainerRecyclerview;
    }
}
