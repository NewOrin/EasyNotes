package com.neworin.easynotes.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

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
    private String count;
    private Boolean isChecked;
    private Date createTime;
    private Date updateTime;
    private Date syncTime;

    @Keep
    public NoteBook() {
    }

    @Keep
    public NoteBook(long id, String name, String count, Boolean isChecked) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.isChecked = isChecked;
    }

    public NoteBook(String name, String count, Boolean isChecked) {
        this.name = name;
        this.count = count;
        this.isChecked = isChecked;
    }

    @Generated(hash = 1745787609)
    public NoteBook(long id, long userId, String name, String count,
            Boolean isChecked, Date createTime, Date updateTime, Date syncTime) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.count = count;
        this.isChecked = isChecked;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.syncTime = syncTime;
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
    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsChecked() {
        return this.isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Bindable
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.count);
        dest.writeValue(this.isChecked);
        dest.writeLong(this.createTime != null ? this.createTime.getTime() : -1);
        dest.writeLong(this.updateTime != null ? this.updateTime.getTime() : -1);
        dest.writeLong(this.syncTime != null ? this.syncTime.getTime() : -1);
    }

    protected NoteBook(Parcel in) {
        this.id = in.readLong();
        this.userId = in.readLong();
        this.name = in.readString();
        this.count = in.readString();
        this.isChecked = (Boolean) in.readValue(Boolean.class.getClassLoader());
        long tmpCreateTime = in.readLong();
        this.createTime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
        long tmpUpdateTime = in.readLong();
        this.updateTime = tmpUpdateTime == -1 ? null : new Date(tmpUpdateTime);
        long tmpSyncTime = in.readLong();
        this.syncTime = tmpSyncTime == -1 ? null : new Date(tmpSyncTime);
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
}
