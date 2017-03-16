package com.neworin.easynotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.neworin.easynotes.greendao.gen.DaoMaster;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.utils.Constant;

/**
 * Created by NewOrin Zhang on 2017/2/24.
 * E-Mail : NewOrinZhang@Gmail.com
 */

public class DBManager {

    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper mOpenHelper;
    private Context mContext;

    private DBManager(Context context) {
        this.mContext = context;
        mOpenHelper = new DaoMaster.DevOpenHelper(mContext, Constant.DB_NAME, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     *
     * @return
     */
    private SQLiteDatabase getReadableDatabase() {
        if (null == mOpenHelper) {
            mOpenHelper = new DaoMaster.DevOpenHelper(mContext, Constant.DB_NAME, null);
        }
        return mOpenHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (mOpenHelper == null) {
            mOpenHelper = new DaoMaster.DevOpenHelper(mContext, Constant.DB_NAME, null);
        }
        return mOpenHelper.getWritableDatabase();
    }

    /**
     * 获取写数据库会话
     *
     * @return
     */
    public DaoSession getWriteDaoSession() {
        return new DaoMaster(getWritableDatabase()).newSession();
    }

    /**
     * 获取读数据库对话
     *
     * @return
     */
    public DaoSession getReadDaoSession() {
        return new DaoMaster(getReadableDatabase()).newSession();
    }
}
