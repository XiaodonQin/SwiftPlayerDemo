/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/5/6.
 */
package sg.com.conversant.swiftplayer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.com.conversant.swiftplayer.R;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class SearchPreviewGridAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<String> itemList;

    public SearchPreviewGridAdapter(Activity activity) {

        mActivity = activity;
        itemList = new ArrayList<>();

    }

    public void setItems(List<String> items) {
        if (items != null && items.size() > 0) {
            itemList.clear();

            itemList.addAll(items);

            notifyDataSetChanged();
        }

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public String getItem(int position) {
        return itemList.size() > 0 ? itemList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemHolder itemHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_search_text, parent, false);

            itemHolder = new ItemHolder();
            itemHolder.searchTextView = (TextView) convertView.findViewById(R.id.search_text);

            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        String searchTxt = itemList.get(position);

        itemHolder.searchTextView.setText(searchTxt);

        return convertView;
    }

    public class ItemHolder {
        TextView searchTextView;
    }
}
