package com.neworin.easynotes.ui.activity;

import com.neworin.easynotes.R;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.ui.fragment.SettingFragment;

/**
 * Created by NewOrin Zhang on 2017/3/22.
 * E-mail : NewOrinZhang@Gmail.com
 */

public class SettingActivity extends BaseAppCompatActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_layout;
    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new SettingFragment()).commit();
        getSupportActionBar().setTitle(getString(R.string.settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
    }
}
