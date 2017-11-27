package sg.com.conversant.swiftplayer.mvp.ui.activity;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.mvp.contact.StreamDetailContact;
import sg.com.conversant.swiftplayer.mvp.data.StreamComment;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetailCluster;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.local.StreamDetailLocalDataSource;
import sg.com.conversant.swiftplayer.mvp.data.remote.StreamDetailRemoteDataSource;
import sg.com.conversant.swiftplayer.mvp.data.repository.StreamDetailRepository;
import sg.com.conversant.swiftplayer.mvp.presenter.StreamDetailPresenter;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.StreamDetailAdapter;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/9.
 */

public class StreamDetailActivity extends AppCompatActivity implements StreamDetailContact.View {
    private static String LOG_TAG = StreamDetailActivity.class.getSimpleName();

    private StreamDetailContact.Presenter mPresenter;
    private boolean isFirstLoad = true;
    private StreamItem mStreamItem;
    private StreamDetail mStreamDetail;
    private StreamDetailCluster mStreamDetailCluster;
    private StreamDetailAdapter mStreamDetailAdapter;

    @InjectView(R.id.toolbar_transparent)
    Toolbar mToolbar;

    @InjectView(R.id.progress)
    ContentLoadingProgressBar mProgressBar;

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @InjectView(R.id.cover_view)
    ImageView mCoverImage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_detail);
        ButterKnife.inject(this);

        initData();
        initViews();
        setPresenter(new StreamDetailPresenter(this, StreamDetailRepository.newInstance(
                StreamDetailRemoteDataSource.newInstance(),
                StreamDetailLocalDataSource.newInstance(this)
        )));
    }

    @Override
    public void setPresenter(StreamDetailContact.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void initViews() {
        mToolbar.getBackground().setAlpha(0);
        setSupportActionBar(mToolbar);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.setNavigationOnClickListener(v -> finish());

        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            //设为 false
//           actionBar.setDisplayShowTitleEnabled(false);  //是否隐藏标题
            actionBar.setDisplayHomeAsUpEnabled(true);     //是否显示返回按钮
        }

        //实现透明状态栏效果  并且toolbar 需要设置  android:fitsSystemWindows="true"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags);
        }

        Utils.showPosterWithGlide(mStreamItem.getThumbnailPath(), mCoverImage);
        mCoverImage.setVisibility(View.GONE);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(onScrollListener);

        WindowManager wm = getWindowManager();
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);
        int width = outSize.x;
        int height = width * 9/16;
        FrameLayout.LayoutParams videoParams = new FrameLayout.LayoutParams(width, height);

        mStreamDetailAdapter = new StreamDetailAdapter(getSupportFragmentManager(), videoParams);
        mRecyclerView.setAdapter(mStreamDetailAdapter);
        mStreamDetailAdapter.setStreamPreview(mStreamItem);
    }

    private void initData() {
        mStreamItem = (StreamItem) getIntent().getSerializableExtra(API.BUNDLE_PARAMS.STREAM_ITEM);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        runOnUiThread(() -> {
            if (active) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void showStreamDetailResult(@NonNull StreamDetail detail) {
        L.i(LOG_TAG + " showStreamDetailResult detail: " + detail.getName());

        mStreamDetailAdapter.setStreamDetail(detail);
        mStreamDetailAdapter.setStreamDetailInfo(detail);
        mPresenter.loadStreamRecommends();
    }

    @Override
    public void startLoadRecommend() {
        mPresenter.loadStreamRecommends();
    }

    @Override
    public void showStreamRecommendResult(@NonNull List<StreamItem> recommends) {
        mStreamDetailAdapter.setRecommendTitle();
        mStreamDetailAdapter.setRecommends(recommends);
        mPresenter.loadStreamComments();
    }

    @Override
    public void startLoadComments() {
        mPresenter.loadStreamComments();
    }

    @Override
    public void showStreamCommentsResult(@NonNull List<StreamComment> comments) {
        mStreamDetailAdapter.setCommentEdit();
        mStreamDetailAdapter.setStreamComments(comments);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isFirstLoad) {
            mPresenter.loadStreamDetail(mStreamItem.getId());
            isFirstLoad = false;
        }
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            float height = getResources().getDimensionPixelSize(R.dimen.preview_height) - mToolbar.getHeight();  //获取图片的高度
            L.i(LOG_TAG + " height: " + height);
            if (dy < height){
                int i = Float.valueOf(dy * 255/height).intValue();    //i 有可能小于 0
                L.i(LOG_TAG + " i: " + i);
                mToolbar.getBackground().setAlpha(Math.max(i,0));   // 0~255 透明度
            }else {
                mToolbar.getBackground().setAlpha(255);
            }
        }
    };
}
