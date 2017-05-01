package com.neworin.easynotes.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteBookDao;
import com.neworin.easynotes.greendao.gen.NoteDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;

/**
 * Created by NewOrin Zhang on 2017/2/26.
 * E-Mail : NewOrinZhang@Gmail.com
 */

@Entity
public class Note extends BaseObservable implements Parcelable {

    @Id
    private long id;
    private long notebookId;
    private long userId;
    private String title;
    private String content;
    private Date createTime;
    private Date updateTime;
    private int isDelete;//0为未删除，1为已删除
    private int status;
    @ToOne(joinProperty = "notebookId")
    private NoteBook mNoteBook;

    @Generated(hash = 1272611929)
    public Note() {
    }

    @Bindable
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Bindable
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Bindable
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public NoteBook getNoteBook() {
        return mNoteBook;
    }

    public void setNoteBook(NoteBook noteBook) {
        mNoteBook = noteBook;
    }

    public long getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(long notebookId) {
        this.notebookId = notebookId;
    }

    @Bindable
    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Keep
    public Note(long id, String title, long userId, String content, Date createTime, Date updateTime, NoteBook noteBook, int isDelete, int status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.mNoteBook = noteBook;
        this.isDelete = isDelete;
        this.userId = userId;
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.notebookId);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeLong(this.createTime != null ? this.createTime.getTime() : -1);
        dest.writeLong(this.updateTime != null ? this.updateTime.getTime() : -1);
        dest.writeParcelable(this.mNoteBook, flags);
        dest.writeLong(this.userId);
        dest.writeInt(this.status);
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 599181167)
    public NoteBook getMNoteBook() {
        long __key = this.notebookId;
        if (mNoteBook__resolvedKey == null || !mNoteBook__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NoteBookDao targetDao = daoSession.getNoteBookDao();
            NoteBook mNoteBookNew = targetDao.load(__key);
            synchronized (this) {
                mNoteBook = mNoteBookNew;
                mNoteBook__resolvedKey = __key;
            }
        }
        return mNoteBook;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1124274750)
    public void setMNoteBook(@NotNull NoteBook mNoteBook) {
        if (mNoteBook == null) {
            throw new DaoException(
                    "To-one property 'notebookId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.mNoteBook = mNoteBook;
            notebookId = mNoteBook.getId();
            mNoteBook__resolvedKey = notebookId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 799086675)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNoteDao() : null;
    }

    protected Note(Parcel in) {
        this.id = in.readLong();
        this.notebookId = in.readLong();
        this.title = in.readString();
        this.content = in.readString();
        long tmpCreateTime = in.readLong();
        this.createTime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
        long tmpUpdateTime = in.readLong();
        this.updateTime = tmpUpdateTime == -1 ? null : new Date(tmpUpdateTime);
        this.mNoteBook = in.readParcelable(NoteBook.class.getClassLoader());
        this.userId = in.readLong();
        this.status = in.readInt();
    }

    @Generated(hash = 338718600)
    public Note(long id, long notebookId, long userId, String title, String content, Date createTime, Date updateTime, int isDelete) {
        this.id = id;
        this.notebookId = notebookId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isDelete = isDelete;
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 363862535)
    private transient NoteDao myDao;
    @Generated(hash = 1268916075)
    private transient Long mNoteBook__resolvedKey;
}
