/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2016/5/3.
 */
package sg.com.conversant.swiftplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import sg.com.conversant.swiftplayer.fragment.CategoryFilterFragment;
import sg.com.conversant.swiftplayer.fragment.CategoryRecommendFragment;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class CategoryPagerAdapter extends FragmentPagerAdapter {

    private int numTabs;

    public CategoryPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);

        this.numTabs = numberOfTabs;
    }

    @Override
    public int getCount() {
        return numTabs;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new CategoryRecommendFragment();
        } else if (position == 1) {
            return new CategoryFilterFragment();
        }
        return null;
    }
}
