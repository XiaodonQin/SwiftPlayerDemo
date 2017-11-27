package sg.com.conversant.swiftplayer.mvp.data.datasource;

import android.support.annotation.NonNull;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsDetail;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/12.
 */

public interface MainContentDataSource {

    interface LoadMainContentCallback {

        void onStreamItemsLoaded(@NonNull List<StreamItem> items);

        void onDataNotAvailable();
    }

    void getMainPosterList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback);

    void getMainRecommendList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback);

    void getMainSeriesList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback);

    void getMainMovieList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback);

    void getMainLiveList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback);

    void getMainMusicList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback);

    void saveAll(@NonNull List<StreamItem> list);
}
