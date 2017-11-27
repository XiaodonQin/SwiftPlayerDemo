/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/3/20.
 */
package sg.com.conversant.swiftplayer.sdk;

/**
 * TODO
 *
 * @author Xiaodong

 */
public interface API {

    interface TAG {
        int TAG_RECOMMEND = 0;
        int TAG_SERIES = 1;
        int TAG_MOVIE = 2;
        int TAG_LIVE = 3;
        int TAG_MUSIC = 4;
    }

    interface USER {
        String USER_NAME = "user_name";
        String USER_TOKEN = "user_token";
        String USER_AVATAR = "user_avatar";
        String USER_TYPE = "user_type";
    }

    interface CONFIG {
        String SERVER_URL = "server_url";
    }

    String GET_POSTER_LIST = "test/poster_list.json";
    String GET_RECOMMEND_LIST = "test/recommend_list.json";
    String GET_SERIES_LIST = "test/series_list.json";
    String GET_MOVIE_LIST = "test/movie_list.json";
    String GET_LIVE_LIST = "test/live_list.json";
    String GET_MUSIC_LIST = "test/music_list.json";
    String GET_COMMENT_LIST = "test/comments_list.json";
    String GET_MY_PROFILE = "test/user/my_profile.json";

    String GET_CATEGORY_RECOMMEND_LIST = "test/category/recommend_list.json";

    String GET_MOVIES_TOP250 = "v2/movie/top250";

    interface QUERY {
        String START = "start";
        String COUNT = "count";
    }

    interface API_ZHIHU {
        String GET_ZHIHU_DAILY_NEWS_LIST = "before/{date}";
        String GET_ZHIHU_DAILY_NEWS_CONTENT = "{id}";
    }

    interface API_ZHIHU_PATH {
        String DATE = "date";
        String ID = "id";
    }

    interface PARAMS_ZHIHU {
        String INTENT_NEWS_ID = "news_id";
    }

    interface API_MAIN_CONTENT {
        String GET_MAIN_POSTER_LIST = "test/item/poster_list.json";
        String GET_MAIN_RECOMMEND_LIST = "test/item/recommend_list.json";
        String GET_MAIN_SERIES_LIST = "test/item/series_list.json";
        String GET_MAIN_MOVIE_LIST = "test/item/movie_list.json";
        String GET_MAIN_LIVE_LIST = "test/item/live_list.json";
        String GET_MAIN_MUSIC_LIST = "test/item/music_list.json";
        String GET_STREAM_DETAIL = "test/detail/{id}.json";
    }

    interface API_MAIN_CONTENT_PATH {
        String ID = "id";
    }

    interface API_STREAM_TYPE {
        int TYPE_POSTER = 1;
        int TYPE_RECOMMEND = 2;
        int TYPE_SERIES = 3;
        int TYPE_MOVIE = 4;
        int TYPE_LIVE = 5;
        int TYPE_MUSIC = 6;
    }

    interface BUNDLE_PARAMS {
        String STREAM_ITEM = "stream_item";
        String STREAM_DETAIL = "stream_detail";
    }
}
