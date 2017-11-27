/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2016/4/26.
 */
package sg.com.conversant.swiftplayer.sample;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.karim.MaterialTabs;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.utils.L;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class TestActivity extends AppCompatActivity {
    private static String LOG_TAG = "TestActivity";

    @InjectView(R.id.material_tabs)
    MaterialTabs mMaterialTabs;

    @InjectView(R.id.view_pager)
    ViewPager mViewPager;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

//        Fragment fragment = new SmartTabFragment();
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.test_container, fragment, getString(R.string.test_title))
//                .commit();

        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);

        // Setting navigation icon
        mToolbar.setNavigationIcon(getDrawable(this, R.drawable.md_nav_back));

        // Setting this navigation icon's onClickListener
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SamplePagerAdapter adapter = new SamplePagerAdapter(getSupportFragmentManager(), 10);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(8);

        mMaterialTabs.setViewPager(mViewPager);
        mMaterialTabs.setOnTabSelectedListener(new MaterialTabs.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                L.i(LOG_TAG + " onTabSelected called with position " + position);
            }
        });

        mMaterialTabs.setOnTabReselectedListener(new MaterialTabs.OnTabReselectedListener() {
            @Override
            public void onTabReselected(int position) {
                L.i(LOG_TAG + " onTabReselected called with position " + position);
            }
        });

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);
    }

    /**
     * Convenience to use the new getDrawable(...) on Lollipop and the deprecated one on preLollipop.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable getDrawable(Context context, @DrawableRes int drawableResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(drawableResId);
        } else {
            return context.getResources().getDrawable(drawableResId);
        }
    }
}
