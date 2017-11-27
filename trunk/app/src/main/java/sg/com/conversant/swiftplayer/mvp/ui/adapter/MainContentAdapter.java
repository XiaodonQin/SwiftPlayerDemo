package sg.com.conversant.swiftplayer.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.StreamItemCluster;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder.MainContentLiveHolder;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder.MainContentMovieHolder;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder.MainContentMusicHolder;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder.MainContentPagerPosterHolder;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder.MainContentRecommendHolder;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder.MainContentSeriesHolder;
import sg.com.conversant.swiftplayer.sdk.API;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/3.
 */

public class MainContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment mFragment;

    private List<StreamItemCluster> streamItemClusters;
    private static int TYPE_POSTER = 1;
    private static int TYPE_RECOMMEND = 2;
    private static int TYPE_SERIES = 3;
    private static int TYPE_MOVIE = 4;
    private static int TYPE_LIVE = 5;
    private static int TYPE_MUSIC = 6;

    public MainContentAdapter(Fragment fragment) {
        this.mFragment = fragment;
        streamItemClusters = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return streamItemClusters.get(position).getType();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_POSTER) {
            return MainContentPagerPosterHolder.newInstance(parent, this.mFragment);
        } else if (viewType == TYPE_RECOMMEND) {
            return MainContentRecommendHolder.newInstance(parent, this.mFragment);
        } else if (viewType == TYPE_SERIES) {
            return MainContentSeriesHolder.newInstance(parent, this.mFragment);
        } else if (viewType == TYPE_MOVIE) {
            return MainContentMovieHolder.newInstance(parent, this.mFragment);
        } else if (viewType == TYPE_LIVE) {
            return MainContentLiveHolder.newInstance(parent, this.mFragment);
        } else if (viewType == TYPE_MUSIC) {
            return MainContentMusicHolder.newInstance(parent, this.mFragment);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainContentPagerPosterHolder) {
            MainContentPagerPosterHolder posterHolder = (MainContentPagerPosterHolder) holder;
            posterHolder.onBindHolder(streamItemClusters.get(position), position, null);
        } else if (holder instanceof MainContentRecommendHolder) {
            MainContentRecommendHolder recommendHolder = (MainContentRecommendHolder) holder;
            recommendHolder.onBindHolder(streamItemClusters.get(position), position, null);
        } else if (holder instanceof MainContentSeriesHolder) {
            MainContentSeriesHolder seriesHolder = (MainContentSeriesHolder) holder;
            seriesHolder.onBindHolder(streamItemClusters.get(position), position, null);
        } else if (holder instanceof MainContentMovieHolder) {
            MainContentMovieHolder movieHolder = (MainContentMovieHolder) holder;
            movieHolder.onBindHolder(streamItemClusters.get(position), position, null);
        } else if (holder instanceof MainContentLiveHolder) {
            MainContentLiveHolder liveHolder = (MainContentLiveHolder) holder;
            liveHolder.onBindHolder(streamItemClusters.get(position), position, null);
        } else if (holder instanceof MainContentMusicHolder) {
            MainContentMusicHolder musicHolder = (MainContentMusicHolder) holder;
            musicHolder.onBindHolder(streamItemClusters.get(position), position, null);
        }
    }

    @Override
    public int getItemCount() {
        return streamItemClusters.size();
    }

    public void setMainContent(List<StreamItem> items, int type) {
        if (type == API.API_STREAM_TYPE.TYPE_POSTER) {
            streamItemClusters.clear();
            notifyDataSetChanged();
        }
        StreamItemCluster cluster = new StreamItemCluster();
        cluster.setType(type);
        cluster.setStreamItems(items);
        streamItemClusters.add(streamItemClusters.size(), cluster);

        notifyItemChanged(streamItemClusters.size() - 1);
    }
}
