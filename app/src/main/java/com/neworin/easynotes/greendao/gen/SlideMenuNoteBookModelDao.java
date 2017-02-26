package com.neworin.easynotes.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.neworin.easynotes.model.SlideMenuNoteBookModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SLIDE_MENU_NOTE_BOOK_MODEL".
*/
public class SlideMenuNoteBookModelDao extends AbstractDao<SlideMenuNoteBookModel, Long> {

    public static final String TABLENAME = "SLIDE_MENU_NOTE_BOOK_MODEL";

    /**
     * Properties of entity SlideMenuNoteBookModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Count = new Property(2, String.class, "count", false, "COUNT");
        public final static Property IsChecked = new Property(3, Boolean.class, "isChecked", false, "IS_CHECKED");
    }


    public SlideMenuNoteBookModelDao(DaoConfig config) {
        super(config);
    }
    
    public SlideMenuNoteBookModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SLIDE_MENU_NOTE_BOOK_MODEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"COUNT\" TEXT," + // 2: count
                "\"IS_CHECKED\" INTEGER);"); // 3: isChecked
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SLIDE_MENU_NOTE_BOOK_MODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SlideMenuNoteBookModel entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String count = entity.getCount();
        if (count != null) {
            stmt.bindString(3, count);
        }
 
        Boolean isChecked = entity.getIsChecked();
        if (isChecked != null) {
            stmt.bindLong(4, isChecked ? 1L: 0L);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SlideMenuNoteBookModel entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String count = entity.getCount();
        if (count != null) {
            stmt.bindString(3, count);
        }
 
        Boolean isChecked = entity.getIsChecked();
        if (isChecked != null) {
            stmt.bindLong(4, isChecked ? 1L: 0L);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public SlideMenuNoteBookModel readEntity(Cursor cursor, int offset) {
        SlideMenuNoteBookModel entity = new SlideMenuNoteBookModel( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // count
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0 // isChecked
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SlideMenuNoteBookModel entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCount(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIsChecked(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SlideMenuNoteBookModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SlideMenuNoteBookModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SlideMenuNoteBookModel entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
