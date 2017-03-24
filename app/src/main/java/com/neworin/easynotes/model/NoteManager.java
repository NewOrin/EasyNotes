package com.neworin.easynotes.model;

import android.content.Context;

import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.greendao.gen.DaoSession;

/**
 * Created by NewOrin Zhang on 2017/3/24.
 * Project : com.neworin.easynotes.model
 * Description:
 */

public class NoteManager {

    private DBManager mDBManager;
    private DaoSession mDaoSession;
    private Context mContext;

    public NoteManager(Context context) {
        this.mContext = context;
        mDBManager = DBManager.getInstance(context);
    }

    /**
     * 更新
     *
     * @param note
     */
    public void update(Note note) {
        mDaoSession = getDaoSession();
        mDaoSession.getNoteDao().update(note);
        mDaoSession.clear();
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteByKey(long id) {
        mDaoSession = getDaoSession();
        mDaoSession.getNoteDao().deleteByKey(id);
        mDaoSession.clear();
    }

    /**
     * 插入NoteBook
     *
     * @param note
     */
    public void insert(Note note) {
        mDaoSession = getDaoSession();
        mDaoSession.getNoteDao().insert(note);
        mDaoSession.clear();
    }
    private DaoSession getDaoSession() {
        return mDBManager.getWriteDaoSession();
    }
}
