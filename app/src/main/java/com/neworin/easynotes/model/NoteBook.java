package com.neworin.easynotes.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

/**
 * Created by NewOrin Zhang
 * 02/17/2017
 * Project: EasyNotes
 * Email: neworin@163.com
 */

@Entity
public class NoteBook extends BaseObservable {

    @Id
    private long id;
    private String name;
    private String count;
    private Boolean isChecked;

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
}
