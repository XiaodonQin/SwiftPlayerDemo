/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2016/4/29.
 */
package sg.com.conversant.swiftplayer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.activity.CategoryDetailActivity;
import sg.com.conversant.swiftplayer.sdk.module.ChannelItem;
import sg.com.conversant.swiftplayer.views.SquareLayout;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mActivity;

    private List<ChannelItem> channelItems;

    public ChannelAdapter(Activity activity) {

        mActivity = activity;

        channelItems = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel,
                parent, false);

        return new ChannelHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ChannelHolder) {
            ChannelHolder channelHolder = (ChannelHolder) holder;

            final ChannelItem item = channelItems.get(position);

            if (item != null) {
                channelHolder.channelIcon.setImageResource(item.getChannelIcon());
                channelHolder.channelName.setText(item.getChannelName());
            }

            channelHolder.channelSquareLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, CategoryDetailActivity.class);
                    intent.putExtra(CategoryDetailActivity.ITEM_CATEGORY, item);
                    mActivity.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return channelItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void setChannelItems(List<ChannelItem> items) {
        if (items != null) {
            channelItems.clear();;
            channelItems.addAll(items);

            notifyDataSetChanged();
        }
    }

    public class ChannelHolder extends RecyclerView.ViewHolder {
        SquareLayout channelSquareLayout;
        ImageView channelIcon;
        TextView channelName;

        public ChannelHolder(View itemView) {
            super(itemView);

            channelSquareLayout = (SquareLayout) itemView;
            channelIcon = (ImageView) itemView.findViewById(R.id.channel_icon);
            channelName = (TextView) itemView.findViewById(R.id.channel_name);

        }
    }
}
