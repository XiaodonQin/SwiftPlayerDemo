/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/5/3.
 */
package sg.com.conversant.swiftplayer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.activity.MediaDetailActivity;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.sdk.Constants;
import sg.com.conversant.swiftplayer.sdk.module.ContentItem;

/**
 * TODO
 *
 * @author Xiaodong
 */
public class CategoryGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private static final int CATEGORY_TYPE_HOT = 0;
    private static final int CATEGORY_TYPE_AREA = 1;
    private static final int CATEGORY_TYPE_TYPE = 2;
    private static final int CATEGORY_TYPE_YEAR = 3;

    private boolean header = false;

    private Activity mActivity;
    private LayoutInflater inflater;

    private List<String> categoryHotList;
    private List<String> categoryAreaList;
    private List<String> categoryTypeList;
    private List<String> categoryYearList;
    private List<ContentItem> contentItems;

    private String filterHot;
    private String filterArea;
    private String filterType;
    private String filterYear;

    public CategoryGridAdapter(Activity activity) {
        this.mActivity = activity;

        this.inflater = LayoutInflater.from(mActivity);

        categoryHotList = new ArrayList<>();
        categoryAreaList = new ArrayList<>();
        categoryTypeList = new ArrayList<>();
        categoryYearList = new ArrayList<>();

        contentItems = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && header) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_filter_bar, parent, false);
            return new HeaderHolder(view);
        }

        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_grid, parent, false);
            return new ItemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;

            setupFilterBar(categoryHotList, headerHolder.mCategoryHotContainer, CATEGORY_TYPE_HOT);
            setupFilterBar(categoryAreaList, headerHolder.mCategoryAreaContainer, CATEGORY_TYPE_AREA);
            setupFilterBar(categoryTypeList, headerHolder.mCategoryTypeContainer, CATEGORY_TYPE_TYPE);
            setupFilterBar(categoryYearList, headerHolder.mCategoryYearContainer, CATEGORY_TYPE_YEAR);


        } else if (holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;

            final ContentItem item = contentItems.get(position);

            if (item != null) {
                itemHolder.simpleDraweeView.setController(getDraweeController(Constants.SERVER_URL + item.getThumbnailPath()));
                itemHolder.itemNameView.setText(item.getName());

                itemHolder.simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent(mActivity, MediaDetailActivity.class);
                        mIntent.putExtra(MediaDetailActivity.INTENT_CONTENT_ITEM, item);
                        mActivity.startActivity(mIntent);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return contentItems.size();
    }

    public void setContentItems(List<ContentItem> items) {
        if (items != null && items.size() > 0) {
            contentItems.addAll(items);

            notifyDataSetChanged();
        }
    }

    public void deleteAllItems() {
        if (contentItems != null) {
            contentItems.clear();
        }
    }

    public void setHeaderItems(List<String> hots, List<String> areas, List<String> types, List<String> years) {
        if ((hots != null && hots.size() > 0) || (areas != null && areas.size() > 0) ||
                (types != null && types.size() > 0) || (years != null && years.size() > 0)) {
            this.header = true;
            contentItems.add(0, null);
            if (hots != null && hots.size() > 0) {
                categoryHotList.clear();
                categoryHotList.addAll(hots);
            }

            if (areas != null && areas.size() > 0) {
                categoryAreaList.clear();
                categoryAreaList.addAll(areas);
            }

            if (types != null && types.size() > 0) {
                categoryTypeList.clear();
                categoryTypeList.addAll(types);
            }

            if (years != null && years.size() > 0) {
                categoryYearList.clear();
                categoryYearList.addAll(years);
            }

            notifyItemChanged(0);
        }
    }

    public boolean isHeader(int position) {
        return header && position == 0;
    }

    private void setupFilterBar(List<String> filters, final LinearLayout container, final int type) {
        if (filters == null || filters.size() == 0) {
            container.removeAllViews();
            return;
        }

        FilterHolder filterHolder;

        int mDataSize = filters.size();
        int mChildViewSize = container.getChildCount();

        if (mChildViewSize < mDataSize) {
            while (mChildViewSize < mDataSize) {
                filterHolder = new FilterHolder();
                TextView itemView = (TextView) inflater.inflate(R.layout.item_category_filter, null);

                filterHolder.textView = itemView;

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int margin = mActivity.getResources().getDimensionPixelSize(R.dimen.default_padding);
                layoutParams.setMargins(margin, margin, margin, margin);

                filterHolder.textView.setLayoutParams(layoutParams);
                filterHolder.position = mChildViewSize;

                itemView.setTag(filterHolder);

                container.addView(itemView);

                mChildViewSize++;

            }
        } else {
            while (mChildViewSize > mDataSize) {
                container.removeViewAt(0);
                mChildViewSize--;
            }
        }

        //set item text
        for (int i = 0; i < mDataSize; i++) {
            final String item = filters.get(i);

            TextView itemView = (TextView) container.getChildAt(i);
            filterHolder = (FilterHolder) itemView.getTag();
            filterHolder.textView.setText(item);

            if (i == 0) {
                filterHolder.textView.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                filterHolder.textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                setFilterText(filterHolder.textView.getText().toString(), type);
            } else {
                filterHolder.textView.setTextColor(mActivity.getResources().getColor(R.color.black_light));
                filterHolder.textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }

            filterHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FilterHolder holder = (FilterHolder) v.getTag();

                    int mCurrentPosition = holder.position;

                    refreshFilterUI(container, mCurrentPosition, type);
                }
            });
        }
    }

    private void setFilterText(String filter, int type) {
        switch (type) {
            case CATEGORY_TYPE_HOT:

                filterHot = filter;
                break;
            case CATEGORY_TYPE_AREA:

                filterArea = filter;
                break;
            case CATEGORY_TYPE_TYPE:

                filterType = filter;
                break;
            case CATEGORY_TYPE_YEAR:

                filterYear = filter;
                break;
        }
    }

    private void refreshFilterUI(LinearLayout container, int position, int type) {
        if (container == null || position < 0) {
            return;
        }

        if (container.getChildCount() <= position) {
            return;
        }

        for (int i = 0; i < container.getChildCount(); i++) {
            TextView view = (TextView) container.getChildAt(i);

            if (i == position) {
                view.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                view.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                setFilterText(view.getText().toString(), type);
            } else {
                view.setTextColor(mActivity.getResources().getColor(R.color.black_light));
                view.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }

    public String getFilterText() {
        return filterHot + " · " + filterArea + " · " + filterType + " · " + filterYear;
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

    public class HeaderHolder extends RecyclerView.ViewHolder {

        LinearLayout mCategoryHotContainer;
        LinearLayout mCategoryAreaContainer;
        LinearLayout mCategoryTypeContainer;
        LinearLayout mCategoryYearContainer;

        public HeaderHolder(View itemView) {
            super(itemView);

            mCategoryHotContainer = (LinearLayout) itemView.findViewById(R.id.category_hot_layout);
            mCategoryAreaContainer = (LinearLayout) itemView.findViewById(R.id.category_area_layout);
            mCategoryTypeContainer = (LinearLayout) itemView.findViewById(R.id.category_type_layout);
            mCategoryYearContainer = (LinearLayout) itemView.findViewById(R.id.category_year_layout);
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView simpleDraweeView;
        TextView itemNameView;

        public ItemHolder(View itemView) {
            super(itemView);

            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.simple_drawee_view);
            itemNameView = (TextView) itemView.findViewById(R.id.item_content_name);
        }
    }

    public class FilterHolder {
        int position;
        TextView textView;
    }
}
