package com.neworin.easynotes.handlers;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.neworin.easynotes.event.SlideMenuEvent;
import com.neworin.easynotes.ui.activity.ChooseActivity;
import com.neworin.easynotes.ui.activity.PersonalActivity;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by NewOrin Zhang on 2017/2/20.
 * E-Mail : NewOrinZhang@Gmail.com
 */

public class SlideMenuEventHandler {

    private Context mContext;

    public SlideMenuEventHandler() {
    }

    public SlideMenuEventHandler(Context context) {
        this.mContext = context;
    }

    public void headLayoutClick(View view) {
        String email = SharedPreferenceUtil.getString(mContext, Constant.USER_EMAIL)+"9";
        if (email == null || email.equals("")) {
            mContext.startActivity(new Intent(mContext, ChooseActivity.class));
        } else {
            mContext.startActivity(new Intent(mContext, PersonalActivity.class));
        }
        EventBus.getDefault().post(new SlideMenuEvent.HeaderLayoutEvent(view));
    }
}