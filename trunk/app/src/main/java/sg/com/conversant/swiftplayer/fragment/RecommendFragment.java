package sg.com.conversant.swiftplayer.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.karim.MaterialTabs;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.activity.MainActivity;
import sg.com.conversant.swiftplayer.sample.SamplePagerAdapter;
import sg.com.conversant.swiftplayer.utils.L;

/**
 * Created by Xiaodong on 2017/7/27.
 */

public class RecommendFragment extends BaseFragment {
    private static final String LOG_TAG = RecommendFragment.class.getSimpleName();
    private View mRootView;

    @InjectView(R.id.material_tabs)
    MaterialTabs mMaterialTabs;

    @InjectView(R.id.go_channel)
    ImageView mGoChannel;

    @InjectView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.inject(this, mRootView);

        SamplePagerAdapter adapter = new SamplePagerAdapter(getChildFragmentManager(), 10);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(8);

        mMaterialTabs.setViewPager(mViewPager);
        mMaterialTabs.setOnTabSelectedListener(position -> L.i(LOG_TAG + " onTabSelected called with position " + position));

        mMaterialTabs.setOnTabReselectedListener(position -> L.i(LOG_TAG + " onTabReselected called with position " + position));

        mGoChannel.setOnClickListener(clickListener);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);

        return mRootView;
    }

    View.OnClickListener clickListener = v -> {
        switch (v.getId()) {
            case R.id.go_channel:
                if (mActivity instanceof MainActivity) {
                    ((MainActivity) mActivity).setBottomBarMenuSelected(1);
                }
                break;
        }

    };
}
