package com.neworin.easynotes.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.event.SlideMenuEvent;
import com.neworin.easynotes.greendao.gen.SlideMenuNoteBookModelDao;
import com.neworin.easynotes.model.SlideMenuNoteBookModel;
import com.neworin.easynotes.ui.fragment.NoteBookFragment;
import com.neworin.easynotes.R;
import com.neworin.easynotes.ui.fragment.SlideMenuFragment;
import com.neworin.easynotes.databinding.ActivityMainBinding;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class MainActivity extends BaseAppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initView();
    }

    private void initView() {
        getToolbar().setTitle(getString(R.string.main_default_title));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mBinding.mainDrawerlayout, getToolbar(), R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        mBinding.mainDrawerlayout.setDrawerListener(actionBarDrawerToggle);
        getToolbar().inflateMenu(R.menu.main_menu);
        getToolbar().setOnMenuItemClickListener(this);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.main_left_container, new SlideMenuFragment()).commit();
        mFragmentManager.beginTransaction().add(R.id.main_content_container, new NoteBookFragment()).commit();
        SlideMenuNoteBookModel model = new SlideMenuNoteBookModel("笔记本", "1", false);
        DBManager dbManager = new DBManager(this);
        SlideMenuNoteBookModelDao dao = dbManager.getWriteDaoSession().getSlideMenuNoteBookModelDao();
        dao.insert(model);
        L.d(TAG, "写入数据库成功");
        QueryBuilder<SlideMenuNoteBookModel> qb = dbManager.getReadDaoSession().getSlideMenuNoteBookModelDao().queryBuilder();
        List<SlideMenuNoteBookModel> list = qb.list();
        for (SlideMenuNoteBookModel m : list) {
            L.d(TAG, "查询:" + m.getId() + "," + m.getName() + "," + m.getCount());
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Subscribe
    public void onMessageEvent(SlideMenuEvent.HeaderLayoutEvent event) {
        showSnackBar(mBinding.mainDrawerlayout, "点击了");
        mBinding.mainDrawerlayout.closeDrawers();
    }

    @Subscribe
    public void onMessageEvent(SlideMenuEvent.ListItemEvent event) {
        mBinding.mainDrawerlayout.closeDrawers();
        showSnackBar(mBinding.mainDrawerlayout, String.valueOf(event.getPosition()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}