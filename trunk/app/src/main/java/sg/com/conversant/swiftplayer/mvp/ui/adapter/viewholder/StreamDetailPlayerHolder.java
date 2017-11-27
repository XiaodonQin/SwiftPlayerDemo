package sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.mvp.BaseViewHolder;
import sg.com.conversant.swiftplayer.mvp.OnItemClickListener;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetailCluster;
import sg.com.conversant.swiftplayer.mvp.ui.fragment.StreamDetailPlayerFragment;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/12.
 */

public class StreamDetailPlayerHolder extends RecyclerView.ViewHolder implements BaseViewHolder<StreamDetailCluster> {
    private static String LOG_TAG = StreamDetailPlayerHolder.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private FrameLayout.LayoutParams mVideoParams;
    private Fragment mStreamPlayerFragment;
    private FrameLayout mPreviewLayout;
    private ImageView mCover;
    private TextView mLoadingText;

    private StreamDetailPlayerHolder(View itemView, FragmentManager fragmentManager,
                                     FrameLayout.LayoutParams params) {
        super(itemView);
        this.mFragmentManager = fragmentManager;
        this.mVideoParams = params;
        mPreviewLayout = itemView.findViewById(R.id.preview);
        mCover = itemView.findViewById(R.id.cover);
        mLoadingText = itemView.findViewById(R.id.loading_tv);

        mPreviewLayout.setLayoutParams(mVideoParams);
        mCover.setLayoutParams(mVideoParams);
    }

    public static StreamDetailPlayerHolder newInstance(ViewGroup parent, FragmentManager fragmentManager,
                                                       FrameLayout.LayoutParams params) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stream_detail_player, parent, false);
        return new StreamDetailPlayerHolder(view, fragmentManager, params);
    }

    @Override
    public void onBindHolder(StreamDetailCluster item, int position, OnItemClickListener<StreamDetailCluster> listener) {
        if (item.getStreamDetail() == null && item.getStreamItem() != null) {
            mPreviewLayout.setVisibility(View.GONE);
            mCover.setVisibility(View.VISIBLE);
            mLoadingText.setVisibility(View.VISIBLE);
            Utils.showPosterWithGlide(item.getStreamItem().getThumbnailPath(), mCover);
        } else {
            mPreviewLayout.setVisibility(View.VISIBLE);
            mCover.setVisibility(View.GONE);
            mLoadingText.setVisibility(View.GONE);

            addFragment(item.getStreamDetail());
        }
    }

    public void addFragment(StreamDetail item) {
        L.i(LOG_TAG + " addFragment");
        mStreamPlayerFragment = StreamDetailPlayerFragment.newInstance();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (mStreamPlayerFragment == null) {
            mStreamPlayerFragment = StreamDetailPlayerFragment.newInstance();
        }
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(API.BUNDLE_PARAMS.STREAM_DETAIL, item);
        mStreamPlayerFragment.setArguments(mBundle);
        if (mStreamPlayerFragment.isAdded()) {
            fragmentTransaction.show(mStreamPlayerFragment).commit();
        } else {
            fragmentTransaction.replace(R.id.preview, mStreamPlayerFragment, StreamDetailPlayerFragment.class.getSimpleName());
            fragmentTransaction.commit();
        }
    }
}
