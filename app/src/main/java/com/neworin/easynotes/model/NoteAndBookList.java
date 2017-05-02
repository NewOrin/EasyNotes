package com.neworin.easynotes.model;

import java.util.List;

public class NoteAndBookList {
        List<NoteBook> notebooks;
        List<Note> notes;
        long user_id;

        public NoteAndBookList() {
        }

        public NoteAndBookList(List<NoteBook> notebooks, List<Note> notes, long user_id) {
            this.notebooks = notebooks;
            this.notes = notes;
            this.user_id = user_id;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public List<NoteBook> getNotebooks() {
            return notebooks;
        }

        public void setNotebooks(List<NoteBook> notebooks) {
            this.notebooks = notebooks;
        }

        public List<Note> getNotes() {
            return notes;
        }

        public void setNotes(List<Note> notes) {
            this.notes = notes;
        }
    }