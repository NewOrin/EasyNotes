package com.neworin.easynotes.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.ActivityMainBinding;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.event.SlideMenuEvent;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteBookDao;
import com.neworin.easynotes.http.Response;
import com.neworin.easynotes.http.UserBizImpl;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.model.User;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.ui.fragment.NoteBookFragment;
import com.neworin.easynotes.ui.fragment.SlideMenuFragment;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DateUtil;
import com.neworin.easynotes.utils.GsonUtil;
import com.neworin.easynotes.utils.HttpUtil;
import com.neworin.easynotes.utils.L;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends BaseAppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
    private FragmentManager mFragmentManager;
    private DBManager mDBManager;
    private DaoSession mDaoSession;
    private NoteBookDao mNoteBookDao;
    private List<NoteBook> mNoteBookList;
    private NoteBook mNoteBook;
    private boolean mIsThumb = false;

    @Override
    protected void initView() {
        autoLogin();
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        EventBus.getDefault().register(this);
        mDBManager = DBManager.getInstance(this);
        initNoteDao();
        setToolbarTitle(mNoteBook.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mBinding.mainDrawerlayout, getToolbar(), R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        mBinding.mainDrawerlayout.setDrawerListener(actionBarDrawerToggle);
        getToolbar().inflateMenu(R.menu.main_menu);
        getToolbar().setOnMenuItemClickListener(this);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.main_left_container, new SlideMenuFragment()).commit();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.ARG0, mNoteBook);
        mFragmentManager.beginTransaction().add(R.id.main_content_container, NoteBookFragment.newsInstance(bundle)).commit();
    }

    /**
     * 初始化笔记本
     */
    private void initNoteDao() {
        mDaoSession = mDBManager.getWriteDaoSession();
        mNoteBookDao = mDaoSession.getNoteBookDao();
        if (mNoteBookDao.queryBuilder().list().size() == 0) {
            insertFirstNoteBook(mNoteBookDao);
        }
        mNoteBook = mNoteBookDao.load((long) 1);
        mDaoSession.clear();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.main_menu_refresh) {
            EventBus.getDefault().post(new NoteBookFragmentEvent.RefreshNoteEvent());
        }
        if (item.getItemId() == R.id.main_menu_thumb) {
            EventBus.getDefault().post(new NoteBookFragmentEvent.ShowThumbEvent());
            if (mIsThumb) {
                item.setTitle(R.string.main_thumb);
                mIsThumb = false;
            } else {
                item.setTitle(R.string.main_item);
                mIsThumb = true;
            }
        }
        if (item.getItemId() == R.id.main_menu_show_title_only) {
            EventBus.getDefault().post(new NoteBookFragmentEvent.ShowTitleEvent());
        }
        return false;
    }

    @Subscribe
    public void onMessageEvent(SlideMenuEvent.HeaderLayoutEvent event) {
        mBinding.mainDrawerlayout.closeDrawers();
    }

    @Subscribe
    public void onMessageEvent(SlideMenuEvent.ListItemEvent event) {
        mBinding.mainDrawerlayout.closeDrawers();
        this.mNoteBook = event.getNoteBook();
        setToolbarTitle(mNoteBook.getName());
    }

    @Subscribe
    public void onMessageEvent(SlideMenuEvent.SettingItemEvent event) {
        mBinding.mainDrawerlayout.closeDrawers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化第一条笔记本记录
     *
     * @param noteBookDao
     */
    private void insertFirstNoteBook(NoteBookDao noteBookDao) {
        NoteBook noteBook = new NoteBook();
        noteBook.setId(1);
        noteBook.setName(getString(R.string.note_my_notebook));
        noteBook.setCreateTime(DateUtil.getNowTime());
        noteBookDao.insert(noteBook);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.EDIT_BOOK_RESULT_CODE) {
            EventBus.getDefault().post(new SlideMenuEvent.RefreshEvent());
        }
    }

    @Override
    public void onBackPressed() {
        if (mBinding.mainDrawerlayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.mainDrawerlayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 用户自动登录
     */
    private void autoLogin() {
        if (HttpUtil.isNetworkConnected(this)) {
            final String email = SharedPreferenceUtil.getString(this, Constant.USER_EMAIL);
            String password = SharedPreferenceUtil.getString(this, Constant.USER_PASSWORD);
            if (null != email && null != password && !email.equals("") && !password.equals("")) {
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                UserBizImpl userBiz = new UserBizImpl();
                userBiz.login(user, new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        L.d(TAG, "user email = " + email + "auto login success");
                        User user = GsonUtil.getDateFormatGson().fromJson(response.body().getData().toString(), User.class);
                        SharedPreferenceUtil.putString(MainActivity.this, Constant.USER_AVATAR_URL, user.getAvatarurl());
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        L.d(TAG, "user email = " + email + " auto login failed " + t.getMessage());
                    }
                });
            }
        } else {
            L.d(TAG, "no network");
        }
    }
}