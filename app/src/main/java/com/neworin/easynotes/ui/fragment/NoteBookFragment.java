package com.neworin.easynotes.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.neworin.easynotes.BR;
import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.adapter.RecyclerViewCommonAdapter;
import com.neworin.easynotes.databinding.FragmentNoteBookBinding;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.event.SlideMenuEvent;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteDao;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.model.NoteManager;
import com.neworin.easynotes.ui.BaseFragment;
import com.neworin.easynotes.ui.activity.EditActivity;
import com.neworin.easynotes.ui.activity.MainActivity;
import com.neworin.easynotes.ui.activity.NoteActivity;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DialogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 显示笔记列表页面
 */

public class NoteBookFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewCommonAdapter.OnItemLongClickListener, View.OnClickListener {

    private FragmentNoteBookBinding mBinding;
    private java.lang.String TAG = NoteBookFragment.class.getSimpleName();
    private DBManager mDBManager;
    private List<Note> mDatas;
    private DaoSession mDaoSession;
    private NoteDao mNoteDao;
    private NoteBook mNoteBook;
    private QueryBuilder<Note> mQueryBuilder;
    private RecyclerViewCommonAdapter<Note> mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DialogUtils mDialogUtils;
    private String mDialogItems[];
    private boolean mIsThumb;
    private NoteManager mNoteManager;
    private MainActivity mMainActivity;

    public NoteBookFragment() {
    }

    public static Fragment newsInstance(Bundle bundle) {
        NoteBookFragment noteBookFragment = new NoteBookFragment();
        noteBookFragment.setArguments(bundle);
        return noteBookFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (MainActivity) activity;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_note_book;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.bind(getRootView());
        EventBus.getDefault().register(this);
        initView();
        initEvent();
    }

    private void initView() {
        mNoteManager = new NoteManager(getActivity());
        mIsThumb = false;
        mNoteBook = getArguments().getParcelable(Constant.ARG0);
        mDialogItems = new String[]{getString(R.string.note_book_delete)};
        mSwipeRefreshLayout = mBinding.noteBookFgSwipeLayout;
        mDBManager = DBManager.getInstance(getActivity());
        mDatas = getDBDatas();
        if (null != mDatas) {
            setLinearLayoutRecyclerView();
        }
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    private void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        getFAButton().setOnClickListener(this);
    }

