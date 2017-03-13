package com.neworin.easynotes.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by NewOrin Zhang on 2017/2/26.
 * E-Mail : NewOrinZhang@Gmail.com
 */

@Entity
public class Note extends BaseObservable {

    @Id
    private long id;
    private String content;
    private Date time;

    @Generated(hash = 1272611929)
    public Note() {
    }

    @Generated(hash = 1346765385)
    public Note(long id, String content, Date time) {
        this.id = id;
        this.content = content;
        this.time = time;
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
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
