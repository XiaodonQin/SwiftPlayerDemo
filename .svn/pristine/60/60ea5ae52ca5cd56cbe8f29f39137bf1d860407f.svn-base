package sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.mvp.BaseViewHolder;
import sg.com.conversant.swiftplayer.mvp.OnItemClickListener;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/28.
 */

public class ZhihuNewsItemHolder extends RecyclerView.ViewHolder implements BaseViewHolder<ZhihuDailyNews> {
    private static String LOG_TAG = ZhihuNewsItemHolder.class.getSimpleName();

    private CardView cardView;
    private TextView titleView;
    private ImageView coverView;

    private Context context;

    public ZhihuNewsItemHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();

        cardView = itemView.findViewById(R.id.item_zhihu_news_card);
        titleView = itemView.findViewById(R.id.text_view_title);
        coverView = itemView.findViewById(R.id.image_view_cover);
    }

    public static ZhihuNewsItemHolder newInstance(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_zhihu_news, parent, false);
        return new ZhihuNewsItemHolder(view);
    }

    @Override
    public void onBindHolder(ZhihuDailyNews item, int position, OnItemClickListener<ZhihuDailyNews> listener) {
        if (item == null) return;
        L.i(LOG_TAG + " item.title: " + item.getTitle());
        L.i(LOG_TAG + " item.cover: " + item.getImages().get(0));
        titleView.setText(item.getTitle());
        Utils.showThumbnailWithGlide(item.getImages().get(0), coverView);

        if (item.isRead()) {
            titleView.setTextColor(context.getResources().getColor(R.color.colorBlackLight));
        } else {
            titleView.setTextColor(context.getResources().getColor(R.color.colorBlackDark));
        }
        cardView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item, position);
        });
    }
}
