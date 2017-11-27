/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/4/29.
 */
package sg.com.conversant.swiftplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.sdk.module.Library;
import sg.com.conversant.swiftplayer.utils.UIUtils;
import sg.com.conversant.swiftplayer.views.RippleForegroundListener;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class LibsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private RippleForegroundListener rippleForegroundListener = new RippleForegroundListener(R.id.rippleForegroundListenerView);

    private Activity mActivity;

    private List<Library> libs = new ArrayList<>();

    public LibsRecyclerViewAdapter(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_opensource,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        final Context ctx = viewHolder.itemView.getContext();
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;

            final Library library = getItem(position);

            //Set texts
            holder.libraryName.setText(library.getLibraryName());
            holder.libraryCreator.setText(library.getAuthor());
            if (TextUtils.isEmpty(library.getLibraryDescription())) {
                holder.libraryDescription.setText(library.getLibraryDescription());
            } else {
                holder.libraryDescription.setText(Html.fromHtml(library.getLibraryDescription()));
            }

            //Set License or Version Text
            if (TextUtils.isEmpty(library.getLibraryVersion()) && library.getLicense() != null &&
                    TextUtils.isEmpty(library.getLicense().getLicenseName())) {
                holder.libraryBottomDivider.setVisibility(View.GONE);
                holder.libraryBottomContainer.setVisibility(View.GONE);
            } else {
                holder.libraryBottomDivider.setVisibility(View.VISIBLE);
                holder.libraryBottomContainer.setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(library.getLibraryVersion())) {
                    holder.libraryVersion.setText(library.getLibraryVersion());
                } else {
                    holder.libraryVersion.setText("");
                }
                if (library.getLicense() != null && !TextUtils.isEmpty(library.getLicense().getLicenseName())) {
                    holder.libraryLicense.setText(library.getLicense().getLicenseName());
                } else {
                    holder.libraryLicense.setText("");
                }
            }

            //Define onClickListener
            if (!TextUtils.isEmpty(library.getAuthorWebsite())) {
                holder.libraryCreator.setOnTouchListener(rippleForegroundListener);
                holder.libraryCreator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAuthorWebsite(ctx, library.getAuthorWebsite());
                    }
                });
            } else {
                holder.libraryCreator.setOnTouchListener(null);
                holder.libraryCreator.setOnClickListener(null);
            }

            if (!TextUtils.isEmpty(library.getLibraryWebsite()) || !TextUtils.isEmpty(library.getRepositoryLink())) {
                holder.libraryDescription.setOnTouchListener(rippleForegroundListener);
                holder.libraryDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openLibraryWebsite(ctx, library.getLibraryWebsite() != null ? library.getLibraryWebsite() : library.getRepositoryLink());

                    }
                });
            } else {
                holder.libraryDescription.setOnTouchListener(null);
                holder.libraryDescription.setOnClickListener(null);
            }

            if (library.getLicense() != null && !TextUtils.isEmpty((library.getLicense().getLicenseWebsite()))) {
                holder.libraryBottomContainer.setOnTouchListener(rippleForegroundListener);
                holder.libraryBottomContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openLicense(ctx, library);

                    }
                });
            } else {
                holder.libraryBottomContainer.setOnTouchListener(null);
                holder.libraryBottomContainer.setOnClickListener(null);
            }
        }
    }

    /**
     * helper method to open the author website
     *
     * @param ctx
     * @param authorWebsite
     */
    private void openAuthorWebsite(Context ctx, String authorWebsite) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authorWebsite));
            ctx.startActivity(browserIntent);
        } catch (Exception ex) {
        }
    }

    /**
     * helper method to open the library website
     *
     * @param ctx
     * @param libraryWebsite
     */
    private void openLibraryWebsite(Context ctx, String libraryWebsite) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(libraryWebsite));
            ctx.startActivity(browserIntent);
        } catch (Exception ex) {
        }
    }

    /**
     * helper method to open the license dialog / or website
     *
     * @param ctx
     * @param library
     */
    private void openLicense(Context ctx, Library library) {
        try {
            if (!TextUtils.isEmpty(library.getLicense().getLicenseDescription())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setMessage(Html.fromHtml(library.getLicense().getLicenseDescription()));
                builder.create().show();
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(library.getLicense().getLicenseWebsite()));
                ctx.startActivity(browserIntent);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return libs == null ? 0 : libs.size();
    }

    public Library getItem(int pos) {
        return libs.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //you should not do this if you are not sure if the position of the items is also their identifier
        return pos;
    }

    public void setLibs(List<Library> libs) {
        this.libs = libs;
        this.notifyItemRangeInserted(0, libs.size() - 1);
    }

    public void addLibs(List<Library> libs) {
        if (libs != null && libs.size() > 0) {
            int size = this.libs.size() - 1;
            this.libs.addAll(libs);
            this.notifyItemRangeInserted(size, size + libs.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;

        TextView libraryName;
        TextView libraryCreator;
        View libraryDescriptionDivider;
        TextView libraryDescription;

        View libraryBottomDivider;
        View libraryBottomContainer;

        TextView libraryVersion;
        TextView libraryLicense;

        public ViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView;
            card.setCardBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_card, R.color.about_libraries_card));

            libraryName = (TextView) itemView.findViewById(R.id.libraryName);
            libraryName.setTextColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_title_openSource, R.color.about_libraries_title_openSource));
            libraryCreator = (TextView) itemView.findViewById(R.id.libraryCreator);
            libraryCreator.setTextColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_text_openSource, R.color.about_libraries_text_openSource));
            libraryDescriptionDivider = itemView.findViewById(R.id.libraryDescriptionDivider);
            libraryDescriptionDivider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_dividerLight_openSource, R.color.about_libraries_dividerLight_openSource));
            libraryDescription = (TextView) itemView.findViewById(R.id.libraryDescription);
            libraryDescription.setTextColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_text_openSource, R.color.about_libraries_text_openSource));

            libraryBottomDivider = itemView.findViewById(R.id.libraryBottomDivider);
            libraryBottomDivider.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_dividerLight_openSource, R.color.about_libraries_dividerLight_openSource));
            libraryBottomContainer = itemView.findViewById(R.id.libraryBottomContainer);

            libraryVersion = (TextView) itemView.findViewById(R.id.libraryVersion);
            libraryVersion.setTextColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_text_openSource, R.color.about_libraries_text_openSource));
            libraryLicense = (TextView) itemView.findViewById(R.id.libraryLicense);
            libraryLicense.setTextColor(UIUtils.getThemeColorFromAttrOrRes(itemView.getContext(), R.attr.about_libraries_text_openSource, R.color.about_libraries_text_openSource));

        }

    }
}

