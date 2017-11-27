package sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.mvp.BaseViewHolder;
import sg.com.conversant.swiftplayer.mvp.OnItemClickListener;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/12.
 */

public class StreamDetailInfoHolder extends RecyclerView.ViewHolder implements BaseViewHolder<StreamDetail> {
    private static String LOG_TAG = StreamDetailInfoHolder.class.getSimpleName();

    private LinearLayout mStreamInfoButton;
    private TextView mStreamTitle;
    private TextView mStreamDescriptionView;

    private StreamDetailInfoHolder(View itemView) {
        super(itemView);
        mStreamInfoButton = itemView.findViewById(R.id.info_title_layout);
        mStreamTitle = itemView.findViewById(R.id.name_media);
        mStreamDescriptionView = itemView.findViewById(R.id.description_view);
    }

    public static StreamDetailInfoHolder newInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stream_detail_info, parent, false);
        return new StreamDetailInfoHolder(view);
    }

    @Override
    public void onBindHolder(StreamDetail item, int position, OnItemClickListener<StreamDetail> listener) {

        mStreamTitle.setText(item.getName());
        mStreamDescriptionView.setText(item.getDescription());
        mStreamInfoButton.setOnClickListener(v -> {
            if (mStreamDescriptionView.getVisibility() == View.VISIBLE) {
                mStreamDescriptionView.setVisibility(View.GONE);
            } else {
                mStreamDescriptionView.setVisibility(View.VISIBLE);
            }
        });
    }
}
