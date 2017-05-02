package com.neworin.easynotes.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

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
    private String email;
    private String password;
    private String nickname;
    private String avatarurl;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date jointime;

    @Keep
    public User(long id, String email, String password, String nickname, String avatarurl,
                Date jointime) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.avatarurl = avatarurl;
        this.jointime = jointime;
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
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bindable
    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    @Bindable
    public Date getJointime() {
        return jointime;
    }

    public void setJointime(Date jointime) {
        this.jointime = jointime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.nickname);
        dest.writeString(this.avatarurl);
        dest.writeLong(this.jointime != null ? this.jointime.getTime() : -1);
    }

    protected User(Parcel in) {
        this.id = in.readLong();
        this.email = in.readString();
        this.password = in.readString();
        this.nickname = in.readString();
        this.avatarurl = in.readString();
        long tmpJoinTime = in.readLong();
        this.jointime = tmpJoinTime == -1 ? null : new Date(tmpJoinTime);
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatarurl='" + avatarurl + '\'' +
                ", jointime=" + jointime +
                '}';
    }
}
