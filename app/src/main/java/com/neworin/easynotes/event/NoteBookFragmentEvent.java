package com.neworin.easynotes.event;

import android.view.View;

/**
 * Created by NewOrin Zhang on 2017/3/16.
 * E-mail : NewOrinZhang@Gmail.com
 */

public class NoteBookFragmentEvent {

    public static class RefreshNoteEvent {

        private View mView;

        public RefreshNoteEvent() {
        }
    }

    public static class ShowThumbEvent {
        public ShowThumbEvent() {
        }
    }
    public static class ShowTitleEvent {
        public ShowTitleEvent() {
        }
    }
}
