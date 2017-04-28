package com.neworin.easynotes.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.adapter.ListViewCommonAdapter;
import com.neworin.easynotes.databinding.FragmentSlideMenuBinding;
import com.neworin.easynotes.event.SlideMenuEvent;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteBookDao;
import com.neworin.easynotes.greendao.gen.NoteDao;
import com.neworin.easynotes.handlers.SlideMenuEventHandler;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.model.User;
import com.neworin.easynotes.ui.BaseFragment;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.GlideUtils;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by NewOrin Zhang
 * 02/16/2017
 * Project: EasyNotes
 * Email: neworin@163.com
 */

public class SlideMenuFragment extends BaseFragment {

    private FragmentSlideMenuBinding mBinding;
    private DBManager mDBManager;
    private DaoSession mDaoSession;
    private NoteBookDao mNoteBookDao;
    private List<NoteBook> mNoteBookList;
    private ListViewCommonAdapter<NoteBook> mAdapter;
    private int mCheckPosition;
    private User mUser;
    private String mEmail;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_slide_menu;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mBinding = DataBindingUtil.bind(getRootView());
        mDBManager = DBManager.getInstance(getActivity());
        initView();
        initEvent();
    }

    private void initView() {
        mDaoSession = mDBManager.getWriteDaoSession();
        mNoteBookDao = mDaoSession.getNoteBookDao();
        mNoteBookList = mNoteBookDao.queryBuilder().list();
        mNoteBookList = setNoteBookCount(mNoteBookList);
        mDaoSession.clear();
        mCheckPosition = 0;
        mNoteBookList.get(0).setChecked(true);//The first one checked default
        mBinding.setHandler(new SlideMenuEventHandler(getActivity()));
        mAdapter = new ListViewCommonAdapter<>(getActivity(), mNoteBookList, R.layout.item_slide_menu_layout, com.neworin.easynotes.BR.notebookBean);
        mBinding.setAdapter(mAdapter);
        View footViewSettings = LayoutInflater.from(getActivity()).inflate(R.layout.slide_menu_footer_settings_layout, null);
        View footViewEdit = LayoutInflater.from(getActivity()).inflate(R.layout.slide_menu_footer_edit_layout, null);
        View footViewRecycle = LayoutInflater.from(getActivity()).inflate(R.layout.slide_menu_footer_recycle_layout,null);
        mBinding.slideMenuListview.addFooterView(footViewEdit);
        mBinding.slideMenuListview.addFooterView(footViewRecycle);
        mBinding.slideMenuListview.addFooterView(footViewSettings);
        footViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SlideMenuEvent.SettingItemEvent(Constant.SLIDE_ITEM_EDIT, getActivity()));
            }
        });
        footViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SlideMenuEvent.SettingItemEvent(Constant.SLIDE_ITEM_SETTINGS, getActivity()));
            }
        });
        footViewRecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SlideMenuEvent.SettingItemEvent(Constant.SLIDE_ITEM_RECYCLE, getActivity()));
            }
        });
        mEmail = SharedPreferenceUtil.getString(getActivity(), Constant.USER_EMAIL);
        if (mEmail != null && !mEmail.equals("")) {
            mBinding.slideNameText.setText(mEmail);
            setAvatar();
        } else {
            Glide.with(getActivity()).load(R.drawable.ic_default_avatar).placeholder(R.drawable.ic_default_avatar).into(mBinding.slideAvatarImage);
        }
    }

    private void setHeadInfo() {
        if (null == mUser) {
            mBinding.slideNameText.setText(getString(R.string.login_or_register));
        } else {
            mBinding.slideNameText.setText(mUser.getEmail());
        }
        setAvatar();
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

    private void initEvent() {
        mBinding.slideMenuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new SlideMenuEvent.ListItemEvent(mNoteBookList.get(position)));
                mBinding.slideMenuListview.setItemChecked(position, true);
            }
        });
    }

    private void refreshData() {
        if (null != mNoteBookList && null != mAdapter) {
            mNoteBookList = mDBManager.getWriteDaoSession().getNoteBookDao().queryBuilder().list();
            mNoteBookList = setNoteBookCount(mNoteBookList);
            mNoteBookList.get(0).setChecked(true);
            mAdapter = new ListViewCommonAdapter<>(getActivity(), mNoteBookList, R.layout.item_slide_menu_layout, com.neworin.easynotes.BR.notebookBean);
            mBinding.setAdapter(mAdapter);
        }
    }

    private int getCheckPos() {
        return mCheckPosition;
    }

    private void setCheckPos(int checkPosition) {
        this.mCheckPosition = checkPosition;
    }

    @Subscribe
    public void onMessageEvent(SlideMenuEvent.RefreshEvent event) {
        refreshData();
    }

    @Subscribe
    public void onMessageEvent(SlideMenuEvent.RefreshUserEvent event) {
        if (null == event.mUser) {
            mUser = null;
        } else {
            mUser = event.mUser;
        }
        setHeadInfo();
    }

    @Subscribe
    public void onMessageEvent(SlideMenuEvent.RefreshAvatarEvent event) {
        setAvatar();
    }

    /**
     * 设置头像
     */
    private void setAvatar() {
        String user_id = SharedPreferenceUtil.getString(getActivity(), Constant.USER_ID);
        if (user_id != null && !user_id.equals("")) {
            GlideUtils.loadLogo(getActivity(), mBinding.slideAvatarImage, Constant.GET_AVATAR_URL + user_id);
        } else {
            Glide.with(getActivity()).load(R.drawable.ic_default_avatar).error(R.drawable.ic_default_avatar).into(mBinding.slideAvatarImage);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}