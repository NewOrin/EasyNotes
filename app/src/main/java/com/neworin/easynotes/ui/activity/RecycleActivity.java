package com.neworin.easynotes.ui.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.neworin.easynotes.BR;
import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.adapter.RecyclerViewCommonAdapter;
import com.neworin.easynotes.databinding.ActivityRecycleLayoutBinding;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.event.SlideMenuEvent;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteDao;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DialogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by NewOrin Zhang on 2017/4/28.
 * Project : com.neworin.easynotes.ui.activity
 * Description:
 */

public class RecycleActivity extends BaseAppCompatActivity {

    private ActivityRecycleLayoutBinding mBinding;
    private DBManager mDBManager;
    private DaoSession mDaoSession;
    private RecyclerViewCommonAdapter<Note> mAdapter;
    private String[] mDialogItems = {"还原"};
    private List<Note> mDatas;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recycle_layout;
    }

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        getToolbar().setTitle(R.string.recycle_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        mDBManager = DBManager.getInstance(this);
        setLinearLayoutRecyclerView();
    }

    /**
     * 设置线性布局RecyclerView
     */
    private void setLinearLayoutRecyclerView() {
        mDatas = getDBDatas();
        mAdapter = new RecyclerViewCommonAdapter<>(this, mDatas, R.layout.item_recycle_layout, BR.note);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.recycleRecyclerview.setLayoutManager(linearLayoutManager);
        mBinding.recycleRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mBinding.setAdapter(mAdapter);
        initItemEvent();
    }

    private void initItemEvent() {
        mAdapter.setmOnItemClickListener(new RecyclerViewCommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showRevertDialog(position);
            }
        });
    }

    /**
     * 显示还原操作对话框
     *
     * @param pos
     */
    private void showRevertDialog(final int pos) {
        DialogUtils dialogUtils = new DialogUtils(this);
        dialogUtils.showItemDialog(mDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    revertData(pos);
                }
            }
        });
    }

    /**
     * 对数据库还原操作
     *
     * @param position
     */
    private void revertData(int position) {
        mDaoSession = mDBManager.getWriteDaoSession();
        NoteDao dao = mDaoSession.getNoteDao();
        Note note = getDBDatas().get(position);
        note.setIsDelete(0);
        note.setStatus(Constant.STATUS_UPDATE);
        dao.update(note);
        mDaoSession.clear();
        mDatas.remove(position);
        mAdapter.updateData(mDatas);
        mAdapter.notifyDataSetChanged();
        showSnackBar(mBinding.getRoot(), getString(R.string.recycle_revert_success));
        EventBus.getDefault().post(new SlideMenuEvent.RefreshEvent());
        EventBus.getDefault().post(new NoteBookFragmentEvent.RefreshNoteEvent());
    }

    /**
     * 从数据库获取数据
     *
     * @return
     */
    private List<Note> getDBDatas() {
        mDaoSession = mDBManager.getWriteDaoSession();
        NoteDao noteDao = mDaoSession.getNoteDao();
        QueryBuilder queryBuilder = noteDao.queryBuilder().where(NoteDao.Properties.IsDelete.eq(1)).orderDesc(NoteDao.Properties.UpdateTime);
        mDaoSession.clear();
        return queryBuilder.list();
    }
}