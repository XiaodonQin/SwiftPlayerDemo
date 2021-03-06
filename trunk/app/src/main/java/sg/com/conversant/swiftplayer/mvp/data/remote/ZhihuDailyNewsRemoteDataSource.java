package sg.com.conversant.swiftplayer.mvp.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.com.conversant.swiftplayer.mvp.data.RetrofitService;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsCluster;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsDetail;
import sg.com.conversant.swiftplayer.mvp.data.datasource.ZhihuDailyNewsDataSource;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.sdk.Constants;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/13.
 */

public class ZhihuDailyNewsRemoteDataSource implements ZhihuDailyNewsDataSource {
    private static String LOG_TAG = ZhihuDailyNewsRemoteDataSource.class.getSimpleName();

    @Nullable
    private static ZhihuDailyNewsRemoteDataSource INSTANCE = null;

    private RetrofitService.ZhihuDailyService service;

    public ZhihuDailyNewsRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_ZHIHU_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RetrofitService.ZhihuDailyService.class);
    }

    //get data from server
    public static ZhihuDailyNewsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ZhihuDailyNewsRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getZhihuDailyNews(boolean forceUpdate, boolean clearData, long date, @NonNull final LoadZhihuDailyNewCallback callback) {
        L.i(LOG_TAG + " getZhihuDailyNews");
        L.i(LOG_TAG + " forceUpdate: " + forceUpdate + ", clearData: " + clearData + ", date: " + date);
        service.getZhihuDailyNewsList(Utils.formatZhihuDailyDateLongToString(date))
                .enqueue(new Callback<ZhihuDailyNewsCluster>() {
                    @Override
                    public void onResponse(Call<ZhihuDailyNewsCluster> call, Response<ZhihuDailyNewsCluster> response) {
                        L.i(LOG_TAG + " onResponse");
                        if (response.code() == 200) {
                            L.i(LOG_TAG + " onResponse.code: 200");

                            long timestamp = Utils.formatZhihuDailyDateStringToLong(response.body().getDate());
                            for (ZhihuDailyNews item : response.body().getStories()) {
                                item.setTimestamp(timestamp);
                                item.setRead(false);
                            }

                            L.i(LOG_TAG + " timestamp: " + timestamp + ", stories.size: " + response.body().getStories().size());
                            callback.onNewsLoaded(response.body().getStories());
                        } else {
                            L.e(LOG_TAG + " onResponse.code: " + response.code() + ",\nmessage: " + response.errorBody().toString());
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(Call<ZhihuDailyNewsCluster> call, Throwable t) {
                        L.e(LOG_TAG + " onFailure.t: " + t.getMessage());
                        callback.onDataNotAvailable();
                    }
                });

    }

    @Override
    public void saveAll(@NonNull List<ZhihuDailyNews> list) {

    }

    @Override
    public void update(ZhihuDailyNews item) {

    }

    @Override
    public void getZhihuDailyNewsDetail(long id, @NonNull LoadZhihuDailyNewCallback callback) {
        L.i(LOG_TAG + " getZhihuDailyNewsDetail");
        L.i(LOG_TAG + " id: " + id);
        service.getZhihuDailyNewsDetail(id)
                .enqueue(new Callback<ZhihuDailyNewsDetail>() {
                    @Override
                    public void onResponse(Call<ZhihuDailyNewsDetail> call, Response<ZhihuDailyNewsDetail> response) {
                        L.i(LOG_TAG + " onResponse");
                        if (response.code() == 200) {
                            L.i(LOG_TAG + " onResponse.code: 200");
                            callback.onDetailLoaded(response.body());
                        } else {
                            L.e(LOG_TAG + " onResponse.code: " + response.code() + ",\nmessage: " + response.errorBody().toString());
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(Call<ZhihuDailyNewsDetail> call, Throwable t) {
                        L.e(LOG_TAG + " onFailure.t: " + t.getMessage());
                        callback.onDataNotAvailable();
                    }
                });

    }

    @Override
    public void saveAll(@NonNull ZhihuDailyNewsDetail detail) {

    }
}
