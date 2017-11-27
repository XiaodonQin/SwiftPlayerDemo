package sg.com.conversant.swiftplayer.mvp.ui.fragment;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.activity.MediaDetailActivity;
import sg.com.conversant.swiftplayer.fragment.BaseFragment;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.sdk.Constants;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/12.
 */

public class StreamDetailPlayerFragment extends BaseFragment implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, View.OnClickListener {
    private static String LOG_TAG = StreamDetailPlayerFragment.class.getSimpleName();

    private static StreamDetailPlayerFragment INSTANCE;

    private StreamDetail mStreamDetail;
    private Handler mMainHandler;
    private static final int CHECK_VISIBLE_TIME = 5000;
    private long mCurrentPosition = 0;
    private long mDuration = 0;
    private boolean mIsPlaying = false;
    private boolean mPlayOver = false;
    private String mVideoPlayUrl;

    @InjectView(R.id.video_view)
    VideoView mVideoView;

    @InjectView(R.id.video_reload_layout)
    LinearLayout mVideoReloadLayout;

    @InjectView(R.id.bottom_controller_layout)
    LinearLayout mBottomControllerLayout;

    @InjectView(R.id.play_pause_button)
    Button mPlayButton;

    @InjectView(R.id.current_time)
    TextView mCurrentTimeView;

    @InjectView(R.id.duration)
    TextView mDurationView;

    @InjectView(R.id.seekbar)
    SeekBar mSeekBar;

    @InjectView(R.id.full_screen_button)
    Button mFullScreenButton;

    @InjectView(R.id.progress)
    ContentLoadingProgressBar mProgressBar;


    public static StreamDetailPlayerFragment newInstance() {
        if (INSTANCE == null) {
            return new StreamDetailPlayerFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainHandler = new Handler();
        Bundle mBundle = getArguments();
        mStreamDetail = (StreamDetail) mBundle.getSerializable(API.BUNDLE_PARAMS.STREAM_DETAIL);

        if (mStreamDetail == null) {
            return;
        }

        mVideoPlayUrl = Constants.SERVER_URL + mStreamDetail.getPlayUrl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stream_detail_player, container, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }

    private void initView() {
        mVideoReloadLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);

        mPlayButton.setOnClickListener(this);
        mFullScreenButton.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mVideoReloadLayout.setOnClickListener(this);
        mVideoView.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mMainHandler.postDelayed(mVisibleChecker, CHECK_VISIBLE_TIME);
        mVideoView.setVideoPath(mVideoPlayUrl);
        L.i(LOG_TAG + " play url: " + mVideoPlayUrl);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        L.i("onCompletion");

        if (mMainHandler != null) {
            mMainHandler.removeCallbacks(mProgressChecker);
            mIsPlaying = false;
        }

        if (mPlayButton != null) {
            mPlayButton.setBackgroundResource(android.R.drawable.ic_media_play);
            mCurrentTimeView.setText(Utils.stringForTime(mDuration));
            mVideoReloadLayout.setVisibility(View.VISIBLE);
        }

        mPlayOver = true;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        L.i("onError");
        if (mMainHandler != null) {
            mMainHandler.removeCallbacks(mProgressChecker);
            mIsPlaying = false;
        }
        mVideoReloadLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mPlayButton.setBackgroundResource(android.R.drawable.ic_media_play);
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        L.i("onPrepared");
        mp.start();
        mp.setOnInfoListener((mp1, what, extra) -> {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                    L.i("onInfo MEDIA_INFO_VIDEO_RENDERING_START");
                    showLoadingBar(false);
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                    L.i("onInfo MEDIA_INFO_BUFFERING_START");
                    showLoadingBar(true);
                    return true;
                }
                case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                    L.i("onInfo MEDIA_INFO_BUFFERING_END");
                    showLoadingBar(false);
                    return true;
                }
            }
            return false;
        });
        mIsPlaying = true;
        mPlayButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        mDuration = mp.getDuration();
        mDurationView.setText(Utils.stringForTime(mDuration));
        mSeekBar.setMax((int) mDuration);
        mMainHandler.post(mProgressChecker);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_pause_button:

                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mMainHandler.removeCallbacks(mProgressChecker);
                    mIsPlaying = false;
                    mPlayButton.setBackgroundResource(android.R.drawable.ic_media_play);
                } else {
                    if (mVideoReloadLayout.getVisibility() == View.VISIBLE) {
                        mVideoReloadLayout.setVisibility(View.GONE);
                    }
                    mVideoView.start();
                    mMainHandler.post(mProgressChecker);
                    mIsPlaying = true;
                    mPlayButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                }

                break;

            case R.id.full_screen_button:
                break;

            case R.id.video_reload_layout:
                mVideoReloadLayout.setVisibility(View.GONE);
                mVideoView.start();
                mMainHandler.post(mProgressChecker);
                mIsPlaying = true;
                mPlayButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                break;

            case R.id.video_view:
                if (mBottomControllerLayout.getVisibility() == View.VISIBLE) {
                    mBottomControllerLayout.setVisibility(View.GONE);
                } else {
                    mBottomControllerLayout.setVisibility(View.VISIBLE);
                }

                if (mMainHandler != null) {
                    mMainHandler.removeCallbacks(mVisibleChecker);
                    mMainHandler.postDelayed(mVisibleChecker, CHECK_VISIBLE_TIME);
                }
                break;
        }
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mCurrentTimeView.setText(Utils.stringForTime(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mMainHandler.removeCallbacks(mProgressChecker);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mVideoView.seekTo(seekBar.getProgress());
            mMainHandler.post(mProgressChecker);
        }
    };

    private Runnable mProgressChecker = new Runnable() {
        @Override
        public void run() {
            mCurrentPosition = mVideoView.getCurrentPosition();
            mCurrentTimeView.setText(Utils.stringForTime(mCurrentPosition));
            mSeekBar.setProgress((int) mCurrentPosition);
            mMainHandler.postDelayed(this, 1000);
        }
    };

    private Runnable mVisibleChecker = new Runnable() {
        @Override
        public void run() {
            if (mBottomControllerLayout.getVisibility() == View.VISIBLE) {
                mBottomControllerLayout.setVisibility(View.GONE);
                mMainHandler.removeCallbacks(this);
            }
        }
    };

    private void showLoadingBar(boolean show) {
        if (show) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
