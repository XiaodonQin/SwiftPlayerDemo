package sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.mvp.OnItemClickListener;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/10/22.
 */

public class ZhihuNewsItemWithDateHolder extends ZhihuNewsItemHolder {

    private TextView dateView;

    public ZhihuNewsItemWithDateHolder(View itemView) {
        super(itemView);

        dateView = (TextView) itemView.findViewById(R.id.item_zhihu_news_date_view);
    }

    public static ZhihuNewsItemWithDateHolder newInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_zhihu_news_with_date, parent, false);
        return new ZhihuNewsItemWithDateHolder(view);
    }

    @Override
    public void onBindHolder(ZhihuDailyNews item, int position, OnItemClickListener<ZhihuDailyNews> listener) {
        super.onBindHolder(item, position, listener);

        dateView.setText(Utils.formatZhihuDailyDateLongToStringCurrent(item.getTimestamp()));
    }
}
