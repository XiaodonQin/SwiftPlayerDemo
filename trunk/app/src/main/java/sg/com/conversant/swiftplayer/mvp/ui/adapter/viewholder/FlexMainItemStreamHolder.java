package sg.com.conversant.swiftplayer.mvp.ui.adapter.viewholder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sg.com.conversant.swiftplayer.R;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/6.
 */

public class FlexMainItemStreamHolder {

    LinearLayout flexItemLayout;
    CardView cardView;
    ImageView cover;
    TextView title;


    public FlexMainItemStreamHolder(View itemView) {
        flexItemLayout = itemView.findViewById(R.id.item_flex_main_stream_layout);
        cardView = itemView.findViewById(R.id.card);
        cover = itemView.findViewById(R.id.cover);
        title = itemView.findViewById(R.id.title);
    }
}
