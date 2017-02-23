package com.neworin.easynotes.event;

import android.view.View;

/**
 * Created by NewOrin Zhang on 2017/2/20.
 * E-Mail : NewOrinZhang@Gmail.com
 */

public class SlideMenuEvent {
    public static class HeaderLayoutEvent {
        private View mView;

        public HeaderLayoutEvent(View v) {
            mView = v;
        }

        public View getView() {
            return mView;
        }
    }

    public static class ListItemEvent {
        private int position;

        public ListItemEvent(int pos) {
            this.position = pos;
        }

        public int getPosition() {
            return position;
        }
    }
}
