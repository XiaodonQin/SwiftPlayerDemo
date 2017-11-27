/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2016/3/31.
 */
package sg.com.conversant.swiftplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import sg.com.conversant.swiftplayer.sdk.module.CommentItem;
import sg.com.conversant.swiftplayer.sdk.module.ContentItem;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.UIUtils;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * TODO
 *
 * @author Xiaodong
 */
public class MediaDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_MEDIA_INFO = 0;
    private static final int TYPE_FAVORITE_LIST = 1;
    private static final int TYPE_COMMENTS_LIST = 2;

    private Activity context;
    private LayoutInflater inflater;

    private List<ContentItem> mediaInfoList;
    private List<ContentItem> favoritesList;
    private List<CommentItem> commentItems;

    private boolean headerMediaInfo = false;
    private boolean headerMediaFavorites = false;

    public MediaDetailAdapter(Activity context) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);

        mediaInfoList = new ArrayList<>();
        favoritesList = new ArrayList<>();
        commentItems = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MediaInfoHolder) {
            final MediaInfoHolder infoHolder = (MediaInfoHolder) holder;

            if (mediaInfoList != null && mediaInfoList.size() > 0) {
                ContentItem mCurrentItem = mediaInfoList.get(0);

                infoHolder.nameView.setText(mCurrentItem.getName());
                infoHolder.infoTitleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean tag = (Boolean) v.getTag(); //clicked?
                        if (!tag) {
                            infoHolder.arrowView.setImageResource(R.mipmap.ic_arrow_up_dark);
                            infoHolder.infoTitleLayout.setTag(true);
                            infoHolder.desView.setVisibility(View.VISIBLE);
                            ((MediaDetailActivity) context).scrollToTop();
                        } else {
                            infoHolder.arrowView.setImageResource(R.mipmap.ic_arrow_down_dark);
                            infoHolder.infoTitleLayout.setTag(false);
                            infoHolder.desView.setVisibility(View.GONE);
                        }
                    }
                });
                infoHolder.desView.setText(context.getResources().getString(R.string.default_watch_count) + "\n" + mCurrentItem.getDescription());
            }

        } else if (holder instanceof MediaFavoriteHolder) {
            final MediaFavoriteHolder favoriteHolder = (MediaFavoriteHolder) holder;

            if (favoritesList != null && favoritesList.size() > 0) {
                favoriteHolder.favoriteTitleView.setVisibility(View.VISIBLE);
                fillFavoriteContainer(favoriteHolder.favoriteContainer, favoritesList);
            } else {
                favoriteHolder.favoriteTitleView.setVisibility(View.GONE);
            }

            favoriteHolder.editView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    favoriteHolder.editView.setCursorVisible(true);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    favoriteHolder.editView.setSelection(favoriteHolder.editView.getText().length());
                }
            });

            favoriteHolder.editView.setImeOptions(EditorInfo.IME_ACTION_SEND);

            favoriteHolder.editView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(favoriteHolder.editView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        if (favoriteHolder.editView.getText().toString() == null ||
                                favoriteHolder.editView.getText().toString().equals("")) {
                            return true;
                        }
                        CommentItem item = new CommentItem();
                        item.setName(context.getResources().getString(R.string.default_anonymous_user));
                        item.setFavorite(0);
                        item.setComment(favoriteHolder.editView.getText().toString());
                        item.setTimestamp(String.valueOf(System.currentTimeMillis()));
                        item.setFavoriteCount(0);
                        item.setDeviceModel(Utils.getUserAgent());
                        item.setAvatar("");

                        addCommentItem(item);

                        favoriteHolder.editView.setText("");
                        return true;
                    }
                    return false;
                }
            });

            favoriteHolder.sendView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(favoriteHolder.editView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    if (favoriteHolder.editView.getText().toString() == null ||
                            favoriteHolder.editView.getText().toString().equals("")) {
                        return;
                    }
                    CommentItem item = new CommentItem();
                    item.setName(context.getResources().getString(R.string.default_anonymous_user));
                    item.setFavorite(0);
                    item.setComment(favoriteHolder.editView.getText().toString());
                    item.setTimestamp(String.valueOf(System.currentTimeMillis()));
                    item.setFavoriteCount(0);
                    item.setDeviceModel(Utils.getUserAgent());
                    item.setAvatar("");

                    addCommentItem(item);

                    favoriteHolder.editView.setText("");
                }
            });

        } else if (holder instanceof MediaCommentHolder) {
            final MediaCommentHolder commentHolder = (MediaCommentHolder) holder;

            final CommentItem item = commentItems.get(position);
            int dataSize = commentItems.size();

            if (item == null) {
                return;
            }

            commentHolder.simpleDraweeView.setController(getDraweeController(item.getAvatar()));
            commentHolder.nameView.setText(item.getName());
            commentHolder.timeView.setText(Utils.getPublishedTime(Long.parseLong(item.getTimestamp()), context));
            commentHolder.floorView.setText(context.getResources().getString(R.string.floor_text, dataSize - position));
            commentHolder.commentView.setText(item.getComment());
            commentHolder.deviceView.setText(context.getResources().getString(R.string.device_model_text, item.getDeviceModel()));
            commentHolder.favoriteCount.setText(String.valueOf(item.getFavoriteCount()));

            if (item.getFavorite() == 0) {
                commentHolder.favoriteView.setImageResource(R.mipmap.ic_heart);
            } else {
                commentHolder.favoriteView.setImageResource(R.mipmap.ic_heart_pressed);
            }

            commentHolder.favoriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getFavorite() == 0) {
                        item.setFavoriteCount(item.getFavoriteCount() + 1);
                        commentHolder.favoriteCount.setText(String.valueOf(item.getFavoriteCount()));
                        commentHolder.favoriteView.setImageResource(R.mipmap.ic_heart_pressed);
                        item.setFavorite(1);
                    } else {
                        item.setFavoriteCount(item.getFavoriteCount() - 1);
                        commentHolder.favoriteCount.setText(String.valueOf(item.getFavoriteCount()));
                        commentHolder.favoriteView.setImageResource(R.mipmap.ic_heart);
                        item.setFavorite(0);
                    }

                }
            });

            commentHolder.replyView.setTag(item);
            commentHolder.replyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MediaDetailActivity) context).replyCommentLayout(item.getName());
                }
            });

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_MEDIA_INFO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_detail_info,
                    parent, false);

            return new MediaInfoHolder(view);
        } else if (viewType == TYPE_FAVORITE_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_guess_you_like,
                    parent, false);

            return new MediaFavoriteHolder(view);

        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,
                parent, false);

        return new MediaCommentHolder(view);
    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0 && headerMediaInfo) {
            return TYPE_MEDIA_INFO;
        } else if (position == 0 && !headerMediaInfo) {
            return TYPE_FAVORITE_LIST;
        } else if (position == 1 && headerMediaFavorites) {
            return TYPE_FAVORITE_LIST;
        }

        return TYPE_COMMENTS_LIST;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setHeaderMediaInfo(List<ContentItem> items) {
        if (items != null && items.size() > 0) {
            headerMediaInfo = true;
            commentItems.add(0, null);
            mediaInfoList.addAll(items);

            notifyItemChanged(0);
        }
    }

    public void setHeaderMediaFavorites(List<ContentItem> items) {
        if (mediaInfoList != null && mediaInfoList.size() == 0 && items != null) {
            headerMediaFavorites = true;
            commentItems.add(0, null);
            favoritesList.addAll(items);

            notifyItemChanged(0);

        } else if (mediaInfoList != null && mediaInfoList.size() > 0 && items != null) {
            headerMediaFavorites = true;
            commentItems.add(1, null);
            favoritesList.addAll(items);

            notifyItemChanged(1);
        }
    }

    private void fillFavoriteContainer(LinearLayout container, List<ContentItem> items) {

        if (items == null || items.size() == 0) {
            container.removeAllViews();
            return;
        }

        LinearLayout itemView;
        FavoriteItemHolder favoriteItemHolder;

        int mDataSize = items.size();
        int mChildViewSize = container.getChildCount();

        if (mChildViewSize < mDataSize) {
            while (mChildViewSize < mDataSize) {
                favoriteItemHolder = new FavoriteItemHolder();

                itemView = (LinearLayout) inflater.inflate(R.layout.item_guess_content, null);
                favoriteItemHolder.itemLayout = (LinearLayout) itemView.findViewById(R.id.item_guess_content_layout);
                favoriteItemHolder.cardView = (CardView) itemView.findViewById(R.id.guess_content_card);
                favoriteItemHolder.cardView.setCardBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(context,
                        R.attr.about_card, R.color.about_card));
                favoriteItemHolder.simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.guess_content_drawee_view);
                favoriteItemHolder.nameView = (TextView) itemView.findViewById(R.id.guess_content_name);
                favoriteItemHolder.desView = (TextView) itemView.findViewById(R.id.guess_content_des);

                itemView.setTag(favoriteItemHolder);

                container.addView(itemView);

                mChildViewSize++;

            }
        } else {
            while (mChildViewSize > mDataSize) {
                container.removeViewAt(0);
                mChildViewSize--;
            }
        }

        //set item image
        for (int i = 0; i < mDataSize; i++) {
            final ContentItem item = items.get(i);

            itemView = (LinearLayout) container.getChildAt(i);
            favoriteItemHolder = (FavoriteItemHolder) itemView.getTag();
            favoriteItemHolder.simpleDraweeView.setController(getDraweeController(item.getThumbnailPath()));
            favoriteItemHolder.nameView.setText(item.getName());
            favoriteItemHolder.desView.setText(item.getDescription());

            favoriteItemHolder.simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    L.i("click item " + item.getName());
                    if (item != null) {
                        ((MediaDetailActivity) context).refreshContent(item);
                    }
                }
            });

            favoriteItemHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    L.i("click item " + item.getName());
                    if (item != null) {
                        ((MediaDetailActivity) context).refreshContent(item);
                    }
                }
            });
        }

    }

    public void setCommentItems(List<CommentItem> items) {
        if (items != null && items.size() > 0) {
            commentItems.addAll(items);

            notifyDataSetChanged();
        }
    }

    public void addCommentItem(CommentItem item) {
        if (item != null && !headerMediaInfo && !headerMediaFavorites) {
            commentItems.add(0, item);

            notifyDataSetChanged();
            return;
        }

        if (item != null && headerMediaInfo && !headerMediaFavorites) {
            commentItems.add(1, item);

            notifyDataSetChanged();
            return;
        }

        if (item != null && headerMediaInfo && headerMediaFavorites) {
            commentItems.add(2, item);

            notifyDataSetChanged();
            return;
        }
    }

    public List<CommentItem> getCommentItems() {
        return commentItems;
    }

    public void deleteContents() {
        if (mediaInfoList != null) {
            mediaInfoList.clear();
            headerMediaInfo = false;
        }

        if (favoritesList != null) {
            favoritesList.clear();
            headerMediaFavorites = false;
        }

        if (commentItems != null) {
            commentItems.clear();
        }

        notifyDataSetChanged();
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

    public class MediaInfoHolder extends RecyclerView.ViewHolder {

        LinearLayout infoTitleLayout;
        TextView nameView;
        ImageView arrowView;
        TextView desView;
        TextView goodCountView;
        TextView badCountView;

        public MediaInfoHolder(View itemView) {
            super(itemView);

            infoTitleLayout = (LinearLayout) itemView.findViewById(R.id.info_title_layout);
            infoTitleLayout.setTag(false); // default: not clicked
            nameView = (TextView) itemView.findViewById(R.id.name_media);
            arrowView = (ImageView) itemView.findViewById(R.id.info_arrow);
            desView = (TextView) itemView.findViewById(R.id.description_view);
            desView.setVisibility(View.GONE);
            goodCountView = (TextView) itemView.findViewById(R.id.good_count);
            badCountView = (TextView) itemView.findViewById(R.id.bad_count);
        }
    }

    public class MediaFavoriteHolder extends RecyclerView.ViewHolder {

        TextView favoriteTitleView;
        LinearLayout favoriteContainer;
        EditText editView;
        ImageView sendView;


        public MediaFavoriteHolder(View itemView) {
            super(itemView);

            favoriteTitleView = (TextView) itemView.findViewById(R.id.favorite_title);
            favoriteTitleView.setVisibility(View.GONE);

            favoriteContainer = (LinearLayout) itemView.findViewById(R.id.favorite_container);

            editView = (EditText) itemView.findViewById(R.id.comment_edit);
            sendView = (ImageView) itemView.findViewById(R.id.comment_send);
        }
    }

    public class FavoriteItemHolder {
        LinearLayout itemLayout;
        CardView cardView;
        SimpleDraweeView simpleDraweeView;
        TextView nameView;
        TextView desView;
    }

    public class MediaCommentHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView simpleDraweeView;
        TextView nameView;
        TextView timeView;
        TextView commentView;
        TextView floorView;
        TextView deviceView;
        ImageView favoriteView;
        TextView favoriteCount;
        ImageView replyView;

        public MediaCommentHolder(View itemView) {
            super(itemView);

            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.avatar_view);
            nameView = (TextView) itemView.findViewById(R.id.account_name);
            timeView = (TextView) itemView.findViewById(R.id.publish_time);
            commentView = (TextView) itemView.findViewById(R.id.comment_view);
            floorView = (TextView) itemView.findViewById(R.id.floor_position);
            deviceView = (TextView) itemView.findViewById(R.id.device_model);
            favoriteView = (ImageView) itemView.findViewById(R.id.favorite_btn);
            favoriteCount = (TextView) itemView.findViewById(R.id.favorite_count);
            replyView = (ImageView) itemView.findViewById(R.id.reply_btn);
        }
    }
}

