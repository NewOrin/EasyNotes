package com.neworin.easynotes.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by NewOrin Zhang on 2017/2/26.
 * E-Mail : NewOrinZhang@Gmail.com
 */

@Entity
public class Note extends BaseObservable implements Parcelable {

    @Id
    private long id;
    private String title;
    private String content;
    private Date createTime;
    private Date updateTime;
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

    @Generated(hash = 435923545)
    public Note(long id, String title, String content, Date createTime,
            Date updateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeLong(this.createTime != null ? this.createTime.getTime() : -1);
        dest.writeLong(this.updateTime != null ? this.updateTime.getTime() : -1);
        dest.writeParcelable(this.mNoteBook, flags);
    }

    protected Note(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.content = in.readString();
        long tmpCreateTime = in.readLong();
        this.createTime = tmpCreateTime == -1 ? null : new Date(tmpCreateTime);
        long tmpUpdateTime = in.readLong();
        this.updateTime = tmpUpdateTime == -1 ? null : new Date(tmpUpdateTime);
        this.mNoteBook = in.readParcelable(NoteBook.class.getClassLoader());
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
}
