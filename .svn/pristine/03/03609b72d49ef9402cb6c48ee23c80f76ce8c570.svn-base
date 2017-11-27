package sg.com.conversant.swiftplayer.mvp.ui.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ScrollView;

import sg.com.conversant.swiftplayer.utils.L;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/10/25.
 */

public class ObservableScrollView extends ScrollView {
    private static String LOG_TAG = ObservableScrollView.class.getSimpleName();

    private OnScrollChangedListener onScrollChangedListener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        L.i(LOG_TAG + " onScrollChanged.l: " + l + ", t: " + t + ", oldl: " + oldl + ", oldt: " + oldt);

        if (this.onScrollChangedListener != null) {
            // alpha = 滑出去的高度/(screenHeight/3);
            float heightPixels = getContext().getResources().getDisplayMetrics().heightPixels;
            float scrollY = getScrollY();//该值 大于0
            float alpha = 1 - scrollY / (heightPixels / 3);// 0~1  透明度是1~0
            //这里选择的screenHeight的1/3 是alpha改变的速率 （根据你的需要你可以自己定义）
            onScrollChangedListener.onScrollChanged(t, oldt);
            onScrollChangedListener.onAlpha(alpha);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        this.onScrollChangedListener = listener;
    }
}
