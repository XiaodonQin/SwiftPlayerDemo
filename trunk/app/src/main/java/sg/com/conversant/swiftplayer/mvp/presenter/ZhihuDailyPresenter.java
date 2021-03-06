package sg.com.conversant.swiftplayer.mvp.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.contact.ZhihuContact;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsDetail;
import sg.com.conversant.swiftplayer.mvp.data.repository.ZhihuDailyNewsRepository;
import sg.com.conversant.swiftplayer.mvp.data.datasource.ZhihuDailyNewsDataSource;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/13.
 */

public class ZhihuDailyPresenter implements ZhihuContact.Presenter {
    private static String LOG_TAG = ZhihuDailyPresenter.class.getSimpleName();

    @NonNull
    private ZhihuContact.View mView;

    @NonNull
    private ZhihuDailyNewsRepository mRepository;

    public ZhihuDailyPresenter(@NonNull ZhihuContact.View view,
                               @NonNull ZhihuDailyNewsRepository repository) {

        this.mView = view;
        this.mRepository = repository;

        this.mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadData(boolean forceUpdate, boolean clearCache, long date) {
        if (forceUpdate) {
            mView.setLoadingIndicator(true);
        }

        mView.setResponseOK(false);

        mRepository.getZhihuDailyNews(forceUpdate, clearCache, date, new ZhihuDailyNewsDataSource.LoadZhihuDailyNewCallback() {
            @Override
            public void onNewsLoaded(@NonNull List<ZhihuDailyNews> list) {
                if (mView.isActive()) {
                    mView.setResponseOK(true);
                    mView.showResult(list);
                    mView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.setResponseOK(true);
                    mView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onDetailLoaded(@NonNull ZhihuDailyNewsDetail detail) {

            }
        });
    }

    @Override
    public void loadDetail(long id) {
        mView.setLoadingIndicator(true);

        mView.setResponseOK(false);

        mRepository.getZhihuDailyNewsDetail(id, new ZhihuDailyNewsDataSource.LoadZhihuDailyNewCallback() {
            @Override
            public void onNewsLoaded(@NonNull List<ZhihuDailyNews> list) {

            }

            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.setResponseOK(true);
                    mView.setLoadingIndicator(false);
                }
            }

            @Override
            public void onDetailLoaded(@NonNull ZhihuDailyNewsDetail detail) {
                if (mView.isActive()) {
                    mView.setResponseOK(true);
                    mView.showResult(detail);
                    mView.setLoadingIndicator(false);
                }
            }
        });
    }
}
