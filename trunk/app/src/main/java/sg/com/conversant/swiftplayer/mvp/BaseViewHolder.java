package sg.com.conversant.swiftplayer.mvp;

import android.view.View;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/28.
 */

public interface BaseViewHolder<T> {

    void onBindHolder(T item, int position, OnItemClickListener<T> listener);
}
