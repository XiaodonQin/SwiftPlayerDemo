package sg.com.conversant.swiftplayer.mvp.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sg.com.conversant.swiftplayer.mvp.data.StreamComment;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetailCluster;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder.StreamDetailInfoHolder;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder.StreamDetailPlayerHolder;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/12.
 */

public class StreamDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int TYPE_STREAM_PLAYER = 0;
    private static int TYPE_STREAM_INFO = 1;
    private static int TYPE_STREAM_RECOMMEND_TITLE = 2;
    private static int TYPE_STREAM_RECOMMEND = 3;
    private static int TYPE_STREAM_COMMENT_EDIT = 4;
    private static int TYPE_STREAM_COMMENT = 5;

    private List<StreamDetailCluster> mClusters;

    private FragmentManager mFragmentManager;
    private FrameLayout.LayoutParams mVideoParams;

    public StreamDetailAdapter(FragmentManager fragmentManager, FrameLayout.LayoutParams params) {
        this.mFragmentManager = fragmentManager;
        this.mVideoParams = params;
        mClusters = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return mClusters.get(position).getType();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_STREAM_PLAYER) {
            return StreamDetailPlayerHolder.newInstance(parent, mFragmentManager, mVideoParams);
        } else if (viewType == TYPE_STREAM_INFO) {
            return StreamDetailInfoHolder.newInstance(parent);
        } else if (viewType == TYPE_STREAM_RECOMMEND_TITLE) {

        } else if (viewType == TYPE_STREAM_RECOMMEND) {

        } else if (viewType == TYPE_STREAM_COMMENT_EDIT) {

        } else if (viewType == TYPE_STREAM_COMMENT) {

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StreamDetailPlayerHolder) {
            StreamDetailPlayerHolder playerHolder = (StreamDetailPlayerHolder) holder;
            if (mClusters.get(position) != null) {
                playerHolder.onBindHolder(mClusters.get(position), position, null);
            }
        } else if (holder instanceof StreamDetailInfoHolder) {
            StreamDetailInfoHolder infoHolder = (StreamDetailInfoHolder) holder;
            if (mClusters.get(position) != null &&
                    mClusters.get(position).getStreamDetail() != null) {
                infoHolder.onBindHolder(mClusters.get(position).getStreamDetail(), position, null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mClusters.size();
    }

    public void setStreamPreview(StreamItem item) {
        StreamDetailCluster cluster = new StreamDetailCluster();
        cluster.setType(TYPE_STREAM_PLAYER);
        cluster.setStreamItem(item);

        mClusters.add(0, cluster);
        notifyItemChanged(0);
    }

    public void setStreamDetail(StreamDetail detail) {
        StreamDetailCluster cluster = new StreamDetailCluster();
        cluster.setType(TYPE_STREAM_PLAYER);
        cluster.setStreamDetail(detail);

        if (mClusters.get(0) != null) {
            mClusters.set(0, cluster);
        } else {
            mClusters.add(0, cluster);
        }
        notifyItemChanged(0);
    }

    public void setStreamDetailInfo(StreamDetail detail) {
        StreamDetailCluster cluster = new StreamDetailCluster();
        cluster.setType(TYPE_STREAM_INFO);
        cluster.setStreamDetail(detail);

        mClusters.add(cluster);
        notifyItemChanged(mClusters.size() - 1);
    }

    public void setRecommendTitle() {
        StreamDetailCluster cluster = new StreamDetailCluster();
        cluster.setType(TYPE_STREAM_RECOMMEND_TITLE);

        mClusters.add(cluster);
        notifyItemChanged(mClusters.size() - 1);
    }

    public void setRecommends(List<StreamItem> items) {
        for (StreamItem item :items) {
            StreamDetailCluster cluster = new StreamDetailCluster();
            cluster.setType(TYPE_STREAM_RECOMMEND);
            cluster.setStreamItem(item);

            mClusters.add(cluster);
        }
        notifyItemRangeInserted(mClusters.size() - items.size(), items.size());
    }

    public void setCommentEdit() {
        StreamDetailCluster cluster = new StreamDetailCluster();
        cluster.setType(TYPE_STREAM_COMMENT_EDIT);

        mClusters.add(cluster);
        notifyItemChanged(mClusters.size() - 1);
    }

    public void setStreamComments(List<StreamComment> comments) {
        for (StreamComment comment : comments) {
            StreamDetailCluster cluster = new StreamDetailCluster();
            cluster.setType(TYPE_STREAM_COMMENT);
            cluster.setStreamComment(comment);

            mClusters.add(cluster);
        }

        notifyItemRangeInserted(mClusters.size() - comments.size(), comments.size());
    }

    public void addStreamComment(StreamComment comment) {
        StreamDetailCluster cluster = new StreamDetailCluster();
        cluster.setType(TYPE_STREAM_COMMENT);
        cluster.setStreamComment(comment);

        mClusters.add(cluster);
        notifyItemChanged(mClusters.size() - 1);
    }

}
