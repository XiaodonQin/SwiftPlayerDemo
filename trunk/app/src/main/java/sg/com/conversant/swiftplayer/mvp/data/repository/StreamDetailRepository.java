package sg.com.conversant.swiftplayer.mvp.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.datasource.StreamDetailDataSource;
import sg.com.conversant.swiftplayer.mvp.data.local.StreamDetailLocalDataSource;
import sg.com.conversant.swiftplayer.mvp.data.remote.StreamDetailRemoteDataSource;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/9.
 */

public class StreamDetailRepository implements StreamDetailDataSource {
    private static String LOG_TAG = StreamDetailRepository.class.getSimpleName();

    @Nullable
    private static StreamDetailRepository INSTANCE;

    private StreamDetailRemoteDataSource mRemoteDataSource;
    private StreamDetailLocalDataSource mLocalDataSource;

    public StreamDetailRepository(@NonNull StreamDetailRemoteDataSource remoteDataSource,
                                  @NonNull StreamDetailLocalDataSource localDataSource) {
        this.mRemoteDataSource = remoteDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static StreamDetailRepository newInstance(@NonNull StreamDetailRemoteDataSource remoteDataSource,
                                                     @NonNull StreamDetailLocalDataSource localDataSource) {
        if (INSTANCE == null) {
            return new StreamDetailRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getStreamDetail(long streamId, @NonNull LoadStreamDetailCallback callback) {
        mRemoteDataSource.getStreamDetail(streamId, new LoadStreamDetailCallback() {
            @Override
            public void onStreamDetailLoaded(@NonNull StreamDetail detail) {
                callback.onStreamDetailLoaded(detail);
                saveAll(detail);
            }

            @Override
            public void onDataNotAvailable() {
                mLocalDataSource.getStreamDetail(streamId, new LoadStreamDetailCallback() {
                    @Override
                    public void onStreamDetailLoaded(@NonNull StreamDetail detail) {
                        callback.onStreamDetailLoaded(detail);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getStreamDetailRecommends(@NonNull LoadStreamDetailRecommendCallback callback) {

    }

    @Override
    public void getStreamComments(@NonNull LoadStreamDetailCommentCallback callback) {

    }

    @Override
    public void saveAll(@NonNull StreamDetail detail) {
        mRemoteDataSource.saveAll(detail);
        mLocalDataSource.saveAll(detail);
    }

    @Override
    public void saveAll(@NonNull List<StreamItem> items) {

    }

    @Override
    public void saveAll() {

    }
}
