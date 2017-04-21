package com.neworin.easynotes.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.ActivityChooseLayoutBinding;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.ui.fragment.ChooseDialogFragment;
import com.neworin.easynotes.utils.Constant;

/**
 * Created by NewOrin Zhang on 2017/4/21.
 * Project : com.neworin.easynotes.ui.activity
 * Description:
 */

public class ChooseActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ActivityChooseLayoutBinding mBinding;
    private String TAG = ChooseActivity.class.getSimpleName();

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        getToolbar().setTitle(R.string.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        initEvent();
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
        ChooseDialogFragment dialogFragment = ChooseDialogFragment.newInstance(bundle);
        dialogFragment.show(getSupportFragmentManager(), TAG);
    }
}
