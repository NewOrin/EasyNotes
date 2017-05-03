package com.neworin.easynotes.http;

import android.content.Context;

import com.neworin.easynotes.DBManager;
import com.neworin.easynotes.event.NoteBookFragmentEvent;
import com.neworin.easynotes.event.SlideMenuEvent;
import com.neworin.easynotes.greendao.gen.DaoSession;
import com.neworin.easynotes.greendao.gen.NoteBookDao;
import com.neworin.easynotes.greendao.gen.NoteDao;
import com.neworin.easynotes.http.biz.NoteService;
import com.neworin.easynotes.model.Note;
import com.neworin.easynotes.model.NoteAndBookList;
import com.neworin.easynotes.model.NoteBook;
import com.neworin.easynotes.utils.Constant;
import com.neworin.easynotes.utils.DateUtil;
import com.neworin.easynotes.utils.L;
import com.neworin.easynotes.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by NewOrin Zhang on 2017/4/30.
 * Project : com.neworin.easynotes.http
 * Description:
 */

public class NoteBizImpl {

    private final String TAG = NoteBizImpl.class.getSimpleName();
    private DBManager mDBManager;
    private DaoSession mDaoSession;

    /**
     * 同步笔记本
     *
     * @param context
     */
    public void syncData(Context context) {
        final long user_id = Long.parseLong(SharedPreferenceUtil.getString(context, Constant.USER_ID));
        NoteService service = ServiceGenerator.createService(NoteService.class);
        mDBManager = DBManager.getInstance(context);
        mDaoSession = mDBManager.getWriteDaoSession();
        NoteBookDao noteBookDao = mDaoSession.getNoteBookDao();
        NoteDao noteDao = mDaoSession.getNoteDao();
        List<NoteBook> noteBookList = noteBookDao.queryBuilder().where(NoteBookDao.Properties.UserId.eq(user_id)).list();
        List<Note> noteList = noteDao.queryBuilder().where(NoteDao.Properties.UserId.eq(user_id)).list();
        NoteAndBookList noteAndBookList = new NoteAndBookList(noteBookList, noteList, user_id);
        Call<Response> call = service.syncNoteData(noteAndBookList);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                L.d(TAG, "数据同步成功");
                if (response.body().getMeta().isSuccess()) {
                    updateLocalDBStatus(user_id);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                L.d(TAG, "数据同步失败 error = " + t.getMessage());
            }
        });
    }

    /**
     * 将所有数据发送给服务端
     *
     * @param context
     */
    public void postAllDatas(final Context context) {
        final long user_id = Long.parseLong(SharedPreferenceUtil.getString(context, Constant.USER_ID));
        mDBManager = DBManager.getInstance(context);
        mDaoSession = mDBManager.getReadDaoSession();
        NoteBookDao noteBookDao = mDaoSession.getNoteBookDao();
        List<NoteBook> noteBookList = noteBookDao.queryBuilder().list();
        NoteDao noteDao = mDaoSession.getNoteDao();
        List<Note> noteList = noteDao.queryBuilder().list();
        NoteAndBookList noteAndBookList = new NoteAndBookList(noteBookList, noteList, user_id);
        NoteService service = ServiceGenerator.createService(NoteService.class);
        Call<NoteAndBookList> call = service.postNoteData(noteAndBookList);
        call.enqueue(new Callback<NoteAndBookList>() {
            @Override
            public void onResponse(Call<NoteAndBookList> call, retrofit2.Response<NoteAndBookList> response) {
                L.d(TAG, "返回 = " + response.body().toString());
                updateLocalDB(response.body(), user_id);
                SharedPreferenceUtil.putString(context, Constant.USER_SYNC_TIME, String.valueOf(DateUtil.getNowTime()));
                EventBus.getDefault().post(new NoteBookFragmentEvent.RefreshNoteEvent());
                EventBus.getDefault().post(new SlideMenuEvent.RefreshEvent());
            }

            @Override
            public void onFailure(Call<NoteAndBookList> call, Throwable t) {
                L.d(TAG, "数据发送到服务器失败 error = " + t.getMessage());
            }
        });
    }

    private void updateLocalDBStatus(long user_id) {
        mDaoSession = mDBManager.getWriteDaoSession();
        NoteDao noteDao = mDaoSession.getNoteDao();
        NoteBookDao noteBookDao = mDaoSession.getNoteBookDao();
        List<Note> notes = noteDao.queryBuilder().where(NoteDao.Properties.UserId.eq(user_id)).list();
        for (Note note : notes) {
            note.setStatus(Constant.STATUS_COMPLETED);
            noteDao.update(note);
        }
        List<NoteBook> noteBooks = noteBookDao.queryBuilder().where(NoteBookDao.Properties.UserId.eq(user_id)).list();
        for (NoteBook noteBook : noteBooks) {
            noteBook.setStatus(Constant.STATUS_COMPLETED);
        }
    }

    /**
     * 更新本地数据库存储
     *
     * @param noteAndBookList
     */
    private void updateLocalDB(NoteAndBookList noteAndBookList, long user_id) {
        mDaoSession = mDBManager.getWriteDaoSession();
        NoteDao noteDao = mDaoSession.getNoteDao();
        NoteBookDao noteBookDao = mDaoSession.getNoteBookDao();
        List<Note> notes = noteDao.queryBuilder().where(NoteDao.Properties.UserId.eq(user_id)).list();
        for (Note note : notes) {
            note.setStatus(Constant.STATUS_COMPLETED);
            noteDao.update(note);
        }
        List<NoteBook> noteBooks = noteBookDao.queryBuilder().where(NoteBookDao.Properties.UserId.eq(user_id)).list();
        for (NoteBook noteBook : noteBooks) {
            noteBook.setStatus(Constant.STATUS_COMPLETED);
        }
        insertLocalDB(noteAndBookList);
    }

    /**
     * 插入数据库数据
     *
     * @param noteAndBookList
     */
    private void insertLocalDB(NoteAndBookList noteAndBookList) {
        mDaoSession = mDBManager.getWriteDaoSession();
        NoteDao noteDao = mDaoSession.getNoteDao();
        NoteBookDao noteBookDao = mDaoSession.getNoteBookDao();
        List<NoteBook> noteBookList = noteAndBookList.getNotebooks();
        List<Note> noteList = noteAndBookList.getNotes();
        if (noteBookList != null && noteBookList.size() > 0) {
            for (NoteBook noteBook : noteBookList) {
                noteBookDao.insert(noteBook);
            }
        }
        if (noteList != null && noteList.size() > 0) {
            for (Note note : noteList) {
                noteDao.insert(note);
            }
        }
        mDaoSession.clear();
    }

}
