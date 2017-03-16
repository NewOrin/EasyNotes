package com.neworin.easynotes.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.neworin.easynotes.R;
import com.neworin.easynotes.event.NoteBookFragmentEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by NewOrin Zhang
 * 02/15/2017
 * Project: EasyNotes
 * Email: neworin@163.com
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    private static final String TAG = BaseAppCompatActivity.class.getSimpleName();
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }
        getToolbar().setNavigationIcon(R.drawable.ic_arrow_back);
    }

    /**
     * this Activity of tool bar.
     * 获取头部.
     *
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 设置Toolbar标题
     *
     * @param title
     */
    public void setToolbarTitle(String title) {
        getToolbar().setTitle(title);
    }

    public void setNavigationIcon() {
        getToolbar().setNavigationIcon(R.drawable.ic_arrow_back);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Toolbar返回键事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     *
     * @return res layout xml id
     */
    protected abstract int getLayoutId();

    /**
     * 显示SnackBar
     *
     * @param view
     * @param msg
     */
    public void showSnackBar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 显示SnackBar带Action
     *
     * @param view
     * @param msg
     * @param actionMsg
     * @param listener
     */
    public void showSnackBarWithAction(View view,
                                       String msg,
                                       String actionMsg,
                                       View.OnClickListener listener) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).setAction(actionMsg, listener).show();
    }
}
