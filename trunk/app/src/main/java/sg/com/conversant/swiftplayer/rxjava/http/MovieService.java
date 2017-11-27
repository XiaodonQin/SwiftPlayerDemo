package sg.com.conversant.swiftplayer.rxjava.http;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import sg.com.conversant.swiftplayer.rxjava.entity.HttpResult;
import sg.com.conversant.swiftplayer.rxjava.entity.Subject;
import sg.com.conversant.swiftplayer.sdk.API;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/7/28.
 */

public interface MovieService {

    @GET(API.GET_MOVIES_TOP250)
    Observable<HttpResult<List<Subject>>> getMoviesTop250(@Query(API.QUERY.START) int start, @Query(API.QUERY.COUNT) int count);

}
