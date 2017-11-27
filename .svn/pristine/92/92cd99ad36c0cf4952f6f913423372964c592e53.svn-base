/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/7/28.
 */
package sg.com.conversant.swiftplayer.rxjava.http;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import sg.com.conversant.swiftplayer.rxjava.entity.HttpResult;
import sg.com.conversant.swiftplayer.rxjava.entity.Subject;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.sdk.Constants;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class HttpMethods {
    private static final int DEFAULT_TIMEOUT = 5;

    private MovieService movieService;

    private HttpMethods() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request()
//                                .newBuilder()
//                                .addHeader(..., ...)
//                                .build();
//                        return chain.proceed(request);
//                    }
//                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_DOUBAN_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        movieService = retrofit.create(MovieService.class);
    }

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public void getTopMovies(Subscriber<HttpResult<List<Subject>>> subscriber, int start, int count) {



    }


}
