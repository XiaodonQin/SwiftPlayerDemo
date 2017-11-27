package sg.com.conversant.swiftplayer.mvp.contact;

import android.support.annotation.NonNull;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.BasePresenter;
import sg.com.conversant.swiftplayer.mvp.BaseView;
import sg.com.conversant.swiftplayer.mvp.data.StreamComment;
import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/10.
 */

public interface StreamDetailContact {

    interface Presenter extends BasePresenter {
        void loadStreamDetail(long id);

        void loadStreamRecommends();

        void loadStreamComments();
    }

    interface View extends BaseView<Presenter> {
        boolean isActive();

        void showLoadingIndicator(boolean active);

        void showStreamDetailResult(@NonNull StreamDetail detail);

        void startLoadRecommend();

        void showStreamRecommendResult(@NonNull List<StreamItem> recommends);

        void startLoadComments();

        void showStreamCommentsResult(@NonNull List<StreamComment> comments);
    }
}
