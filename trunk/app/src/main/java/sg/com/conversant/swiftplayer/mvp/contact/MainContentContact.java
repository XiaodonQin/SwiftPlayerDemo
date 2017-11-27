package sg.com.conversant.swiftplayer.mvp.contact;

import android.support.annotation.NonNull;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.BasePresenter;
import sg.com.conversant.swiftplayer.mvp.BaseView;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/1.
 */

public interface MainContentContact {

    interface Presenter extends BasePresenter {
        void loadPosterList(boolean forceUpdate, boolean clearCache);

        void loadRecommendList(boolean forceUpdate, boolean clearCache);

        void loadSeriesList(boolean forceUpdate, boolean clearCache);

        void loadMovieList(boolean forceUpdate, boolean clearCache);

        void loadLiveList(boolean forceUpdate, boolean clearCache);

        void loadMusicList(boolean forceUpdate, boolean clearCache);
    }

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showLoadingIndicator(boolean active);

        void showPosterListResult(@NonNull List<StreamItem> results);

        void showStaticPoster();

        void showRecommendListResult(@NonNull List<StreamItem> results);

        void skipShowRecommend();

        void showSeriesListResult(@NonNull List<StreamItem> results);

        void skipShowSeries();

        void showMovieListResult(@NonNull List<StreamItem> results);

        void skipShowMovie();

        void showLiveListResult(@NonNull List<StreamItem> results);

        void skipShowLive();

        void showMusicListResult(@NonNull List<StreamItem> results);

        void skipShowMusic();

        void setResponseOK(boolean responseOK);
    }
}
