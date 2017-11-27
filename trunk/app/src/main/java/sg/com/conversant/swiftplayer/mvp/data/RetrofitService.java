package sg.com.conversant.swiftplayer.mvp.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import sg.com.conversant.swiftplayer.sdk.API;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/13.
 */

public interface RetrofitService {

    interface ZhihuDailyService {

        @GET(API.API_ZHIHU.GET_ZHIHU_DAILY_NEWS_LIST)
        Call<ZhihuDailyNewsCluster> getZhihuDailyNewsList(@Path(API.API_ZHIHU_PATH.DATE) String date);

        @GET(API.API_ZHIHU.GET_ZHIHU_DAILY_NEWS_CONTENT)
        Call<ZhihuDailyNewsDetail> getZhihuDailyNewsDetail(@Path(API.API_ZHIHU_PATH.ID) long id);
    }

    interface MainContentService {

        @GET(API.API_MAIN_CONTENT.GET_MAIN_POSTER_LIST)
        Call<List<StreamItem>> getMainContentPosterList();

        @GET(API.API_MAIN_CONTENT.GET_MAIN_RECOMMEND_LIST)
        Call<List<StreamItem>> getMainContentRecommendList();

        @GET(API.API_MAIN_CONTENT.GET_MAIN_SERIES_LIST)
        Call<List<StreamItem>> getMainContentSeriesList();

        @GET(API.API_MAIN_CONTENT.GET_MAIN_MOVIE_LIST)
        Call<List<StreamItem>> getMainContentMovieList();

        @GET(API.API_MAIN_CONTENT.GET_MAIN_LIVE_LIST)
        Call<List<StreamItem>> getMainContentLiveList();

        @GET(API.API_MAIN_CONTENT.GET_MAIN_MUSIC_LIST)
        Call<List<StreamItem>> getMainContentMusicList();

        @GET(API.API_MAIN_CONTENT.GET_STREAM_DETAIL)
        Call<StreamDetail> getStreamDetail(@Path(API.API_MAIN_CONTENT_PATH.ID) long id);
    }
}
