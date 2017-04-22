package com.neworin.easynotes.event;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.model.User;
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
        public SettingItemEvent(int flag, Activity activity) {
            if (flag == Constant.SLIDE_ITEM_SETTINGS) {
                activity.startActivity(new Intent(activity, SettingActivity.class));
            } else if (flag == Constant.SLIDE_ITEM_EDIT) {
                activity.startActivityForResult(new Intent(activity, EditNoteBookActivity.class), Constant.EDIT_BOOK_RESULT_CODE);
            }
        }
    }

    public static class RefreshEvent {
        public RefreshEvent() {
        }
    }

    public static class RefreshUserEvent {
        public User mUser;

        public RefreshUserEvent(User user) {
            this.mUser = user;
        }
    }
}
