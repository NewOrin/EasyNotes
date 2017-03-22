package com.neworin.easynotes.event;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.ui.activity.EditNoteBookActivity;
import com.neworin.easynotes.ui.activity.SettingActivity;
import com.neworin.easynotes.utils.Constant;

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
        public SettingItemEvent(int flag, Context context) {
            if (flag == Constant.SLIDE_ITEM_SETTINGS) {
                context.startActivity(new Intent(context, SettingActivity.class));
            } else if (flag == Constant.SLIDE_ITEM_EDIT) {
                context.startActivity(new Intent(context, EditNoteBookActivity.class));
            }
        }
    }
}
