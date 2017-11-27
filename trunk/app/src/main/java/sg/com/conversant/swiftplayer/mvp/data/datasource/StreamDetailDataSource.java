package sg.com.conversant.swiftplayer.mvp.data.datasource;

import android.support.annotation.NonNull;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.database.StreamDao_Impl;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/9.
 */

public interface StreamDetailDataSource {

    interface LoadStreamDetailCallback {
        void onStreamDetailLoaded(@NonNull StreamDetail detail);

        void onDataNotAvailable();
    }

    interface LoadStreamDetailRecommendCallback {
        void RecommendLoaded(@NonNull List<StreamItem> items);

        void onDataNotAvailable();
    }

    interface LoadStreamDetailCommentCallback {
        void onCommentLoaded();

        void onDataNotAvailable();
    }

    void getStreamDetail(long streamId, @NonNull LoadStreamDetailCallback callback);

    void getStreamDetailRecommends(@NonNull LoadStreamDetailRecommendCallback callback);

    void getStreamComments(@NonNull LoadStreamDetailCommentCallback callback);

    void saveAll(@NonNull StreamDetail detail);

    void saveAll(@NonNull List<StreamItem> items);

    void saveAll();
}
