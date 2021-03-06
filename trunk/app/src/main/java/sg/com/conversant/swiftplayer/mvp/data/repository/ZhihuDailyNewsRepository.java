package sg.com.conversant.swiftplayer.mvp.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsDetail;
import sg.com.conversant.swiftplayer.mvp.data.datasource.ZhihuDailyNewsDataSource;
import sg.com.conversant.swiftplayer.mvp.data.local.ZhihuDailyNewsLocalDataSource;
import sg.com.conversant.swiftplayer.mvp.data.remote.ZhihuDailyNewsRemoteDataSource;
import sg.com.conversant.swiftplayer.utils.L;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/13.
 */

public class ZhihuDailyNewsRepository implements ZhihuDailyNewsDataSource {
    private static final String LOG_TAG = ZhihuDailyNewsRepository.class.getSimpleName();

    @Nullable
    private static ZhihuDailyNewsRepository INSTANCE = null;

    @NonNull
    private ZhihuDailyNewsRemoteDataSource mRemoteDataSource;

    @NonNull
    private ZhihuDailyNewsLocalDataSource mLocalDataSource;

    private Map<Long, ZhihuDailyNews> mCachedItems;


    private ZhihuDailyNewsRepository(@NonNull ZhihuDailyNewsRemoteDataSource remoteDataSource,
                                     @NonNull ZhihuDailyNewsLocalDataSource localDataSource) {
        this.mRemoteDataSource = remoteDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static ZhihuDailyNewsRepository getInstance(@NonNull ZhihuDailyNewsRemoteDataSource remoteDataSource,
                                                       @NonNull ZhihuDailyNewsLocalDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ZhihuDailyNewsRepository(remoteDataSource, localDataSource);
        }

        return INSTANCE;
    }

    public static void destroyRepository() {
        INSTANCE = null;
    }

    @Override
    public void getZhihuDailyNews(final boolean forceUpdate, final boolean clearData, final long date, @NonNull final LoadZhihuDailyNewCallback callback) {
        L.i(LOG_TAG + " getZhihuDailyNews");

        if (mCachedItems != null && !forceUpdate) {
            callback.onNewsLoaded(new ArrayList<>(mCachedItems.values()));
            return;
        }

        mRemoteDataSource.getZhihuDailyNews(forceUpdate, clearData, date, new LoadZhihuDailyNewCallback() {
            @Override
            public void onNewsLoaded(@NonNull List<ZhihuDailyNews> list) {
                refreshCache(clearData, list);
                callback.onNewsLoaded(new ArrayList<>(mCachedItems.values()));

                saveAll(list);
            }

            @Override
            public void onDataNotAvailable() {

                mLocalDataSource.getZhihuDailyNews(false, false, date, new LoadZhihuDailyNewCallback() {
                    @Override
                    public void onNewsLoaded(@NonNull List<ZhihuDailyNews> list) {
                        refreshCache(false, list);
                        callback.onNewsLoaded(new ArrayList<>(mCachedItems.values()));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }

                    @Override
                    public void onDetailLoaded(@NonNull ZhihuDailyNewsDetail detail) {

                    }
                });
            }

            @Override
            public void onDetailLoaded(@NonNull ZhihuDailyNewsDetail detail) {

            }
        });
    }

    private void refreshCache(boolean clearCache, List<ZhihuDailyNews> list) {
        if (mCachedItems == null) {
            mCachedItems = new LinkedHashMap<>();
        }

        if (clearCache) {
            mCachedItems.clear();
        }

        for (ZhihuDailyNews item : list) {
            mCachedItems.put(item.getId(), item);
        }
    }

    @Override
    public void saveAll(@NonNull List<ZhihuDailyNews> list) {
        mRemoteDataSource.saveAll(list);
        mLocalDataSource.saveAll(list);
    }

    @Override
    public void update(ZhihuDailyNews item) {
        mRemoteDataSource.update(item);
        mLocalDataSource.update(item);
    }

    @Override
    public void getZhihuDailyNewsDetail(long id, @NonNull LoadZhihuDailyNewCallback callback) {
        mRemoteDataSource.getZhihuDailyNewsDetail(id, new LoadZhihuDailyNewCallback() {
            @Override
            public void onNewsLoaded(@NonNull List<ZhihuDailyNews> list) {

            }

            @Override
            public void onDataNotAvailable() {

                mLocalDataSource.getZhihuDailyNewsDetail(id, new LoadZhihuDailyNewCallback() {
                    @Override
                    public void onNewsLoaded(@NonNull List<ZhihuDailyNews> list) {

                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }

                    @Override
                    public void onDetailLoaded(@NonNull ZhihuDailyNewsDetail detail) {
                        callback.onDetailLoaded(detail);
                    }
                });
            }

            @Override
            public void onDetailLoaded(@NonNull ZhihuDailyNewsDetail detail) {
                callback.onDetailLoaded(detail);
                saveAll(detail);
            }
        });
    }

    @Override
    public void saveAll(@NonNull ZhihuDailyNewsDetail detail) {
        mRemoteDataSource.saveAll(detail);
        mLocalDataSource.saveAll(detail);
    }
}
