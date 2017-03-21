package com.neworin.easynotes.event;

import android.view.View;

import com.neworin.easynotes.model.NoteBook;

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
        private NoteBook mNoteBook;

        public ListItemEvent(NoteBook noteBook) {
            this.mNoteBook = noteBook;
        }

        public int getPosition() {
            return position;
        }

        public NoteBook getNoteBook() {
            return mNoteBook;
        }
    }

    public static class SettingItemEvent {
        public SettingItemEvent(int flag) {
        }
    }
}
