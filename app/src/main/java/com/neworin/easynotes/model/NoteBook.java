package com.neworin.easynotes.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteBookDao;
import com.neworin.easynotes.greendao.gen.UserDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;

/**
 * Created by NewOrin Zhang
 * 02/17/2017
 * Project: EasyNotes
 * Email: neworin@163.com
 */

@Entity
public class NoteBook extends BaseObservable implements Parcelable {

    @Id
    private long id;
    private long userId;
    private String name;
    private int count;
    private Boolean isChecked;
    private int isDelete;//0为未删除，1为已删除
    private Date createTime;
    private Date updateTime;
    private Date syncTime;
    @ToOne(joinProperty = "userId")
    private User mUser;
    private int status;

    @Keep
    public NoteBook() {
    }

    @Keep
    public NoteBook(long id, String name, int count, Boolean isChecked) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.isChecked = isChecked;
    }

    public NoteBook(String name, int count, Boolean isChecked) {
        this.name = name;
        this.count = count;
        this.isChecked = isChecked;
    }

    @Keep
    public NoteBook(long id, String name, int count,int userId, Boolean isChecked, Date createTime, Date updateTime, Date syncTime, User user, Integer status, Integer isDelete) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.isChecked = isChecked;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.syncTime = syncTime;
        this.mUser = user;
        this.status = status;
        this.isDelete = isDelete;
        this.userId = userId;

    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable
    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    @Bindable
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public Boolean getIsChecked() {
        return this.isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Bindable
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Bindable
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Bindable
    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NoteBook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.userId);
        dest.writeString(this.name);
        dest.writeInt(this.count);
        dest.writeValue(this.isChecked);
        dest.writeInt(this.isDelete);
        dest.writeLong(this.createTime != null ? this.createTime.getTime() : -1);
        dest.writeLong(this.updateTime != null ? this.updateTime.getTime() : -1);
        dest.writeLong(this.syncTime != null ? this.syncTime.getTime() : -1);
        dest.writeParcelable(this.mUser, flags);
        dest.writeInt(this.status);
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 395820747)
    public User getMUser() {
        long __key = this.userId;
        if (mUser__resolvedKey == null || !mUser__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User mUserNew = targetDao.load(__key);
            synchronized (this) {
                mUser = mUserNew;
                mUser__resolvedKey = __key;
            }
        }
        return mUser;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1128592984)
    public void setMUser(@NotNull User mUser) {
        if (mUser == null) {
            throw new DaoException("To-one property 'userId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.mUser = mUser;
            userId = mUser.getId();
            mUser__resolvedKey = userId;
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
    @Generated(hash = 1888691330)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNoteBookDao() : null;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    protected NoteBook(Parcel in) {
        this.id = in.readLong();
        this.userId = in.readLong();
        this.name = in.readString();
        this.count = in.readInt();
        this.isChecked = (Boolean) in.readValue(Boolean.class.getClassLoader());
        long tmpCreateTime = in.readLong();
        this.createTime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
        long tmpUpdateTime = in.readLong();
        this.updateTime = tmpUpdateTime == -1 ? null : new Date(tmpUpdateTime);
        long tmpSyncTime = in.readLong();
        this.syncTime = tmpSyncTime == -1 ? null : new Date(tmpSyncTime);
        this.mUser = in.readParcelable(User.class.getClassLoader());
        this.status = in.readInt();
        this.isDelete = in.readInt();
    }

    @Keep
    public NoteBook(long id, long userId, String name, int count, Boolean isChecked, Date createTime, Date updateTime, Date syncTime) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.count = count;
        this.isChecked = isChecked;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.syncTime = syncTime;
    }

    @Generated(hash = 623910123)
    public NoteBook(long id, long userId, String name, int count, Boolean isChecked, int isDelete, Date createTime, Date updateTime, Date syncTime, int status) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.count = count;
        this.isChecked = isChecked;
        this.isDelete = isDelete;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.syncTime = syncTime;
        this.status = status;
    }

    public static final Parcelable.Creator<NoteBook> CREATOR = new Parcelable.Creator<NoteBook>() {
        @Override
        public NoteBook createFromParcel(Parcel source) {
            return new NoteBook(source);
        }

        @Override
        public NoteBook[] newArray(int size) {
            return new NoteBook[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1692630944)
    private transient NoteBookDao myDao;
    @Generated(hash = 1377221062)
    private transient Long mUser__resolvedKey;
}
