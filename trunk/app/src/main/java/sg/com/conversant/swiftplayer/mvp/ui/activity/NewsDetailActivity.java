package sg.com.conversant.swiftplayer.mvp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.mvp.contact.ZhihuContact;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsDetail;
import sg.com.conversant.swiftplayer.mvp.data.repository.ZhihuDailyNewsRepository;
import sg.com.conversant.swiftplayer.mvp.data.local.ZhihuDailyNewsLocalDataSource;
import sg.com.conversant.swiftplayer.mvp.data.remote.ZhihuDailyNewsRemoteDataSource;
import sg.com.conversant.swiftplayer.mvp.presenter.ZhihuDailyPresenter;
import sg.com.conversant.swiftplayer.mvp.ui.view.ObservableScrollView;
import sg.com.conversant.swiftplayer.mvp.ui.view.OnScrollChangedListener;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/10/24.
 */

public class NewsDetailActivity extends AppCompatActivity implements ZhihuContact.View {
    private static String LOG_TAG = NewsDetailActivity.class.getSimpleName();

    private boolean isFirstLoad = true;
    private ZhihuContact.Presenter mPresenter;
    private long newsId;

    @InjectView(R.id.toolbar_transparent)
    Toolbar toolbar;

    @InjectView(R.id.scroll_view)
    ObservableScrollView scrollView;

    @InjectView(R.id.cover_view)
    ImageView coverView;

    @InjectView(R.id.web_view)
    WebView webView;

    @InjectView(R.id.progress)
    ContentLoadingProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        ButterKnife.inject(this);

        initData();
        initViews();
        setPresenter(new ZhihuDailyPresenter(this, ZhihuDailyNewsRepository.getInstance(
                ZhihuDailyNewsRemoteDataSource.getInstance(),
                ZhihuDailyNewsLocalDataSource.getInstance(this)
        )));
    }

    private void initData() {
        newsId = getIntent().getLongExtra(API.PARAMS_ZHIHU.INTENT_NEWS_ID, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isFirstLoad) {
            mPresenter.loadDetail(newsId);
            isFirstLoad = false;
        }
    }

    @Override
    public void setPresenter(ZhihuContact.Presenter presenter) {
        if (presenter != null) {
            this.mPresenter = presenter;
        }
    }

    @Override
    public void initViews() {
        toolbar.getBackground().setAlpha(0);
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setNavigationOnClickListener(v -> finish());

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


        scrollView.setOnScrollChangedListener(onScrollChangedListener);
        webView.setScrollbarFadingEnabled(true);
        //能够和js交互
        webView.getSettings().setJavaScriptEnabled(true);
        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        webView.getSettings().setBuiltInZoomControls(false);
        //缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启DOM storage API功能
        webView.getSettings().setDomStorageEnabled(true);
        //开启application Cache功能
        webView.getSettings().setAppCacheEnabled(false);

        // Show the images or not.
        webView.getSettings().setBlockNetworkImage(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                return true;
            }

        });
    }

    OnScrollChangedListener onScrollChangedListener = new OnScrollChangedListener() {
        @Override
        public void onScrollChanged(int y, int oldY) {
            L.i(LOG_TAG + " onScrollChanged.y: " + y + ", oldY: " + oldY);
            float height = coverView.getHeight() - toolbar.getHeight();  //获取图片的高度
            L.i(LOG_TAG + " height: " + height);
            if (oldY < height){
                int i = Float.valueOf(oldY * 255/height).intValue();    //i 有可能小于 0
                L.i(LOG_TAG + " i: " + i);
                toolbar.getBackground().setAlpha(Math.max(i,0));   // 0~255 透明度
            }else {
                toolbar.getBackground().setAlpha(255);
            }
        }

        @Override
        public void onAlpha(float alpha) {
        }
    };

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showResult(@NonNull List<ZhihuDailyNews> list) {

    }

    @Override
    public void showResult(@NonNull ZhihuDailyNewsDetail detail) {
        Utils.showPosterWithGlide(detail.getImage(), coverView);

        if (detail.getBody() != null) {
            String result = detail.getBody();
            result = result.replace("<div class=\"img-place-holder\">", "");
            result = result.replace("<div class=\"headline\">", "");

            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";

            String theme = "<body className=\"\" onload=\"onLoaded()\">";
//            if (mIsNightMode) {
//                theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
//            }

            result = "<!DOCTYPE html>\n"
                    + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                    + "<head>\n"
                    + "\t<meta charset=\"utf-8\" />"
                    + css
                    + "\n</head>\n"
                    + theme
                    + result
                    + "</body></html>";

            webView.loadDataWithBaseURL("x-data://base", result,"text/html","utf-8",null);
        } else {
            webView.loadDataWithBaseURL("x-data://base", detail.getShareUrl(),"text/html","utf-8",null);
        }
    }

    @Override
    public void setResponseOK(boolean responseOK) {

    }
}
