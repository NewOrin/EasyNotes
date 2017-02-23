package com.neworin.easynotes.handlers;

import android.content.Context;
import android.view.View;

import com.neworin.easynotes.event.SlideMenuEvent;

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
        EventBus.getDefault().post(new SlideMenuEvent.HeaderLayoutEvent(view));
    }
}