package com.neworin.easynotes.ui.activity;

import android.databinding.DataBindingUtil;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.SearchView;

import com.neworin.easynotes.BR;
import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.adapter.RecyclerViewCommonAdapter;
import com.neworin.easynotes.databinding.ActivitySearchLayoutBinding;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteDao;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NewOrin Zhang on 2017/5/4.
 * Project : com.neworin.easynotes.ui.activity
 * Description:
 */

public class SearchActivity extends BaseAppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private String TAG = SearchActivity.class.getSimpleName();
    private DBManager mDBManager;
    private DaoSession mDaoSession;
    private List<Note> mNoteList;
    private Map<Integer, String> mNotesMapStr;
    private ActivitySearchLayoutBinding mBinding;
    private RecyclerViewCommonAdapter<Note> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_layout;
    }

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        getToolbar().inflateMenu(R.menu.search_menu);
        MenuItem menuItem = getToolbar().getMenu().findItem(R.id.search_menu_search);
        menuItem.expandActionView();

        mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.onActionViewExpanded();
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                SearchActivity.this.finish();
                return true;
            }
        });
        initData();
        initEvent();
    }

    private void initEvent() {
        mSearchView.setOnQueryTextListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mNoteList = new ArrayList<>();
        setLinearLayoutRecyclerView(mNoteList);
        mNotesMapStr = getNoteStrs();
    }

    /**
     * 设置线性布局RecyclerView
     */
    private void setLinearLayoutRecyclerView(List<Note> noteList) {
        mAdapter = new RecyclerViewCommonAdapter<>(this, noteList, R.layout.item_recycle_layout, BR.note);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mBinding.searchRecyclerview.setLayoutManager(linearLayoutManager);
        mBinding.searchRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mBinding.setAdapter(mAdapter);
        initItemEvent();
    }

    private void initItemEvent() {
        mAdapter.setmOnItemClickListener(new RecyclerViewCommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        queryNotes(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        queryNotes(newText);
        return false;
    }

    /**
     * 查找符合的笔记
     *
     * @param searchText
     */
    private void queryNotes(String searchText) {
        List<Note> noteList = new ArrayList<>();
        if (!searchText.equals("")) {
            for (Map.Entry<Integer, String> entry : mNotesMapStr.entrySet()) {
                if (entry.getValue().contains(searchText)) {
                    noteList.add(mNoteList.get(entry.getKey()));
                }
            }
        }
        mAdapter.updateData(noteList);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 从数据库取出笔记拼成字符串
     *
     * @return
     */
    private Map<Integer, String> getNoteStrs() {
        long userId = 0;
        if (SharedPreferenceUtil.getString(this, Constant.USER_ID) != null) {
            userId = Long.parseLong(SharedPreferenceUtil.getString(this, Constant.USER_ID));
        }
        mDBManager = DBManager.getInstance(this);
        mDaoSession = mDBManager.getReadDaoSession();
        Map<Integer, String> stringMap = new HashMap<>();
        mNoteList = mDaoSession.getNoteDao().queryBuilder().where(NoteDao.Properties.UserId.eq(userId), NoteDao.Properties.IsDelete.eq(0)).list();
        for (int i = 0; i < mNoteList.size(); i++) {
            stringMap.put(i, mNoteList.get(i).getTitle() + mNoteList.get(i).getContent());
        }
        return stringMap;
    }
}
