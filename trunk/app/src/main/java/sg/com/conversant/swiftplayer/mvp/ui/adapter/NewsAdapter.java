package sg.com.conversant.swiftplayer.mvp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sg.com.conversant.swiftplayer.mvp.OnItemClickListener;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder.ZhihuNewsItemHolder;
import sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder.ZhihuNewsItemWithDateHolder;
import sg.com.conversant.swiftplayer.utils.L;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/28.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String LOG_TAG = NewsAdapter.class.getSimpleName();

    private static int TYPE_ITEM = 1;
    private static int TYPE_ITEM_WITH_DATE = 2;
    private List<ZhihuDailyNews> zhihuNewsList;
    private OnItemClickListener<ZhihuDailyNews> onItemClickListener;

    public NewsAdapter() {
        zhihuNewsList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return ZhihuNewsItemHolder.newInstance(parent);
        } else if (viewType == TYPE_ITEM_WITH_DATE) {
            return ZhihuNewsItemWithDateHolder.newInstance(parent);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ITEM_WITH_DATE;
        } else {
            if (zhihuNewsList.get(position - 1).getTimestamp() ==
                    zhihuNewsList.get(position).getTimestamp()) {
                return TYPE_ITEM;
            } else {
                return TYPE_ITEM_WITH_DATE;
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ZhihuNewsItemWithDateHolder) {
            ZhihuNewsItemWithDateHolder itemHolder = (ZhihuNewsItemWithDateHolder) holder;
            itemHolder.onBindHolder(zhihuNewsList.get(position), position, onItemClickListener);
        } else if (holder instanceof ZhihuNewsItemHolder) {
            ZhihuNewsItemHolder itemHolder = (ZhihuNewsItemHolder) holder;
            itemHolder.onBindHolder(zhihuNewsList.get(position), position, onItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return zhihuNewsList.size();
    }

    public void setZhihuNews(List<ZhihuDailyNews> items) {
        if (items != null) {
            zhihuNewsList.clear();
            zhihuNewsList.addAll(items);
            L.i(LOG_TAG + " zhihuNewsList.size: " + zhihuNewsList.size());

            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener<ZhihuDailyNews> listener) {
        this.onItemClickListener = listener;
    }
}
