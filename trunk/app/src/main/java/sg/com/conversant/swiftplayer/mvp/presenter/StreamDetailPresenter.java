package sg.com.conversant.swiftplayer.mvp.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.contact.StreamDetailContact;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.datasource.StreamDetailDataSource;
import sg.com.conversant.swiftplayer.mvp.data.repository.StreamDetailRepository;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/10.
 */

public class StreamDetailPresenter implements StreamDetailContact.Presenter {
    private static String LOG_TAG = StreamDetailPresenter.class.getSimpleName();

    @NonNull
    private StreamDetailContact.View mView;

    @NonNull
    private StreamDetailRepository mRepository;


    public StreamDetailPresenter(@NonNull StreamDetailContact.View view,
                                 @NonNull StreamDetailRepository repository) {
        this.mView = view;
        this.mRepository = repository;

        this.mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadStreamDetail(long id) {

        mRepository.getStreamDetail(id, new StreamDetailDataSource.LoadStreamDetailCallback() {
            @Override
            public void onStreamDetailLoaded(@NonNull StreamDetail detail) {
                if (mView.isActive()) {
                    mView.showStreamDetailResult(detail);
                    mView.showLoadingIndicator(false);
                }
            }

            @Override
            public void onDataNotAvailable() {
                mView.startLoadRecommend();
            }
        });
    }

    @Override
    public void loadStreamRecommends() {
        mRepository.getStreamDetailRecommends(new StreamDetailDataSource.LoadStreamDetailRecommendCallback() {
            @Override
            public void RecommendLoaded(@NonNull List<StreamItem> items) {
                if (mView.isActive()) {
                    mView.showStreamRecommendResult(items);
                }
            }

            @Override
            public void onDataNotAvailable() {
                mView.startLoadComments();
            }
        });

    }

    @Override
    public void loadStreamComments() {

    }
}
