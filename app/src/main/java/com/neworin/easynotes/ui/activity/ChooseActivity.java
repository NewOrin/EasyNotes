package com.neworin.easynotes.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.ActivityChooseLayoutBinding;
import com.neworin.easynotes.event.SlideMenuEvent;
import com.neworin.easynotes.model.User;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.ui.fragment.ChooseDialogFragment;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by NewOrin Zhang on 2017/4/21.
 * Project : com.neworin.easynotes.ui.activity
 * Description:
 */

public class ChooseActivity extends BaseAppCompatActivity implements View.OnClickListener, ChooseDialogFragment.IUserResultListener {

    private ActivityChooseLayoutBinding mBinding;
    private String TAG = ChooseActivity.class.getSimpleName();
    private ChooseDialogFragment dialogFragment;
    private DBManager mDBManager;

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        getToolbar().setTitle(R.string.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        initEvent();
        mDBManager = DBManager.getInstance(this);
    }

    private void initEvent() {
        getLoginBtn().setOnClickListener(this);
        getRegisterBtn().setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_layout;
    }

    private Button getLoginBtn() {
        return mBinding.chooseLoginBtn;
    }

    private Button getRegisterBtn() {
        return mBinding.chooseRegisterBtn;
    }

    @Override
    public void onClick(View v) {
        if (v == getLoginBtn()) {
            showDialog(true);
        } else if (v == getRegisterBtn()) {
            showDialog(false);
        }
    }

    private void showDialog(Boolean mIsLogin) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.ARG0, mIsLogin);
        dialogFragment = ChooseDialogFragment.newInstance(bundle);
        dialogFragment.show(getSupportFragmentManager(), TAG);
        dialogFragment.setIUserResultListener(this);
    }

    @Override
    public void resultSuccess(User user) {
        if (null != dialogFragment) {
            dialogFragment.dismiss();
//            DaoSession session = mDBManager.getWriteDaoSession();
//            session.getUserDao().insert(user);
            SharedPreferenceUtil.insertUserInfo(this, user);
            EventBus.getDefault().post(new SlideMenuEvent.RefreshUserEvent(user));
            finish();
        }
    }
}
