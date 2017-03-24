package com.neworin.easynotes.ui.fragment;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.neworin.easynotes.BR;
import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.adapter.RecyclerViewCommonAdapter;
import com.neworin.easynotes.databinding.FragmentEditnotebookBinding;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteBookDao;
import com.neworin.easynotes.greendao.gen.NoteDao;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.ui.BaseFragment;
import com.neworin.easynotes.utils.DialogUtils;
import com.neworin.easynotes.view.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private DialogUtils mDialogUtils;
    private String[] mShowDialogItems = {"重命名", "查看详情", "删除"};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_editnotebook;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mBinding = DataBindingUtil.bind(getRootView());
        mDBManager = DBManager.getInstance(getActivity());
        initViewData();
        initEvent();
    }

    private void initViewData() {
        mDialogUtils = new DialogUtils(getActivity());
        mNoteBookList = mDBManager.getWriteDaoSession().getNoteBookDao().queryBuilder().list();
        mNoteBookList = setNoteBookCount(mNoteBookList);
        mAdapter = new RecyclerViewCommonAdapter(getActivity(), mNoteBookList, R.layout.item_edit_notebook_layout, BR.notebookBean);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        getRecyclerView().setLayoutManager(linearLayoutManager);
        getRecyclerView().addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        getRecyclerView().setAdapter(mAdapter);
    }

    private void initEvent() {
        mAdapter.setOnMoreInfoClickListener(new RecyclerViewCommonAdapter.OnMoreInfoClickListener() {
            @Override
            public void onMoreInfoClick(int position) {
                showMoreInfoDialog();
            }
        });
        mAdapter.setmOnItemLongClickListener(new RecyclerViewCommonAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                showMoreInfoDialog();
            }
        });
    }

    private RecyclerView getRecyclerView() {
        return mBinding.fragmentContainerRecyclerview;
    }

    @Subscribe
    public void onMessageEvent(NoteBookFragmentEvent.RefreshNoteEvent event) {
        refreshData();
    }

    /**
     * 刷新列表
     */
    private void refreshData() {
        mNoteBookList = mDBManager.getWriteDaoSession().getNoteBookDao().queryBuilder().list();
        mNoteBookList = setNoteBookCount(mNoteBookList);
        mAdapter.updateData(mNoteBookList);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置NoteBookList count
     *
     * @param list
     * @return
     */
    private List<NoteBook> setNoteBookCount(List<NoteBook> list) {
        mDaoSession = mDBManager.getReadDaoSession();
        for (NoteBook nb : list) {
            nb.setCount(mDaoSession.getNoteDao().queryBuilder().where(NoteDao.Properties.NotebookId.eq(nb.getId())).list().size());
        }
        mDaoSession.clear();
        return list;
    }

    private void showMoreInfoDialog(){
        mDialogUtils.showItemDialog(mShowDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
