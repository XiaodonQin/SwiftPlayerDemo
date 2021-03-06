package sg.com.conversant.swiftplayer.mvp.data.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsDetail;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/12.
 */

public interface ZhihuDailyNewsDataSource {

    interface LoadZhihuDailyNewCallback {

        void onNewsLoaded(@NonNull List<ZhihuDailyNews> list);

        void onDetailLoaded(@NonNull ZhihuDailyNewsDetail detail);

        void onDataNotAvailable();
    }

    void getZhihuDailyNews(boolean forceUpdate, boolean clearData, long date, @NonNull LoadZhihuDailyNewCallback callback);

    void saveAll(@NonNull List<ZhihuDailyNews> list);

    void update(ZhihuDailyNews item);

    void getZhihuDailyNewsDetail(long id, @NonNull LoadZhihuDailyNewCallback callback);

    void saveAll(@NonNull ZhihuDailyNewsDetail detail);
}
