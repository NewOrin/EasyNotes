package com.neworin.easynotes.ui.activity;

import android.os.Bundle;

import com.neworin.easynotes.R;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.ui.fragment.EditNoteBookFragment;

/**
 * Created by NewOrin Zhang on 2017/3/22.
 * E-mail : NewOrinZhang@Gmail.com
 */

public class EditNoteBookActivity extends BaseAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new EditNoteBookFragment()).commit();
        getSupportActionBar().setTitle(getString(R.string.edit_notebook));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_layout;
    }
}
