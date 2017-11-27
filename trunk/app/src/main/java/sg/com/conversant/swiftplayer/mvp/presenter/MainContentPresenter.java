package sg.com.conversant.swiftplayer.mvp.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.contact.MainContentContact;
import sg.com.conversant.swiftplayer.mvp.data.repository.MainContentRepository;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.datasource.MainContentDataSource;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/1.
 */

public class MainContentPresenter implements MainContentContact.Presenter {
    private static String LOG_TAG = MainContentPresenter.class.getSimpleName();

    @NonNull
    private MainContentContact.View mView;

    @NonNull
    private MainContentRepository mRepository;

    public MainContentPresenter(@NonNull MainContentContact.View view,
                                @NonNull MainContentRepository repository) {
        this.mView = view;
        this.mRepository = repository;

        this.mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadPosterList(boolean forceUpdate, boolean clearCache) {

        if (forceUpdate) {
            mView.showLoadingIndicator(true);
        }

        mView.setResponseOK(false);

        mRepository.getMainPosterList(forceUpdate, clearCache, new MainContentDataSource.LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                if (mView.isActive()) {
                    mView.showPosterListResult(items);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.showStaticPoster();
                }
            }
        });

    }

    @Override
    public void loadRecommendList(boolean forceUpdate, boolean clearCache) {
        mRepository.getMainRecommendList(forceUpdate, clearCache, new MainContentDataSource.LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                if (mView.isActive()) {
                    mView.showRecommendListResult(items);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.skipShowRecommend();
                }
            }
        });
    }

    @Override
    public void loadSeriesList(boolean forceUpdate, boolean clearCache) {
        mRepository.getMainSeriesList(forceUpdate, clearCache, new MainContentDataSource.LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                if (mView.isActive()) {
                    mView.showSeriesListResult(items);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.skipShowSeries();
                }
            }
        });
    }

    @Override
    public void loadMovieList(boolean forceUpdate, boolean clearCache) {
        mRepository.getMainMovieList(forceUpdate, clearCache, new MainContentDataSource.LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                if (mView.isActive()) {
                    mView.showMovieListResult(items);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.skipShowMovie();
                }
            }
        });
    }

    @Override
    public void loadLiveList(boolean forceUpdate, boolean clearCache) {
        mRepository.getMainLiveList(forceUpdate, clearCache, new MainContentDataSource.LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                if (mView.isActive()) {
                    mView.showLiveListResult(items);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.skipShowLive();
                }
            }
        });
    }

    @Override
    public void loadMusicList(boolean forceUpdate, boolean clearCache) {
        mRepository.getMainMusicList(forceUpdate, clearCache, new MainContentDataSource.LoadMainContentCallback() {
            @Override
            public void onStreamItemsLoaded(@NonNull List<StreamItem> items) {
                if (mView.isActive()) {
                    mView.showMusicListResult(items);
                    mView.showLoadingIndicator(false);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.skipShowMusic();
                    mView.showLoadingIndicator(false);
                }
            }
        });
    }
}
