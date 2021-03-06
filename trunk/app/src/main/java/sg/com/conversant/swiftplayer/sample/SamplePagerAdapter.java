/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/4/27.
 */
package sg.com.conversant.swiftplayer.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import sg.com.conversant.swiftplayer.fragment.HomeFragment;
import sg.com.conversant.swiftplayer.fragment.MoviesFragment;
import sg.com.conversant.swiftplayer.mvp.ui.fragment.MainContentFragment;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class SamplePagerAdapter extends FragmentPagerAdapter {

//        private final String[] TITLES = {"Item One", "Item Two", "Item Three", "Item Four", "Item Five", "Item Six", "Item Seven", "Item Eight",
//                "Item Nine", "Item Ten", "Item Eleven"};

    private final String[] TITLES = {"首页", "电影", "电视剧", "综艺", "音乐", "体育", "纪录片",
            "游戏", "动漫", "资讯"};

    private final ArrayList<String> mTitles;

    public SamplePagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        mTitles = new ArrayList<>();
        for (int i = 0; i < numberOfTabs; i++) {
            mTitles.add(TITLES[i]);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MainContentFragment();
        } else if (position == 1) {
            return new MoviesFragment();
        }
        return SampleFragment.newInstance(position);
    }
}
