/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/1/15.
 */
package sg.com.conversant.swiftplayer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.activity.MediaDetailActivity;
import sg.com.conversant.swiftplayer.sdk.module.ContentItem;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class MainViewPagerAdapter extends PagerAdapter {

    private Activity mContext;

    private LayoutInflater layoutInflater;

    private List<ContentItem> posterItems;

    public MainViewPagerAdapter(Activity activity) {
        this.mContext = activity;
        layoutInflater = LayoutInflater.from(mContext);

        posterItems = new ArrayList<>();
    }

    public void setPosterItems(List<ContentItem> items) {
        if (posterItems != null) {
            posterItems.clear();
            posterItems.addAll(items);
        }

        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView = layoutInflater.inflate(R.layout.item_main_viewpager_poster, container, false);

        ContentItem item = posterItems.get(position);

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.image_header);

        WindowManager wm = mContext.getWindowManager();
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);

        int cardMargin = mContext.getResources().getDimensionPixelSize(R.dimen.default_padding);
        int coverWidth = outSize.x - 2 * cardMargin;
        int coverHeight = coverWidth * 9/16;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(coverWidth, coverHeight);
        simpleDraweeView.setLayoutParams(params);

        simpleDraweeView.setController(getDraweeController(item.getThumbnailPath()));

        simpleDraweeView.setTag(item);

        simpleDraweeView.setOnClickListener(onClickListener);

        container.addView(convertView, 0);

        return convertView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public int getCount() {
        return posterItems == null ? 0 : posterItems.size();
    }

    private DraweeController getDraweeController(String url) {
        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();

        return controller;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContentItem item = (ContentItem) v.getTag();

            Intent mIntent = new Intent(mContext, MediaDetailActivity.class);
            mIntent.putExtra(MediaDetailActivity.INTENT_CONTENT_ITEM, item);
            mContext.startActivity(mIntent);
        }
    };
}
