/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2016/5/3.
 */
package sg.com.conversant.swiftplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.adapter.CategoryPagerAdapter;
import sg.com.conversant.swiftplayer.sdk.module.ChannelItem;

/**
 * TODO
 *
 * @author Xiaodong
 */
public class CategoryDetailActivity extends AppCompatActivity {

    public static String ITEM_CATEGORY = "item_category";

    private ChannelItem channelItem;

    @InjectView(R.id.tab_recommend)
    TextView mTabRecommendView;

    @InjectView(R.id.tab_filter)
    TextView mTabFilterView;

    @InjectView(R.id.title_left)
    TextView titleLeftView;

    @InjectView(R.id.title_right)
    TextView titleRightView;

    @InjectView(R.id.view_pager)
    ViewPager mViewPager;

    @InjectView(R.id.search_btn)
    ImageView mSearchBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        ButterKnife.inject(this);

        getIntentExtras();

        if (channelItem == null) {
            return;
        }

        setupUI();
    }

    private void getIntentExtras() {
        Intent mIntent = getIntent();
        channelItem = (ChannelItem) mIntent.getSerializableExtra(ITEM_CATEGORY);
    }

    private void setupUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setContentInsetsAbsolute(0, 0);

        toolbar.setNavigationOnClickListener(clickListener);

        titleLeftView.setText(channelItem.getChannelName());
        titleRightView.setText(channelItem.getChannelName());

        showRecommend(true);

        mTabRecommendView.setOnClickListener(mClickListener);
        mTabFilterView.setOnClickListener(mClickListener);
        mSearchBtn.setOnClickListener(mClickListener);

        CategoryPagerAdapter adapter = new CategoryPagerAdapter(getSupportFragmentManager(), 2);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.addOnPageChangeListener(pageChangeListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tab_recommend:
                    showRecommend(true);

                    mViewPager.setCurrentItem(0, true);

                    break;

                case R.id.tab_filter:
                    showRecommend(false);

                    mViewPager.setCurrentItem(1, true);

                    break;

                case R.id.search_btn:
                    Intent intent = new Intent(CategoryDetailActivity.this, SearchActivity.class);
                    startActivity(intent);

                    break;
            }
        }
    };

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                showRecommend(true);
            } else if (position == 1) {
                showRecommend(false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void showRecommend(boolean show) {
        if (show) {
            mTabRecommendView.setSelected(true);
            mTabFilterView.setSelected(false);
            mTabRecommendView.setTextColor(getResources().getColor(R.color.colorPrimary));
            mTabFilterView.setTextColor(getResources().getColor(R.color.white));
        } else {
            mTabRecommendView.setSelected(false);
            mTabFilterView.setSelected(true);
            mTabRecommendView.setTextColor(getResources().getColor(R.color.white));
            mTabFilterView.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}
