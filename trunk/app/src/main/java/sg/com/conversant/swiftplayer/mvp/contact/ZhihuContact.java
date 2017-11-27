package sg.com.conversant.swiftplayer.mvp.contact;

import android.support.annotation.NonNull;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.BasePresenter;
import sg.com.conversant.swiftplayer.mvp.BaseView;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsDetail;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/14.
 */

public interface ZhihuContact {

    interface Presenter extends BasePresenter {

        void loadData(boolean forceUpdate, boolean clearCache, long date);

        void loadDetail(long id);
    }

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void setLoadingIndicator(boolean active);

        void showResult(@NonNull List<ZhihuDailyNews> list);

        void showResult(@NonNull ZhihuDailyNewsDetail detail);

        void setResponseOK(boolean responseOK);

    }


}
