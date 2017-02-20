package com.neworin.easynotes.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by NewOrin Zhang
 * 02/17/2017
 * Project: EasyNotes
 * Email: neworin@163.com
 */

public class SlideMenuNoteBookModel extends BaseObservable {
    private String name;
    private String count;
    private Boolean isChecked;

    public SlideMenuNoteBookModel() {
    }

    public SlideMenuNoteBookModel(String name, String count, Boolean isChecked) {
        this.name = name;
        this.count = count;
        this.isChecked = isChecked;
    }

    @Bindable
    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
        notifyChange();
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
}
