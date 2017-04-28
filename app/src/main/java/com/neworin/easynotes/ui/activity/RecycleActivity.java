package com.neworin.easynotes.ui.activity;

import android.databinding.DataBindingUtil;

import com.neworin.easynotes.R;
import com.neworin.easynotes.databinding.ActivityRecycleLayoutBinding;
import com.neworin.easynotes.ui.BaseAppCompatActivity;

/**
 * Created by NewOrin Zhang on 2017/4/28.
 * Project : com.neworin.easynotes.ui.activity
 * Description:
 */

public class RecycleActivity extends BaseAppCompatActivity {

    private ActivityRecycleLayoutBinding mBinding;

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
    }
}
