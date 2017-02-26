package com.neworin.easynotes.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.neworin.easynotes.model.SlideMenuNoteBookModel;

import com.neworin.easynotes.greendao.gen.SlideMenuNoteBookModelDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig slideMenuNoteBookModelDaoConfig;

    private final SlideMenuNoteBookModelDao slideMenuNoteBookModelDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        slideMenuNoteBookModelDaoConfig = daoConfigMap.get(SlideMenuNoteBookModelDao.class).clone();
        slideMenuNoteBookModelDaoConfig.initIdentityScope(type);

        slideMenuNoteBookModelDao = new SlideMenuNoteBookModelDao(slideMenuNoteBookModelDaoConfig, this);

        registerDao(SlideMenuNoteBookModel.class, slideMenuNoteBookModelDao);
    }
    
    public void clear() {
        slideMenuNoteBookModelDaoConfig.clearIdentityScope();
    }

    public SlideMenuNoteBookModelDao getSlideMenuNoteBookModelDao() {
        return slideMenuNoteBookModelDao;
    }

}
