package com.neworin.easynotes.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
import com.neworin.easynotes.http.NoteBizImpl;
import com.neworin.easynotes.http.Response;
import com.neworin.easynotes.http.UserBizImpl;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.model.User;
import com.neworin.easynotes.service.NoteImageUploadService;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.ui.fragment.NoteBookFragment;
import com.neworin.easynotes.ui.fragment.SlideMenuFragment;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DateUtil;
import com.neworin.easynotes.utils.DialogUtils;
import com.neworin.easynotes.utils.GsonUtil;
import com.neworin.easynotes.utils.HttpUtil;
import com.neworin.easynotes.utils.L;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
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
    private String[] mPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private final int REQUEST_PERMISSION_CODE = 9999;

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
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.ARG0, mNoteBook);
        mFragmentManager.beginTransaction().add(R.id.main_content_container, NoteBookFragment.newsInstance(bundle)).commitAllowingStateLoss();
        showGrantPermissionDialog();
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
            String userId = SharedPreferenceUtil.getString(this, Constant.USER_ID);
            if (null != userId && !userId.equals("")) {
                NoteBizImpl noteBiz = new NoteBizImpl();
                noteBiz.syncData(this);
                EventBus.getDefault().post(new NoteBookFragmentEvent.RefreshNoteEvent());
            } else {
                showSnackBar(mBinding.getRoot(), getString(R.string.main_no_login_hint));
            }
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
        if (item.getItemId() == R.id.main_menu_search) {
            startActivity(new Intent(this, SearchActivity.class));
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
                        L.d(TAG, "user email = " + email + " auto login success");
                        User user = GsonUtil.getDateFormatGson().fromJson(response.body().getData().toString(), User.class);
                        SharedPreferenceUtil.putString(MainActivity.this, Constant.USER_AVATAR_URL, user.getAvatarurl());
                        NoteBizImpl noteBiz = new NoteBizImpl();
                        noteBiz.postAllDatas(MainActivity.this);
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        L.d(TAG, "user email = " + email + " auto login failed " + t.getMessage());
                    }
                });
            }
        } else {
            L.d(TAG, "auto login no network");
        }
    }

    /**
     * 显示授予权限提示对话框
     */
    private void showGrantPermissionDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final List<String> needsToGrantPermissions = new ArrayList<>();
            for (int i = 0; i < mPermissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, mPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    needsToGrantPermissions.add(mPermissions[i]);
                }
            }
            if (needsToGrantPermissions.size() > 0) {
                DialogUtils dialogUtils = new DialogUtils(this);
                dialogUtils.showHintDialog(getString(R.string.grant_permission_hint), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, needsToGrantPermissions.toArray(new String[needsToGrantPermissions.size()]), REQUEST_PERMISSION_CODE);
                    }
                });
            }else{
                mFragmentManager.beginTransaction().add(R.id.main_left_container, new SlideMenuFragment()).commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    if (ContextCompat.checkSelfPermission(this, mPermissions[0]) != PackageManager.PERMISSION_GRANTED) {
                        showGrantPermissionDialog();
                        return;
                    }
                }
                mFragmentManager.beginTransaction().add(R.id.main_left_container, new SlideMenuFragment()).commitAllowingStateLoss();
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 启动上传图片服务
     */
    public void startUploadImageService() {
        startService(new Intent(MainActivity.this, NoteImageUploadService.class));
    }
}