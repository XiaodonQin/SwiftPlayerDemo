package sg.com.conversant.swiftplayer.mvp.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.com.conversant.swiftplayer.mvp.data.RetrofitService;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.datasource.StreamDetailDataSource;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.sdk.Constants;
import sg.com.conversant.swiftplayer.utils.L;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/9.
 */

public class StreamDetailRemoteDataSource implements StreamDetailDataSource {
    private static String LOG_TAG = StreamDetailRemoteDataSource.class.getSimpleName();

    @Nullable
    private static StreamDetailRemoteDataSource INSTANCE;

    private RetrofitService.MainContentService service;

    private StreamDetailRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RetrofitService.MainContentService.class);
    }

    public static StreamDetailRemoteDataSource newInstance() {
        if (INSTANCE == null) {
            return new StreamDetailRemoteDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void getStreamDetail(long streamId, @NonNull LoadStreamDetailCallback callback) {
        L.i(LOG_TAG + " getStreamDetail");
        L.i(LOG_TAG + " streamId: " + streamId);
        service.getStreamDetail(streamId)
                .enqueue(new Callback<StreamDetail>() {
                    @Override
                    public void onResponse(Call<StreamDetail> call, Response<StreamDetail> response) {
                        L.i(LOG_TAG + " onResponse");
                        if (response.code() == 200) {
                            L.i(LOG_TAG + " onResponse.code: 200");
                            callback.onStreamDetailLoaded(response.body());
                        } else {
                            L.e(LOG_TAG + " onResponse.code: " + response.code() + ",\nmessage: " + response.errorBody().toString());
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(Call<StreamDetail> call, Throwable t) {
                        L.e(LOG_TAG + " onFailure.t: " + t.getMessage());
                        callback.onDataNotAvailable();
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

    }

    @Override
    public void saveAll(@NonNull List<StreamItem> items) {

    }

    @Override
    public void saveAll() {

    }
}
