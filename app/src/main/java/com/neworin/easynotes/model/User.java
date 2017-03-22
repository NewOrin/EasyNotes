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
 * Created by NewOrin Zhang on 2017/3/21.
 * E-mail : NewOrinZhang@Gmail.com
 */

@Entity
public class User extends BaseObservable implements Parcelable {

    @Id
    private long id;
    private String wechatNumber;
    private String nickName;
    private String avatarUrl;
    private Date joinTime;

    @Keep
    public User(long id, String wechatNumber, String nickName, String avatarUrl,
            Date joinTime) {
        this.id = id;
        this.wechatNumber = wechatNumber;
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.joinTime = joinTime;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.wechatNumber);
        dest.writeString(this.nickName);
        dest.writeString(this.avatarUrl);
        dest.writeLong(this.joinTime != null ? this.joinTime.getTime() : -1);
    }

    protected User(Parcel in) {
        this.id = in.readLong();
        this.wechatNumber = in.readString();
        this.nickName = in.readString();
        this.avatarUrl = in.readString();
        long tmpJoinTime = in.readLong();
        this.joinTime = tmpJoinTime == -1 ? null : new Date(tmpJoinTime);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
