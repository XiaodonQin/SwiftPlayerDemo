package sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.mvp.BaseViewHolder;
import sg.com.conversant.swiftplayer.mvp.OnItemClickListener;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetailCluster;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/24.
 */

public class StreamDetailRecommendHolder extends RecyclerView.ViewHolder implements BaseViewHolder<StreamDetailCluster>{

    private StreamDetailRecommendHolder(View itemView) {
        super(itemView);

    }

    public static StreamDetailRecommendHolder newInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stream_detail_recommend, parent, false);
        return new StreamDetailRecommendHolder(view);
    }

    @Override
    public void onBindHolder(StreamDetailCluster item, int position, OnItemClickListener<StreamDetailCluster> listener) {

    }
}
