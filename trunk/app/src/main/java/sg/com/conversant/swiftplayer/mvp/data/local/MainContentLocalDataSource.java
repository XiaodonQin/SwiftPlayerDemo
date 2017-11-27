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
import sg.com.conversant.swiftplayer.mvp.data.datasource.MainContentDataSource;
import sg.com.conversant.swiftplayer.sdk.API;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/2.
 */

public class MainContentLocalDataSource implements MainContentDataSource {
    private static String LOG_TAG = MainContentLocalDataSource.class.getSimpleName();

    @Nullable
    private static MainContentLocalDataSource INSTANCE;

    @Nullable
    private AppDatabase mDb;

    private MainContentLocalDataSource(@NonNull Context context) {
        DatabaseCreator databaseCreator = DatabaseCreator.getInstance();
        if (!databaseCreator.isDbCreated()) {
            databaseCreator.createDb(context);
        }
    }

    public static MainContentLocalDataSource newInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            return new MainContentLocalDataSource(context);
        }

        return INSTANCE;
    }

    @Override
    public void getMainPosterList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {

        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null) {
            new AsyncTask<Void, Void, List<StreamItem>>() {
                @Override
                protected void onPostExecute(List<StreamItem> list) {
                    super.onPostExecute(list);

                    if (list != null) {
                        callback.onStreamItemsLoaded(list);
                    } else {
                        callback.onDataNotAvailable();
                    }
                }

                @Override
                protected List<StreamItem> doInBackground(Void... params) {
                    return mDb.mainContentDao().queryAllStreamByType(API.API_STREAM_TYPE.TYPE_POSTER);
                }
            }.execute();
        }

    }

    @Override
    public void getMainRecommendList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null) {
            new AsyncTask<Void, Void, List<StreamItem>>() {
                @Override
                protected void onPostExecute(List<StreamItem> list) {
                    super.onPostExecute(list);

                    if (list != null) {
                        callback.onStreamItemsLoaded(list);
                    } else {
                        callback.onDataNotAvailable();
                    }
                }

                @Override
                protected List<StreamItem> doInBackground(Void... params) {
                    return mDb.mainContentDao().queryAllStreamByType(API.API_STREAM_TYPE.TYPE_RECOMMEND);
                }
            }.execute();
        }
    }

    @Override
    public void getMainSeriesList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null) {
            new AsyncTask<Void, Void, List<StreamItem>>() {
                @Override
                protected void onPostExecute(List<StreamItem> list) {
                    super.onPostExecute(list);

                    if (list != null) {
                        callback.onStreamItemsLoaded(list);
                    } else {
                        callback.onDataNotAvailable();
                    }
                }

                @Override
                protected List<StreamItem> doInBackground(Void... params) {
                    return mDb.mainContentDao().queryAllStreamByType(API.API_STREAM_TYPE.TYPE_SERIES);
                }
            }.execute();
        }
    }

    @Override
    public void getMainMovieList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null) {
            new AsyncTask<Void, Void, List<StreamItem>>() {
                @Override
                protected void onPostExecute(List<StreamItem> list) {
                    super.onPostExecute(list);

                    if (list != null) {
                        callback.onStreamItemsLoaded(list);
                    } else {
                        callback.onDataNotAvailable();
                    }
                }

                @Override
                protected List<StreamItem> doInBackground(Void... params) {
                    return mDb.mainContentDao().queryAllStreamByType(API.API_STREAM_TYPE.TYPE_MOVIE);
                }
            }.execute();
        }
    }

    @Override
    public void getMainLiveList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null) {
            new AsyncTask<Void, Void, List<StreamItem>>() {
                @Override
                protected void onPostExecute(List<StreamItem> list) {
                    super.onPostExecute(list);

                    if (list != null) {
                        callback.onStreamItemsLoaded(list);
                    } else {
                        callback.onDataNotAvailable();
                    }
                }

                @Override
                protected List<StreamItem> doInBackground(Void... params) {
                    return mDb.mainContentDao().queryAllStreamByType(API.API_STREAM_TYPE.TYPE_LIVE);
                }
            }.execute();
        }
    }

    @Override
    public void getMainMusicList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null) {
            new AsyncTask<Void, Void, List<StreamItem>>() {
                @Override
                protected void onPostExecute(List<StreamItem> list) {
                    super.onPostExecute(list);

                    if (list != null) {
                        callback.onStreamItemsLoaded(list);
                    } else {
                        callback.onDataNotAvailable();
                    }
                }

                @Override
                protected List<StreamItem> doInBackground(Void... params) {
                    return mDb.mainContentDao().queryAllStreamByType(API.API_STREAM_TYPE.TYPE_MUSIC);
                }
            }.execute();
        }
    }

    @Override
    public void saveAll(@NonNull List<StreamItem> list) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDb != null){
            new Thread(() -> {
                mDb.beginTransaction();
                try {
                    mDb.mainContentDao().insertAll(list);
                    mDb.setTransactionSuccessful();
                } finally {
                    mDb.endTransaction();
                }
            }).start();
        }
    }
}
