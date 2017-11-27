package sg.com.conversant.swiftplayer.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.fragment.BaseFragment;
import sg.com.conversant.swiftplayer.mvp.contact.MainContentContact;
import sg.com.conversant.swiftplayer.mvp.data.repository.MainContentRepository;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.local.MainContentLocalDataSource;
import sg.com.conversant.swiftplayer.mvp.data.remote.MainContentRemoteDataSource;
import sg.com.conversant.swiftplayer.mvp.presenter.MainContentPresenter;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.MainContentAdapter;
import sg.com.conversant.swiftplayer.sdk.API;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/1.
 */

public class MainContentFragment extends BaseFragment implements MainContentContact.View,
        SwipeRefreshLayout.OnRefreshListener {
    private static String LOG_TAG = MainContentFragment.class.getSimpleName();

    private View mRootView;
    private MainContentContact.Presenter mPresenter;
    private LinearLayoutManager manager;
    private boolean mActive = false;
    private boolean isFirstLoad = true;

    private MainContentAdapter contentAdapter;

    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main_content, container, false);
        ButterKnife.inject(this, mRootView);
        initViews();

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(new MainContentPresenter(MainContentFragment.this,
                MainContentRepository.newInstance(
                        MainContentRemoteDataSource.newInstance(),
                        MainContentLocalDataSource.newInstance(getContext())
                )));

        mActive = true;
        mPresenter.loadPosterList(true, false);
    }

    @Override
    public void setPresenter(MainContentContact.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void initViews() {
        swipeRefreshLayout.setOnRefreshListener(this);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        contentAdapter = new MainContentAdapter(MainContentFragment.this);
        recyclerView.setAdapter(contentAdapter);
    }

    @Override
    public boolean isActive() {
        return mActive;
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        mActivity.runOnUiThread(() -> {
            if (active) {
                if (!swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(true);
                }
             } else {
                swipeRefreshLayout.setRefreshing(false);
            }

        });
    }

    @Override
    public void showPosterListResult(@NonNull List<StreamItem> results) {
        contentAdapter.setMainContent(results, API.API_STREAM_TYPE.TYPE_POSTER);
        mPresenter.loadRecommendList(true, false);
    }

    @Override
    public void showStaticPoster() {
        contentAdapter.setMainContent(null, API.API_STREAM_TYPE.TYPE_POSTER);
        mPresenter.loadRecommendList(true, false);
    }

    @Override
    public void showRecommendListResult(@NonNull List<StreamItem> results) {
        contentAdapter.setMainContent(results, API.API_STREAM_TYPE.TYPE_RECOMMEND);
        mPresenter.loadSeriesList(true, false);
    }

    @Override
    public void skipShowRecommend() {
        mPresenter.loadSeriesList(true, false);
    }

    @Override
    public void showSeriesListResult(@NonNull List<StreamItem> results) {
        contentAdapter.setMainContent(results, API.API_STREAM_TYPE.TYPE_SERIES);
        mPresenter.loadMovieList(true, false);
    }

    @Override
    public void skipShowSeries() {
        mPresenter.loadMovieList(true, false);
    }

    @Override
    public void showMovieListResult(@NonNull List<StreamItem> results) {
        contentAdapter.setMainContent(results, API.API_STREAM_TYPE.TYPE_MOVIE);
        mPresenter.loadLiveList(true, false);
    }

    @Override
    public void skipShowMovie() {
        mPresenter.loadLiveList(true, false);
    }

    @Override
    public void showLiveListResult(@NonNull List<StreamItem> results) {
        contentAdapter.setMainContent(results, API.API_STREAM_TYPE.TYPE_LIVE);
        mPresenter.loadMusicList(true, false);
    }

    @Override
    public void skipShowLive() {
        mPresenter.loadMusicList(true, false);
    }

    @Override
    public void showMusicListResult(@NonNull List<StreamItem> results) {
        contentAdapter.setMainContent(results, API.API_STREAM_TYPE.TYPE_MUSIC);
    }

    @Override
    public void skipShowMusic() {

    }

    @Override
    public void setResponseOK(boolean responseOK) {

    }

    @Override
    public void onRefresh() {
        mPresenter.loadPosterList(true, false);
    }
}
