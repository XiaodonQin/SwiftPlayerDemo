package sg.com.conversant.swiftplayer.mvp.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.datasource.MainContentDataSource;
import sg.com.conversant.swiftplayer.mvp.data.local.MainContentLocalDataSource;
import sg.com.conversant.swiftplayer.mvp.data.remote.MainContentRemoteDataSource;
import sg.com.conversant.swiftplayer.utils.L;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/1.
 */

public class MainContentRepository implements MainContentDataSource {
    private static String LOG_TAG = MainContentRepository.class.getSimpleName();

    @Nullable
    private static MainContentRepository INSTANCE;

    @NonNull
    private MainContentRemoteDataSource mainContentRemoteDataSource;

    @NonNull
    private MainContentLocalDataSource mainContentLocalDataSource;

    private Map<Long, StreamItem> mPosterCaches;
    private Map<Long, StreamItem> mRecommendCaches;
    private Map<Long, StreamItem> mSeriesCaches;
    private Map<Long, StreamItem> mMovieCaches;
    private Map<Long, StreamItem> mLiveCaches;
    private Map<Long, StreamItem> mMusicCaches;

    private MainContentRepository(@NonNull MainContentRemoteDataSource remoteDataSource,
                                  @NonNull MainContentLocalDataSource localDataSource) {
        this.mainContentRemoteDataSource = remoteDataSource;
        this.mainContentLocalDataSource = localDataSource;
    }

    public static MainContentRepository newInstance(@NonNull MainContentRemoteDataSource remoteDataSource,
                                                    @NonNull MainContentLocalDataSource localDataSource) {
        if (INSTANCE == null) {
            return new MainContentRepository(remoteDataSource, localDataSource);
        }

        return INSTANCE;
    }

    @Override
    public void getMainPosterList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        if (mPosterCaches != null && !forceUpdate) {
            return;
        }

        mainContentRemoteDataSource.getMainPosterList(forceUpdate, clearData, new LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                refreshPosterCache(clearData, items);
                callback.onStreamItemsLoaded(new ArrayList<>(mPosterCaches.values()));

                saveAll(items);
            }

