package com.neworin.easynotes.ui.activity;

import android.databinding.DataBindingUtil;

import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.ActivityPersonalLayoutBinding;
import com.neworin.easynotes.ui.BaseAppCompatActivity;

/**
 * Created by NewOrin Zhang on 2017/4/22.
 * Project : com.neworin.easynotes.ui.activity
 * Description:
 */

public class PersonalActivity extends BaseAppCompatActivity {

    private ActivityPersonalLayoutBinding mBinding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_layout;
    }

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        getToolbar().setTitle(R.string.personal_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        initEvent();
    }

    private void initEvent() {

    }
}