    /**
     * 初始化列表事件
     */
    private void initItemEvent() {
        mAdapter.setmOnItemLongClickListener(this);
        mAdapter.setmOnItemClickListener(new RecyclerViewCommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constant.ARG0, mDatas.get(position));
                bundle.putParcelable(Constant.ARG1, mNoteBook);
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constant.NOTE_BOOK_FRAGMENT_RESULT_CODE);
            }
        });
    }
    /**
     * 刷新数据
     */
    private void refreshData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mAdapter.updateData(mDatas = getDBDatas());
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mDaoSession.clear();
        EventBus.getDefault().post(new SlideMenuEvent.RefreshEvent());
        mMainActivity.startUploadImageService();
    }

    /**
     * 设置线性布局RecyclerView
     */
    private void setLinearLayoutRecyclerView() {
        mAdapter = new RecyclerViewCommonAdapter<>(getActivity(), mDatas, R.layout.item_note_fragment_layout, BR.note);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.noteBookFgRecyclerview.setLayoutManager(linearLayoutManager);
        mBinding.noteBookFgRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mBinding.setAdapter(mAdapter);
        initItemEvent();
    }

    /**
     * 设置RecyclerView网格布局
     */
    private void setGridLayoutRecyclerView() {
        mAdapter = new RecyclerViewCommonAdapter<>(getActivity(), mDatas, R.layout.item_note_fragment_grid_layout, BR.note);
        getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(), 2));
        getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mBinding.setAdapter(mAdapter);
        initItemEvent();
    }

    /**
     * 设置RecyclerView标题布局
     */
    private void setTitleLayoutRecyclerView() {
        mAdapter = new RecyclerViewCommonAdapter<>(getActivity(), mDatas, R.layout.item_note_fragment_title_layout, BR.note);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.noteBookFgRecyclerview.setLayoutManager(linearLayoutManager);
        mBinding.noteBookFgRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mBinding.setAdapter(mAdapter);
        initItemEvent();
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

    /**
     * 刷新列表
     *
     * @param event
     */
    @Subscribe
    public void onMessageEvent(NoteBookFragmentEvent.RefreshNoteEvent event) {
        refreshData();
    }

    @Subscribe
    public void onMessageEvent(NoteBookFragmentEvent.ShowThumbEvent event) {
        if (mIsThumb) {
            setLinearLayoutRecyclerView();
            mIsThumb = false;
        } else {
            setGridLayoutRecyclerView();
            mIsThumb = true;
        }
    }

    @Subscribe
    public void onMessageEvent(NoteBookFragmentEvent.ShowTitleEvent event) {
        setTitleLayoutRecyclerView();
    }

    @Subscribe
    public void onMessageEvent(SlideMenuEvent.ListItemEvent event) {
        mNoteBook = event.getNoteBook();
        refreshData();
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    /**
     * RecyclerView长按事件
     *
     * @param position
     */
    @Override
    public void onItemLongClick(final int position) {
        mDialogUtils = new DialogUtils(getActivity());
        mDialogUtils.showItemDialog(mDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showDeleteDialog(position);
                }
            }
        });
    }

    /**
     * 显示删除条目对话框
     *
     * @param position
     */
    private void showDeleteDialog(final int position) {
        final Note note = mDatas.get(position);
        final int status = note.getStatus();
        mDialogUtils = new DialogUtils(getActivity());
        mDialogUtils.showAlertDialog(getString(R.string.cofirm_delete_hint), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem(position);
                showSnackBarWithAction(getRootView(), getString(R.string.edit_notebook_success), getString(R.string.edit_notebook_recall), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        note.setIsDelete(0);
                        note.setStatus(status);
                        mNoteManager.update(note);
                        refreshData();
                    }
                });
                mAdapter.notifyItemRemoved(position);
                refreshData();
            }
        });
    }

    /**
     * 删除笔记本记录
     *
     * @param position
     */
    private void deleteItem(int position) {
        mDaoSession = mDBManager.getWriteDaoSession();
        mNoteDao = mDaoSession.getNoteDao();
        Note note = mDatas.get(position);
        note.setStatus(Constant.STATUS_DELETE);
        note.setIsDelete(1);
        mNoteDao.update(note);
    }

    @Override
    public void onClick(View v) {
        if (v == getFAButton()) {
            Intent intent = new Intent(getActivity(), NoteActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constant.ARG1, mNoteBook);
            intent.putExtras(bundle);
            getActivity().startActivityForResult(intent, Constant.NOTE_BOOK_FRAGMENT_RESULT_CODE);
        }
    }

    /**
     * 获取RecyclerView对象
     *
     * @return
     */
    private RecyclerView getRecyclerView() {
        return mBinding.noteBookFgRecyclerview;
    }

    /**
     * 获取FloatingActionButton对象
     *
     * @return
     */
    private FloatingActionButton getFAButton() {
        return mBinding.noteBookFgFloatBtn;
    }

    private List<Note> getDBDatas() {
        mDaoSession = mDBManager.getReadDaoSession();
        mNoteDao = mDaoSession.getNoteDao();
        mQueryBuilder = mNoteDao.queryBuilder().where(NoteDao.Properties.NotebookId.eq(mNoteBook.getId()), NoteDao.Properties.IsDelete.eq(0)).orderDesc(NoteDao.Properties.CreateTime);
        mDaoSession.clear();
        return mDatas = mQueryBuilder.list();
    }
}