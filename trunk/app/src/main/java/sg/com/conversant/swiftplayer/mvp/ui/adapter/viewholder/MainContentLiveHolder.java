package sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder;

import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.mvp.BaseViewHolder;
import sg.com.conversant.swiftplayer.mvp.OnItemClickListener;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.StreamItemCluster;
import sg.com.conversant.swiftplayer.utils.Utils;


/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/6.
 */

public class MainContentLiveHolder extends RecyclerView.ViewHolder implements BaseViewHolder<StreamItemCluster> {

    private Fragment mFragment;
    private View mView;
    private TextView title;
    private FlexboxLayout container;
    private LinearLayout.LayoutParams lp;
    private LinearLayout.LayoutParams titleLp;

    private MainContentLiveHolder(Fragment fragment, View itemView) {
        super(itemView);

        this.mFragment = fragment;
        this.mView = itemView;
        title = itemView.findViewById(R.id.header);
        container = itemView.findViewById(R.id.container);

        title.setText(this.mFragment.getActivity().getResources().getString(R.string.category_live_text));
        title.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_live, 0, 0, 0);
        title.setCompoundDrawablePadding(5);

        createFlexWeightLayoutParams();
    }

    public static MainContentLiveHolder newInstance(ViewGroup parent, Fragment fragment) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_layout,
                parent, false);
        return new MainContentLiveHolder(fragment, view);
    }

    @Override
    public void onBindHolder(StreamItemCluster cluster, int position, OnItemClickListener<StreamItemCluster> listener) {
        initFlexLayout(cluster.getStreamItems());
    }

    private void initFlexLayout(List<StreamItem> items) {
        container.removeAllViews();

        LinearLayout flexLayout;
        FlexMainItemStreamHolder flexItemHolder;
        int iDataSize = items.size();
        int childViewSize = container.getChildCount();
        if (childViewSize < iDataSize) {
            while (childViewSize < iDataSize) {
                flexLayout = (LinearLayout) View.inflate(mView.getContext(), R.layout.item_flex_main_stream, null);
                flexItemHolder = new FlexMainItemStreamHolder(flexLayout);
                flexItemHolder.flexItemLayout.setTag(flexItemHolder);
                container.addView(flexLayout);
                childViewSize++;
            }
        } else {
            while (childViewSize > iDataSize) {
                container.removeViewAt(0);
                childViewSize--;
            }
        }

        StreamItem streamItem;
        Object tagObject;
        for (int i = 0; i < iDataSize; i++) {
            streamItem = items.get(i);
            flexLayout = (LinearLayout) container.getChildAt(i);
            tagObject = flexLayout.getTag();
            flexItemHolder = (FlexMainItemStreamHolder) tagObject;
            flexItemHolder.cardView.setLayoutParams(lp);
            flexItemHolder.title.setLayoutParams(titleLp);
            flexItemHolder.title.setPadding(5, 0, 5, 0);
            Utils.showPosterWithGlide(streamItem.getThumbnailPath(), flexItemHolder.cover);
            flexItemHolder.title.setText(streamItem.getName());
        }
    }

    private void createFlexWeightLayoutParams() {
        WindowManager wm = mFragment.getActivity().getWindowManager();
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);

        int margin = mFragment.getActivity().getResources().getDimensionPixelSize(R.dimen.default_padding_1);
        int coverWidth = (outSize.x - 8 * margin) / 3;// 每行三个
        int coverHeight = coverWidth * 9 / 16;

        lp = new LinearLayout.LayoutParams(coverWidth, coverHeight);
        lp.setMargins(margin, margin, margin, margin);

        titleLp = new LinearLayout.LayoutParams(coverWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.setMargins(margin, margin, margin, margin);
    }
}
