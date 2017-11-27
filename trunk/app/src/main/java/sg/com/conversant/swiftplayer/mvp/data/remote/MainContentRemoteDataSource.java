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
import sg.com.conversant.swiftplayer.mvp.data.datasource.MainContentDataSource;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.sdk.Constants;
import sg.com.conversant.swiftplayer.utils.L;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/1.
 */

public class MainContentRemoteDataSource implements MainContentDataSource {
    private static String LOG_TAG = MainContentRemoteDataSource.class.getSimpleName();

    @Nullable
    private static MainContentRemoteDataSource INSTANCE;

    private RetrofitService.MainContentService service;

    private MainContentRemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(RetrofitService.MainContentService.class);
    }

    public static MainContentRemoteDataSource newInstance() {
        if (INSTANCE == null) {
            return new MainContentRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getMainPosterList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        L.i(LOG_TAG + " getMainPosterList");
        L.i(LOG_TAG + " forceUpdate: " + forceUpdate + ", clearData: " + clearData);
        service.getMainContentPosterList()
                .enqueue(new Callback<List<StreamItem>>() {
                    @Override
                    public void onResponse(Call<List<StreamItem>> call, Response<List<StreamItem>> response) {
                        L.i(LOG_TAG + " onResponse");
                        if (response.code() == 200) {
                            L.i(LOG_TAG + " onResponse.code: 200");

                            int streamType = API.API_STREAM_TYPE.TYPE_POSTER;
                            for (StreamItem item : response.body()) {
                                item.setType(streamType);
                            }

                            L.i(LOG_TAG + " streamType: " + streamType + ", streams.size: " + response.body().size());
                            callback.onStreamItemsLoaded(response.body());
                        } else {
                            L.e(LOG_TAG + " onResponse.code: " + response.code() + ",\nmessage: " + response.errorBody().toString());
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StreamItem>> call, Throwable t) {
                        L.e(LOG_TAG + " onFailure.t: " + t.getMessage());
                        callback.onDataNotAvailable();
                    }
                });

    }

    @Override
    public void getMainRecommendList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        L.i(LOG_TAG + " getMainRecommendList");
        L.i(LOG_TAG + " forceUpdate: " + forceUpdate + ", clearData: " + clearData);
        service.getMainContentRecommendList()
                .enqueue(new Callback<List<StreamItem>>() {
                    @Override
                    public void onResponse(Call<List<StreamItem>> call, Response<List<StreamItem>> response) {
                        L.i(LOG_TAG + " onResponse");
                        if (response.code() == 200) {
                            L.i(LOG_TAG + " onResponse.code: 200");

                            int streamType = API.API_STREAM_TYPE.TYPE_RECOMMEND;
                            for (StreamItem item : response.body()) {
                                item.setType(streamType);
                            }

                            L.i(LOG_TAG + " streamType: " + streamType + ", streams.size: " + response.body().size());
                            callback.onStreamItemsLoaded(response.body());
                        } else {
                            L.e(LOG_TAG + " onResponse.code: " + response.code() + ",\nmessage: " + response.errorBody().toString());
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StreamItem>> call, Throwable t) {
                        L.e(LOG_TAG + " onFailure.t: " + t.getMessage());
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void getMainSeriesList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        L.i(LOG_TAG + " getMainSeriesList");
        L.i(LOG_TAG + " forceUpdate: " + forceUpdate + ", clearData: " + clearData);
        service.getMainContentSeriesList()
                .enqueue(new Callback<List<StreamItem>>() {
                    @Override
                    public void onResponse(Call<List<StreamItem>> call, Response<List<StreamItem>> response) {
                        L.i(LOG_TAG + " onResponse");
                        if (response.code() == 200) {
                            L.i(LOG_TAG + " onResponse.code: 200");

                            int streamType = API.API_STREAM_TYPE.TYPE_SERIES;
                            for (StreamItem item : response.body()) {
                                item.setType(streamType);
                            }

                            L.i(LOG_TAG + " streamType: " + streamType + ", streams.size: " + response.body().size());
                            callback.onStreamItemsLoaded(response.body());
                        } else {
                            L.e(LOG_TAG + " onResponse.code: " + response.code() + ",\nmessage: " + response.errorBody().toString());
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StreamItem>> call, Throwable t) {
                        L.e(LOG_TAG + " onFailure.t: " + t.getMessage());
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void getMainMovieList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        L.i(LOG_TAG + " getMainMovieList");
        L.i(LOG_TAG + " forceUpdate: " + forceUpdate + ", clearData: " + clearData);
        service.getMainContentMovieList()
                .enqueue(new Callback<List<StreamItem>>() {
                    @Override
                    public void onResponse(Call<List<StreamItem>> call, Response<List<StreamItem>> response) {
                        L.i(LOG_TAG + " onResponse");
                        if (response.code() == 200) {
                            L.i(LOG_TAG + " onResponse.code: 200");

                            int streamType = API.API_STREAM_TYPE.TYPE_MOVIE;
                            for (StreamItem item : response.body()) {
                                item.setType(streamType);
                            }

                            L.i(LOG_TAG + " streamType: " + streamType + ", streams.size: " + response.body().size());
                            callback.onStreamItemsLoaded(response.body());
                        } else {
                            L.e(LOG_TAG + " onResponse.code: " + response.code() + ",\nmessage: " + response.errorBody().toString());
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StreamItem>> call, Throwable t) {
                        L.e(LOG_TAG + " onFailure.t: " + t.getMessage());
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void getMainLiveList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        L.i(LOG_TAG + " getMainLiveList");
        L.i(LOG_TAG + " forceUpdate: " + forceUpdate + ", clearData: " + clearData);
        service.getMainContentLiveList()
                .enqueue(new Callback<List<StreamItem>>() {
                    @Override
                    public void onResponse(Call<List<StreamItem>> call, Response<List<StreamItem>> response) {
                        L.i(LOG_TAG + " onResponse");
                        if (response.code() == 200) {
                            L.i(LOG_TAG + " onResponse.code: 200");

                            int streamType = API.API_STREAM_TYPE.TYPE_LIVE;
                            for (StreamItem item : response.body()) {
                                item.setType(streamType);
                            }

                            L.i(LOG_TAG + " streamType: " + streamType + ", streams.size: " + response.body().size());
                            callback.onStreamItemsLoaded(response.body());
                        } else {
                            L.e(LOG_TAG + " onResponse.code: " + response.code() + ",\nmessage: " + response.errorBody().toString());
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StreamItem>> call, Throwable t) {
                        L.e(LOG_TAG + " onFailure.t: " + t.getMessage());
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void getMainMusicList(boolean forceUpdate, boolean clearData, @NonNull LoadMainContentCallback callback) {
        L.i(LOG_TAG + " getMainMusicList");
        L.i(LOG_TAG + " forceUpdate: " + forceUpdate + ", clearData: " + clearData);
        service.getMainContentMusicList()
                .enqueue(new Callback<List<StreamItem>>() {
                    @Override
                    public void onResponse(Call<List<StreamItem>> call, Response<List<StreamItem>> response) {
                        L.i(LOG_TAG + " onResponse");
                        if (response.code() == 200) {
                            L.i(LOG_TAG + " onResponse.code: 200");

                            int streamType = API.API_STREAM_TYPE.TYPE_MUSIC;
                            for (StreamItem item : response.body()) {
                                item.setType(streamType);
                            }

                            L.i(LOG_TAG + " streamType: " + streamType + ", streams.size: " + response.body().size());
                            callback.onStreamItemsLoaded(response.body());
                        } else {
                            L.e(LOG_TAG + " onResponse.code: " + response.code() + ",\nmessage: " + response.errorBody().toString());
                            callback.onDataNotAvailable();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StreamItem>> call, Throwable t) {
                        L.e(LOG_TAG + " onFailure.t: " + t.getMessage());
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void saveAll(@NonNull List<StreamItem> list) {

    }
}
