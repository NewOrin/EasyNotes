package com.neworin.easynotes.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by NewOrin Zhang on 2017/3/21.
 * E-mail : NewOrinZhang@Gmail.com
 */

@Entity
public class User extends BaseObservable {

    @Id
    private long id;
    private String wechatNumber;
    private String nickName;
    private String avatarUrl;
    private Date joinTime;
    private long noteBookId;

    @Generated(hash = 926753409)
    public User(long id, String wechatNumber, String nickName, String avatarUrl,
            Date joinTime, long noteBookId) {
        this.id = id;
        this.wechatNumber = wechatNumber;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.joinTime = joinTime;
        this.noteBookId = noteBookId;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    @Bindable
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable
    public String getWechatNumber() {
        return wechatNumber;
    }

    public void setWechatNumber(String wechatNumber) {
        this.wechatNumber = wechatNumber;
    }

    @Bindable
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Bindable
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Bindable
    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    @Bindable
    public long getNoteBookId() {
        return noteBookId;
    }

    public void setNoteBookId(long noteBookId) {
        this.noteBookId = noteBookId;
    }
}
