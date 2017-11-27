/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/3/31.
 */
package sg.com.conversant.swiftplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.adapter.MediaDetailAdapter;
import sg.com.conversant.swiftplayer.fragment.MediaPlayerFragment;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.sdk.client.ProductClient;
import sg.com.conversant.swiftplayer.sdk.module.CommentItem;
import sg.com.conversant.swiftplayer.sdk.module.ContentItem;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class MediaDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = "MediaDetailActivity";

    public static final String INTENT_CONTENT_ITEM = "content_item";
    private static final String PLAYER_FRAGMENT_TAG = "media_player_fragment_tag";
    private static final String COMMENT_FRAGMENT_TAG = "media_comments_fragment_tag";

    private ImageView mBackBtn;
    private ImageView mFavoriteBtn;
    private FrameLayout mPreview;
    private RecyclerView mRecyclerView;
    private LinearLayout mReplyCommentLayout;
    private TextView mCancelReplyView;
    private SimpleDraweeView mAvatarView;
    private EditText mEditView;
    private ImageView mSendBtn;

    private ContentItem mContentItem;
    private MediaDetailAdapter mMediaDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);

        getIntentExtra();

        setupUI();
    }

    private void getIntentExtra() {
        Intent mIntent = getIntent();
        mContentItem = (ContentItem) mIntent.getSerializableExtra(INTENT_CONTENT_ITEM);
    }

    private void setupUI() {
        mBackBtn = (ImageView) findViewById(R.id.back_btn);
        mFavoriteBtn = (ImageView) findViewById(R.id.favorite_btn);
        mPreview = (FrameLayout) findViewById(R.id.preview);

        WindowManager wm = getWindowManager();
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);
        int width = outSize.x;
        int height = width * 9/16;

        FrameLayout.LayoutParams videoParams = new FrameLayout.LayoutParams(width, height);
        mPreview.setLayoutParams(videoParams);

        mBackBtn.setOnClickListener(this);
        mFavoriteBtn.setOnClickListener(this);

        addPlayerFragment();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mMediaDetailAdapter = new MediaDetailAdapter(this);
        mRecyclerView.setAdapter(mMediaDetailAdapter);

        mMediaDetailAdapter.deleteContents();

        mReplyCommentLayout = (LinearLayout) findViewById(R.id.comment_edit_layout);
        mReplyCommentLayout.setVisibility(View.GONE);

        mCancelReplyView = (TextView) findViewById(R.id.cancel_reply);
        mAvatarView = (SimpleDraweeView) findViewById(R.id.avatar_view);
        mEditView = (EditText) findViewById(R.id.comment_edit);
        mSendBtn = (ImageView) findViewById(R.id.comment_send);

        setMediaInfo();

        setFavoriteContent();
    }

    private void addPlayerFragment() {
        Fragment fragment = new MediaPlayerFragment();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(MediaDetailActivity.INTENT_CONTENT_ITEM, mContentItem);
        fragment.setArguments(mBundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.preview,
                fragment, PLAYER_FRAGMENT_TAG).commit();
        fragmentManager.executePendingTransactions();
    }

    private void setMediaInfo() {
        List<ContentItem> mediaInfoList = new ArrayList<>();
        mediaInfoList.add(mContentItem);

        if (mMediaDetailAdapter != null) {
            mMediaDetailAdapter.setHeaderMediaInfo(mediaInfoList);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:

                if (Utils.getScreenOrientation(this) ==
                        Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                } else {
                    finish();

                }
                break;

            case R.id.favorite_btn:

                break;
        }
    }

    private void setFavoriteContent() {
        L.i(LOG_TAG + " setFavoriteContent ");

        ProductClient.get(API.GET_SERIES_LIST, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                L.e(LOG_TAG + " status code " + statusCode);

                if (statusCode == 0) {
                    Toast toast = Toast.makeText(MediaDetailActivity.this, R.string.no_network, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                L.i(LOG_TAG + " status code " + statusCode);

                List<ContentItem> contentItems = getContentsFromJson(responseString);

                if (contentItems != null && mMediaDetailAdapter != null) {
                    mMediaDetailAdapter.setHeaderMediaFavorites(contentItems);
                }

                setCommentsContent();
            }
        });
    }

    private List<ContentItem> getContentsFromJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ContentItem>>(){}.getType();
        return gson.fromJson(json, type);
    }

    private void setCommentsContent() {
        L.i(LOG_TAG + " setCommentsContent ");

        ProductClient.get(API.GET_COMMENT_LIST, null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                L.e(LOG_TAG + " status code " + statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                L.i(LOG_TAG + " status code " + statusCode);

                List<CommentItem> commentItems = getCommentsFromJson(responseString);

                if (commentItems != null && mMediaDetailAdapter != null) {
                    mMediaDetailAdapter.setCommentItems(commentItems);
                }
            }
        });
    }

    private List<CommentItem> getCommentsFromJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<CommentItem>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void hidePlayerFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager
                .findFragmentByTag(PLAYER_FRAGMENT_TAG);

        if (fragment.isAdded() && fragment.isVisible()) {
            fragmentManager.beginTransaction().remove(fragment)
                    .commitAllowingStateLoss();
        }
    }

    public void hideNavigationBar() {
        if (mPreview != null) {
            mPreview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    public void refreshContent(ContentItem item) {
        mContentItem = item;

        setupUI();
    }

    public void scrollToTop() {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    public void replyCommentLayout(String prefix) {
        if (mReplyCommentLayout != null) {
            mReplyCommentLayout.setVisibility(View.VISIBLE);
        }

        mCancelReplyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(mEditView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                mEditView.setText("");
                mReplyCommentLayout.setVisibility(View.GONE);
            }
        });

        final String replyPrefix = getResources().getString(R.string.default_reply, prefix);
        mEditView.setHint(replyPrefix);
        mEditView.setFocusable(true);
        mEditView.setFocusableInTouchMode(true);
        mEditView.requestFocus();
        mEditView.findFocus();
        InputMethodManager imm = (InputMethodManager) mEditView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        mEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mEditView.setCursorVisible(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mEditView.setSelection(mEditView.getText().length());
            }
        });

        mEditView.setImeOptions(EditorInfo.IME_ACTION_SEND);

        mEditView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(mEditView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    if (mEditView.getText().toString() == null ||
                            mEditView.getText().toString().equals("")) {
                        mReplyCommentLayout.setVisibility(View.GONE);
                        return true;
                    }
                    CommentItem item = new CommentItem();
                    item.setName(getResources().getString(R.string.default_anonymous_user));
                    item.setFavorite(0);
                    item.setComment(replyPrefix + mEditView.getText().toString());
                    item.setTimestamp(String.valueOf(System.currentTimeMillis()));
                    item.setFavoriteCount(0);
                    item.setDeviceModel(Utils.getUserAgent());
                    item.setAvatar("");
                    mEditView.setText("");
                    mReplyCommentLayout.setVisibility(View.GONE);

                    if (mMediaDetailAdapter != null) {
                        mMediaDetailAdapter.addCommentItem(item);
                    }

                    return true;
                }
                return false;
            }
        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(mEditView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (mEditView.getText().toString() == null ||
                        mEditView.getText().toString().equals("")) {
                    mReplyCommentLayout.setVisibility(View.GONE);

                    return;
                }
                CommentItem item = new CommentItem();
                item.setName(getResources().getString(R.string.default_anonymous_user));
                item.setFavorite(0);
                item.setComment(replyPrefix + mEditView.getText().toString());
                item.setTimestamp(String.valueOf(System.currentTimeMillis()));
                item.setFavoriteCount(0);
                item.setDeviceModel(Utils.getUserAgent());
                item.setAvatar("");
                mEditView.setText("");
                mReplyCommentLayout.setVisibility(View.GONE);

                if (mMediaDetailAdapter != null) {
                    mMediaDetailAdapter.addCommentItem(item);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        FrameLayout.LayoutParams layoutParams;

        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                layoutParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                mPreview.setLayoutParams(layoutParams);

                mPreview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                break;

            case Configuration.ORIENTATION_PORTRAIT:

                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                int height = getResources().getDimensionPixelSize(R.dimen.preview_height);
                int verticalMargin = getResources().getDimensionPixelSize(R.dimen.default_padding);

                layoutParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, height);
                layoutParams.setMargins(0, 0, 0, verticalMargin);

                mPreview.setLayoutParams(layoutParams);

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                Utils.getScreenOrientation(this) ==
                        Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager
                    .findFragmentByTag(PLAYER_FRAGMENT_TAG);

            if (fragment.isAdded() && fragment.isVisible()) {
                ((MediaPlayerFragment) fragment).setFullScreenIcon();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}

