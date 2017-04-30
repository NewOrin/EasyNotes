package com.neworin.easynotes.ui.fragment;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.neworin.easynotes.BR;
import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.adapter.RecyclerViewCommonAdapter;
import com.neworin.easynotes.databinding.EditNotebookInfoLayoutBinding;
import com.neworin.easynotes.databinding.FragmentEditnotebookBinding;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteBookDao;
import com.neworin.easynotes.greendao.gen.NoteDao;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.model.NoteBookManager;
import com.neworin.easynotes.ui.BaseFragment;
import com.neworin.easynotes.utils.DateUtil;
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
    private NoteBookManager mNoteBookManager;
    private EditNotebookInfoLayoutBinding mNoteBookInfoBinding;

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
        mNoteBookManager = new NoteBookManager(getActivity());
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
                showMoreInfoDialog(position);
            }
        });
        mAdapter.setmOnItemLongClickListener(new RecyclerViewCommonAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                showMoreInfoDialog(position);
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
        mNoteBookList = mDBManager.getWriteDaoSession().getNoteBookDao().queryBuilder().where(NoteDao.Properties.IsDelete.eq(0)).list();
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
            nb.setCount(mDaoSession.getNoteDao().queryBuilder().where(NoteDao.Properties.NotebookId.eq(nb.getId()),NoteDao.Properties.IsDelete.eq(0)).list().size());
        }
        mDaoSession.clear();
        return list;
    }

    /**
     * 显示更多对话框
     */
    private void showMoreInfoDialog(final int pos) {
        mDialogUtils.showItemDialog(mShowDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleMoreInfo(which, pos);
            }
        });
    }

    /**
     * 处理NoteBook
     *
     * @param which
     */
    private void handleMoreInfo(int which, int pos) {
        switch (which) {
            case 0:
                reNameNoteBook(pos);
                break;
            case 1:
                showNoteBookInfoDialog(pos);
                break;
            case 2:
                handleDeleteNoteBook(pos);
                break;
        }
    }

    /**
     * NoteBook重命名
     *
     * @param pos
     */
    private void reNameNoteBook(final int pos) {
        mDialogUtils.showEditTextDialog(R.string.edit_notebook_rename, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNoteBookList.get(pos).setName(mDialogUtils.getEditText());
                mNoteBookList.get(pos).setUpdateTime(DateUtil.getNowTime());
                mNoteBookManager.update(mNoteBookList.get(pos));
                refreshData();
            }
        });
    }

    /**
     * 显示NoteBook详情
     *
     * @param pos
     */
    private void showNoteBookInfoDialog(int pos) {
        mNoteBookInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.edit_notebook_info_layout, null, false);
        mNoteBookInfoBinding.setNotebook(mNoteBookList.get(pos));
        mDialogUtils.showInfoDialog(mNoteBookInfoBinding.getRoot());
    }

    /**
     * 处理笔记本删除
     *
     * @param pos
     */
    private void handleDeleteNoteBook(final int pos) {
        final NoteBook nb = mNoteBookList.get(pos);
        if (nb.getId() == 1) {
            showSnackBar(getRootView(), getString(R.string.edit_notebook_cannot_delete_default_hint));
            return;
        }
        mDialogUtils.showAlertDialog(getString(R.string.edit_notebook_delete_alert), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNoteBookManager.deleteByKey(nb.getId());
                showSnackBarWithAction(getRootView(), getString(R.string.edit_notebook_success), getString(R.string.edit_notebook_recall), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNoteBookManager.insert(nb);
                        refreshData();
                    }
                });
                refreshData();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
