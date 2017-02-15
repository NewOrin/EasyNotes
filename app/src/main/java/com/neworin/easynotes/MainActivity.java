package com.neworin.easynotes;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends BaseAppCompatActivity {

    private LinearLayout mMainLeftContainerLl;
    private FrameLayout mMainContentContainer;
    private DrawerLayout mMainDrawerlayout;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
    }

    private void initView() {
        getToolbar().setTitle(getString(R.string.main_default_title));
        mMainLeftContainerLl = (LinearLayout) findViewById(R.id.main_left_container_ll);
        mMainContentContainer = (FrameLayout) findViewById(R.id.main_content_container);
        mMainDrawerlayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle
                actionBarDrawerToggle =
                new ActionBarDrawerToggle(this,
                                          mMainDrawerlayout,
                                          getToolbar(),
                                          R.string.app_name,
                                          R.string.close);
        actionBarDrawerToggle.syncState();
        mMainDrawerlayout.setDrawerListener(actionBarDrawerToggle);
        getToolbar().inflateMenu(R.menu.main_menu);
        getToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}