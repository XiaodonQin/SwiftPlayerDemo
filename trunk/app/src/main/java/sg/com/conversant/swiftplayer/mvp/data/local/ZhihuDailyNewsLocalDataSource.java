package sg.com.conversant.swiftplayer.mvp.data.local;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.com.conversant.swiftplayer.mvp.data.RetrofitService;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsCluster;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsDetail;
import sg.com.conversant.swiftplayer.mvp.data.database.AppDatabase;
import sg.com.conversant.swiftplayer.mvp.data.database.DatabaseCreator;
import sg.com.conversant.swiftplayer.mvp.data.datasource.ZhihuDailyNewsDataSource;
import sg.com.conversant.swiftplayer.sdk.Constants;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/13.
 */

public class ZhihuDailyNewsLocalDataSource implements ZhihuDailyNewsDataSource {
    private static String LOG_TAG = ZhihuDailyNewsLocalDataSource.class.getSimpleName();

    @Nullable
    private static ZhihuDailyNewsLocalDataSource INSTANCE = null;

    @Nullable
    private AppDatabase mDb;

    public ZhihuDailyNewsLocalDataSource(@NonNull Context context) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance();
        if (!dbCreator.isDbCreated()) {
            dbCreator.createDb(context);
        }
    }

    //get data from server
    public static ZhihuDailyNewsLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ZhihuDailyNewsLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getZhihuDailyNews(boolean forceUpdate, boolean clearData, final long date, @NonNull final LoadZhihuDailyNewCallback callback) {
        L.i(LOG_TAG + " getZhihuDailyNews");
        L.i(LOG_TAG + " forceUpdate: " + forceUpdate + ", clearData: " + clearData + ", date: " + date);

        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null) {
            new AsyncTask<Void, Void, List<ZhihuDailyNews>>() {
                @Override
                protected void onPostExecute(List<ZhihuDailyNews> list) {
                    super.onPostExecute(list);

                    if (list != null) {
                        callback.onNewsLoaded(list);
                    } else {
                        callback.onDataNotAvailable();
                    }
                }

                @Override
                protected List<ZhihuDailyNews> doInBackground(Void... params) {
                    return mDb.zhihuDailyNewsDao().queryAllByDate(date);
                }
            }.execute();
        }

    }

    @Override
    public void saveAll(@NonNull List<ZhihuDailyNews> list) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null){
            new Thread(() -> {
                mDb.beginTransaction();
                try {
                    mDb.zhihuDailyNewsDao().insertAll(list);
                    mDb.setTransactionSuccessful();
                } finally {
                    mDb.endTransaction();
                }
            }).start();
        }
    }

    @Override
    public void update(ZhihuDailyNews item) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null){
            new Thread(() -> {
                mDb.beginTransaction();
                try {
                    mDb.zhihuDailyNewsDao().updateZhihuNews(item);
                    mDb.setTransactionSuccessful();
                } finally {
                    mDb.endTransaction();
                }
            }).start();
        }
    }

    @Override
    public void getZhihuDailyNewsDetail(long id, @NonNull LoadZhihuDailyNewCallback callback) {
        L.i(LOG_TAG + " getZhihuDailyNewsDetail");
        L.i(LOG_TAG + " id: " + id);

        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null) {
            new AsyncTask<Void, Void, ZhihuDailyNewsDetail>() {
                @Override
                protected void onPostExecute(ZhihuDailyNewsDetail detail) {
                    super.onPostExecute(detail);

                    if (detail != null) {
                        callback.onDetailLoaded(detail);
                    } else {
                        callback.onDataNotAvailable();
                    }
                }

                @Override
                protected ZhihuDailyNewsDetail doInBackground(Void... params) {
                    return mDb.zhihuDailyNewsDao().queryById(id);
                }
            }.execute();
        }
    }

    @Override
    public void saveAll(@NonNull ZhihuDailyNewsDetail detail) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null){
            new Thread(() -> {
                mDb.beginTransaction();
                try {
                    mDb.zhihuDailyNewsDao().insertAll(detail);
                    mDb.setTransactionSuccessful();
                } finally {
                    mDb.endTransaction();
                }
            }).start();
        }
    }
}
