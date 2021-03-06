package sg.com.conversant.swiftplayer;

import java.util.HashMap;

import sg.com.conversant.swiftplayer.fragment.CategoryFilterFragment;
import sg.com.conversant.swiftplayer.fragment.CategoryFragment;
import sg.com.conversant.swiftplayer.fragment.CategoryRecommendFragment;
import sg.com.conversant.swiftplayer.fragment.FindFragment;
import sg.com.conversant.swiftplayer.fragment.HomeFragment;
import sg.com.conversant.swiftplayer.fragment.MyFragment;
import sg.com.conversant.swiftplayer.fragment.RecommendFragment;
import sg.com.conversant.swiftplayer.mvp.ui.fragment.MainContentFragment;

/**
 * Created by Xiaodong on 2015/10/25.
 */
public class MainUIConfiguration {

    /**
     * find a detail fragment in this map by item_id
     */
    public static HashMap<Integer, String> mFragmentMap;

    static {
        mFragmentMap = new HashMap<>();

        //add main tab fragment
        addItem(R.id.navi_recommend, RecommendFragment.class.getName());
        addItem(R.id.navi_category, CategoryFragment.class.getName());
        addItem(R.id.navi_find, FindFragment.class.getName());
        addItem(R.id.navi_my, MyFragment.class.getName());

        addItem(R.id.tab_recommend, CategoryRecommendFragment.class.getName());
        addItem(R.id.tab_filter, CategoryFilterFragment.class.getName());
    }

    public static void addItem(int itemId, String fragmentName) {
        mFragmentMap.put(itemId, fragmentName);
    }

    public static String getFragmentName(int item_id) {
        return mFragmentMap.get(item_id);
    }
}