            @Override
            public void onDataNotAvailable() {
                mainContentLocalDataSource.getMainPosterList(false, false, new LoadMainContentCallback() {
                    @Override
                    public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                        L.i(LOG_TAG + " mainContentLocalDataSource.getMainLiveList.size: " + items.size());
                        refreshPosterCache(true, items);
                        callback.onStreamItemsLoaded(new ArrayList<>(mPosterCaches.values()));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    private void refreshPosterCache(boolean clearCache, List<StreamItem> items) {
        if (mPosterCaches == null) {
            mPosterCaches = new LinkedHashMap<>();
        }

        if (clearCache) {
            mPosterCaches.clear();
        }

        for (StreamItem item : items) {
            mPosterCaches.put(item.getId(), item);
        }
    }

    @Override
    public void getMainRecommendList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        if (mRecommendCaches != null && !forceUpdate) {
            return;
        }

        mainContentRemoteDataSource.getMainRecommendList(forceUpdate, clearData, new LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                refreshRecommendCache(clearData, items);
                callback.onStreamItemsLoaded(new ArrayList<>(mRecommendCaches.values()));

                saveAll(items);
            }

            @Override
            public void onDataNotAvailable() {
                mainContentLocalDataSource.getMainRecommendList(false, false, new LoadMainContentCallback() {
                    @Override
                    public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                        L.i(LOG_TAG + " mainContentLocalDataSource.getMainRecommendList.size: " + items.size());
                        refreshRecommendCache(true, items);
                        callback.onStreamItemsLoaded(new ArrayList<>(mRecommendCaches.values()));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    private void refreshRecommendCache(boolean clearCache, List<StreamItem> items) {
        if (mRecommendCaches == null) {
            mRecommendCaches = new LinkedHashMap<>();
        }

        if (clearCache) {
            mRecommendCaches.clear();
        }

        for (StreamItem item : items) {
            mRecommendCaches.put(item.getId(), item);
        }
    }

    @Override
    public void getMainSeriesList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        if (mSeriesCaches != null && !forceUpdate) {
            return;
        }

        mainContentRemoteDataSource.getMainSeriesList(forceUpdate, clearData, new LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                refreshSeriesCache(clearData, items);
                callback.onStreamItemsLoaded(new ArrayList<>(mSeriesCaches.values()));

                saveAll(items);
            }

            @Override
            public void onDataNotAvailable() {
                mainContentLocalDataSource.getMainSeriesList(false, false, new LoadMainContentCallback() {
                    @Override
                    public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                        L.i(LOG_TAG + " mainContentLocalDataSource.getMainSeriesList.size: " + items.size());
                        refreshSeriesCache(true, items);
                        callback.onStreamItemsLoaded(new ArrayList<>(mSeriesCaches.values()));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    private void refreshSeriesCache(boolean clearCache, List<StreamItem> items) {
        if (mSeriesCaches == null) {
            mSeriesCaches = new LinkedHashMap<>();
        }

        if (clearCache) {
            mSeriesCaches.clear();
        }

        for (StreamItem item : items) {
            mSeriesCaches.put(item.getId(), item);
        }
    }

    @Override
    public void getMainMovieList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        if (mMovieCaches != null && !forceUpdate) {
            return;
        }

        mainContentRemoteDataSource.getMainMovieList(forceUpdate, clearData, new LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                refreshMovieCache(clearData, items);
                callback.onStreamItemsLoaded(new ArrayList<>(mMovieCaches.values()));

                saveAll(items);
            }

            @Override
            public void onDataNotAvailable() {
                mainContentLocalDataSource.getMainMovieList(false, false, new LoadMainContentCallback() {
                    @Override
                    public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                        L.i(LOG_TAG + " mainContentLocalDataSource.getMainMovieList.size: " + items.size());
                        refreshMovieCache(true, items);
                        callback.onStreamItemsLoaded(new ArrayList<>(mMovieCaches.values()));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    private void refreshMovieCache(boolean clearCache, List<StreamItem> items) {
        if (mMovieCaches == null) {
            mMovieCaches = new LinkedHashMap<>();
        }

        if (clearCache) {
            mMovieCaches.clear();
        }

        for (StreamItem item : items) {
            mMovieCaches.put(item.getId(), item);
        }
    }

    @Override
    public void getMainLiveList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        if (mLiveCaches != null && !forceUpdate) {
            return;
        }

        mainContentRemoteDataSource.getMainLiveList(forceUpdate, clearData, new LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                refreshLiveCache(clearData, items);
                callback.onStreamItemsLoaded(new ArrayList<>(mLiveCaches.values()));

                saveAll(items);
            }

            @Override
            public void onDataNotAvailable() {
                mainContentLocalDataSource.getMainLiveList(false, false, new LoadMainContentCallback() {
                    @Override
                    public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                        L.i(LOG_TAG + " mainContentLocalDataSource.getMainLiveList.size: " + items.size());
                        refreshLiveCache(true, items);
                        callback.onStreamItemsLoaded(new ArrayList<>(mLiveCaches.values()));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    private void refreshLiveCache(boolean clearCache, List<StreamItem> items) {
        if (mLiveCaches == null) {
            mLiveCaches = new LinkedHashMap<>();
        }

        if (clearCache) {
            mLiveCaches.clear();
        }

        for (StreamItem item : items) {
            mLiveCaches.put(item.getId(), item);
        }
    }

    @Override
    public void getMainMusicList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        if (mMusicCaches != null && !forceUpdate) {
            return;
        }

        mainContentRemoteDataSource.getMainMusicList(forceUpdate, clearData, new LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                refreshMusicCache(clearData, items);
                callback.onStreamItemsLoaded(new ArrayList<>(mMusicCaches.values()));

                saveAll(items);
            }

            @Override
            public void onDataNotAvailable() {
                mainContentLocalDataSource.getMainMusicList(false, false, new LoadMainContentCallback() {
                    @Override
                    public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                        L.i(LOG_TAG + " mainContentLocalDataSource.getMainMusicList.size: " + items.size());
                        refreshMusicCache(true, items);
                        callback.onStreamItemsLoaded(new ArrayList<>(mMusicCaches.values()));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    private void refreshMusicCache(boolean clearCache, List<StreamItem> items) {
        if (mMusicCaches == null) {
            mMusicCaches = new LinkedHashMap<>();
        }

        if (clearCache) {
            mMusicCaches.clear();
        }

        for (StreamItem item : items) {
            mMusicCaches.put(item.getId(), item);
        }
    }

    @Override
    public void saveAll(@NonNull List<StreamItem> list) {
        mainContentRemoteDataSource.saveAll(list);
        mainContentLocalDataSource.saveAll(list);

    }
}
