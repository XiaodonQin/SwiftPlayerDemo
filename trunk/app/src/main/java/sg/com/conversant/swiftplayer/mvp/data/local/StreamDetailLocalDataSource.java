package sg.com.conversant.swiftplayer.mvp.data.local;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.database.AppDatabase;
import sg.com.conversant.swiftplayer.mvp.data.database.DatabaseCreator;
import sg.com.conversant.swiftplayer.mvp.data.datasource.StreamDetailDataSource;
import sg.com.conversant.swiftplayer.sdk.API;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/9.
 */

public class StreamDetailLocalDataSource implements StreamDetailDataSource {
    private static String LOG_TAG = StreamDetailLocalDataSource.class.getSimpleName();

    @Nullable
    private static StreamDetailLocalDataSource INSTANCE;

    private AppDatabase mDb;

    private StreamDetailLocalDataSource(@NonNull Context context) {
        DatabaseCreator creator = DatabaseCreator.getInstance();

        if (!creator.isDbCreated()) {
            creator.createDb(context);
        }
    }

    public static StreamDetailLocalDataSource newInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            return new StreamDetailLocalDataSource(context);
        }

        return INSTANCE;
    }

    @Override
    public void getStreamDetail(long streamId, @NonNull LoadStreamDetailCallback callback) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null) {
            new AsyncTask<Void, Void, StreamDetail>() {
                @Override
                protected void onPostExecute(StreamDetail detail) {
                    super.onPostExecute(detail);

                    if (detail != null) {
                        callback.onStreamDetailLoaded(detail);
                    } else {
                        callback.onDataNotAvailable();
                    }
                }

                @Override
                protected StreamDetail doInBackground(Void... params) {
                    return mDb.streamDao().queryStreamDetailById(streamId);
                }
            }.execute();
        }
    }

    @Override
    public void getStreamDetailRecommends(@NonNull LoadStreamDetailRecommendCallback callback) {

    }

    @Override
    public void getStreamComments(@NonNull LoadStreamDetailCommentCallback callback) {

    }

    @Override
    public void saveAll(@NonNull StreamDetail detail) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null){
            new Thread(() -> {
                mDb.beginTransaction();
                try {
                    mDb.streamDao().insertAll(detail);
                    mDb.setTransactionSuccessful();
                } finally {
                    mDb.endTransaction();
                }
            }).start();
        }
    }

    @Override
    public void saveAll(@NonNull List<StreamItem> items) {

    }

    @Override
    public void saveAll() {

    }
}
