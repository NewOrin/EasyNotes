package com.neworin.easynotes.ui.activity;

import android.content.DialogInterface;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.R;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.ui.BaseAppCompatActivity;
import com.neworin.easynotes.ui.fragment.EditNoteBookFragment;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DateUtil;
import com.neworin.easynotes.utils.DialogUtils;
import com.neworin.easynotes.utils.GenerateSequenceUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by NewOrin Zhang on 2017/3/22.
 * E-mail : NewOrinZhang@Gmail.com
 */

public class EditNoteBookActivity extends BaseAppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private DialogUtils mDialogUtils;
    private DBManager mDBManager;
    private DaoSession mDaoSession;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_layout;
    }

    @Override
    protected void initView() {
        mDBManager = DBManager.getInstance(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new EditNoteBookFragment()).commit();
        getSupportActionBar().setTitle((getString(R.string.edit_notebook)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationIcon();
        getToolbar().setOnMenuItemClickListener(this);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Constant.EDIT_BOOK_RESULT_CODE);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_notebook_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.editnote_menu_create) {
            showCreateBookDailog();
        }
        return false;
    }

    /**
     * 显示创建对话框
     */
    private void showCreateBookDailog() {
        mDialogUtils = new DialogUtils(this);
        mDialogUtils.showEditTextDialog(R.string.edit_notebook_create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertNoteBook(mDialogUtils.getEditText());
            }
        });
    }

    /**
     * 添加笔记本
     *
     * @param name
     */
    private void insertNoteBook(String name) {
        mDaoSession = mDBManager.getWriteDaoSession();
        NoteBook noteBook = new NoteBook();
        noteBook.setId(GenerateSequenceUtil.generateSequenceNo());
        noteBook.setName(name);
        noteBook.setCreateTime(DateUtil.getNowTime());
        mDaoSession.getNoteBookDao().insert(noteBook);
        mDaoSession.clear();
        EventBus.getDefault().post(new NoteBookFragmentEvent.RefreshNoteEvent());
    }

    @Override
    public void onBackPressed() {
        setResult(Constant.EDIT_BOOK_RESULT_CODE);
        finish();
        super.onBackPressed();
    }
}
