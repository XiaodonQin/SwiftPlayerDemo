/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2016/4/14.
 */
package sg.com.conversant.swiftplayer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.adapter.ChannelAdapter;
import sg.com.conversant.swiftplayer.sdk.module.ChannelItem;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class CategoryFragment extends BaseFragment {
    private static final String LOG_TAG = "HomeFragment";
    private View mRootView;

    private RecyclerView mRecyclerView;

    private ChannelAdapter mChannelAdapter;

    private List<ChannelItem> channelItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initChannelList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_category, container, false);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mChannelAdapter = new ChannelAdapter(mActivity);
        mRecyclerView.setAdapter(mChannelAdapter);

        mChannelAdapter.setChannelItems(channelItems);

        return mRootView;
    }

    private void initChannelList() {
        channelItems = new ArrayList<>();

//        addChannelItem(R.string.tab_home, R.mipmap.ic_home);
        addChannelItem(R.string.tab_series, R.mipmap.ic_series);
        addChannelItem(R.string.tab_movie, R.mipmap.ic_movie);
        addChannelItem(R.string.tab_variety, R.mipmap.ic_variety);
        addChannelItem(R.string.tab_music, R.mipmap.ic_music);
        addChannelItem(R.string.tab_physical, R.mipmap.ic_physical);
        addChannelItem(R.string.tab_bbc, R.mipmap.ic_bbc);
        addChannelItem(R.string.tab_games, R.mipmap.ic_games);
        addChannelItem(R.string.tab_live, R.mipmap.ic_live);
        addChannelItem(R.string.tab_cartoon, R.mipmap.ic_cartoon);
        addChannelItem(R.string.tab_news, R.mipmap.ic_news);
        addChannelItem(R.string.tab_car, R.mipmap.ic_car);
        addChannelItem(R.string.tab_life, R.mipmap.ic_life);
        addChannelItem(R.string.tab_baby, R.mipmap.ic_baby);
        addChannelItem(R.string.tab_girl, R.mipmap.ic_girl);
        addChannelItem(R.string.tab_stock, R.mipmap.ic_stock);
        addChannelItem(R.string.tab_love, R.mipmap.ic_love);
    }

    private void addChannelItem(String name, int id) {
        ChannelItem item = new ChannelItem();
        item.setChannelName(name);
        item.setChannelIcon(id);

        if (channelItems != null) {
            channelItems.add(item);
        }
    }

    private void addChannelItem(int name, int id) {
        ChannelItem item = new ChannelItem();
        item.setChannelName(getResources().getString(name));
        item.setChannelIcon(id);

        if (channelItems != null) {
            channelItems.add(item);
        }
    }
}
