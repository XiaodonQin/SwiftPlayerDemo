/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2016/7/22.
 */
package sg.com.conversant.swiftplayer.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.ui.fragment.SimpleImageFragmentFactory;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class MainPosterPagerAdapter extends FragmentStatePagerAdapter {
    private static String LOG_TAG = MainPosterPagerAdapter.class.getSimpleName();

    private int posterNum = 0;
    private List<StreamItem> posterList;

    public MainPosterPagerAdapter(FragmentManager fm){
        super(fm);
        posterList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int i) {
        if (posterList != null && posterList.size() > 0) {
            return SimpleImageFragmentFactory.newInstance(posterList.get(i));
        }
        return SimpleImageFragmentFactory.newInstance(R.mipmap.default_poster);
    }

    @Override
    public int getCount() {
        if (posterList != null && posterList.size() > 0) {
            return posterList.size();
        }
        return posterNum;
    }

    public void setItems(List<StreamItem> posters) {
        if (posters != null && posters.size() > 0) {
            posterList.clear();
            posterList.addAll(posters);

            notifyDataSetChanged();
        } else {
            posterList.clear();
            posterList.add(null);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
