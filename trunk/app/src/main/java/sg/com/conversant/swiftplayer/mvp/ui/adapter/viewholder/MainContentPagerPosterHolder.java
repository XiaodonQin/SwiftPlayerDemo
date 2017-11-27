package sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder;

import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.mvp.BaseViewHolder;
import sg.com.conversant.swiftplayer.mvp.OnItemClickListener;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.StreamItemCluster;
import sg.com.conversant.swiftplayer.mvp.ui.activity.NewsActivity;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.MainPosterPagerAdapter;
import sg.com.conversant.swiftplayer.views.CirclePageIndicator;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/3.
 */

public class MainContentPagerPosterHolder extends RecyclerView.ViewHolder implements BaseViewHolder<StreamItemCluster> {
    private Fragment mFragment;

    private ViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    private LinearLayout newsCategoryView;

    private MainPosterPagerAdapter pagerAdapter;

    private MainContentPagerPosterHolder(View itemView, Fragment fragment) {
        super(itemView);
        this.mFragment = fragment;
        mViewPager = itemView.findViewById(R.id.viewpager);
        mIndicator = itemView.findViewById(R.id.viewpager_indicator);
        newsCategoryView = itemView.findViewById(R.id.category_news_layout);

        WindowManager wm = mFragment.getActivity().getWindowManager();
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);

        int cardMargin = mFragment.getActivity().getResources().getDimensionPixelSize(R.dimen.default_padding);
        int coverWidth = outSize.x - 2 * cardMargin;
        int coverHeight = coverWidth * 9 / 16;

        CardView.LayoutParams layoutParams = new CardView.LayoutParams(coverWidth, coverHeight);
        mViewPager.setLayoutParams(layoutParams);

        pagerAdapter = new MainPosterPagerAdapter(mFragment.getChildFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
        mIndicator.setViewPager(mViewPager);
    }


    public static MainContentPagerPosterHolder newInstance(ViewGroup parent, Fragment fragment) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_content_poster, parent,
                false);
        return new MainContentPagerPosterHolder(view, fragment);
    }

    @Override
    public void onBindHolder(StreamItemCluster item, int position, OnItemClickListener<StreamItemCluster> listener) {
        List<StreamItem> streamItems = item.getStreamItems();
        if (streamItems == null || streamItems.size() == 0) {
            mIndicator.setVisibility(View.GONE);
            pagerAdapter.setItems(null);
        } else {
            mIndicator.setVisibility(View.VISIBLE);
            pagerAdapter.setItems(streamItems);
        }

        newsCategoryView.setOnClickListener(v -> {
            Intent mIntent = new Intent(mFragment.getContext(), NewsActivity.class);
            mFragment.getContext().startActivity(mIntent);
        });
    }
}
