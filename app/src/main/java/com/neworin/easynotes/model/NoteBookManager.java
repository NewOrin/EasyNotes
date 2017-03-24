package com.neworin.easynotes.model;

import android.content.Context;

import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.greendao.gen.DaoSession;

/**
 * Created by NewOrin Zhang on 2017/3/24.
 * Project : com.neworin.easynotes.model
 * Description:
 */

public class NoteBookManager {

    private DBManager mDBManager;
    private DaoSession mDaoSession;
    private Context mContext;

    public NoteBookManager(Context context) {
        this.mContext = context;
        mDBManager = DBManager.getInstance(context);
    }

    /**
     * 更新
     *
     * @param noteBook
     */
    public void update(NoteBook noteBook) {
        mDaoSession = getDaoSession();
        mDaoSession.getNoteBookDao().update(noteBook);
        mDaoSession.clear();
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteByKey(long id) {
        mDaoSession = getDaoSession();
        mDaoSession.getNoteBookDao().deleteByKey(id);
        mDaoSession.clear();
    }

    private DaoSession getDaoSession() {
        return mDBManager.getWriteDaoSession();
    }
}
