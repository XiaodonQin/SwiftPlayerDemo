/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/17.
 */
package sg.com.conversant.swiftplayer.views;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.attachment.ImageUploadHelper;
import com.zendesk.sdk.util.UiUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import sg.com.conversant.swiftplayer.R;

/**
 * TODO
 *
 * @author Xiaodong
 */
public class AttachmentRecyclerView extends RecyclerView {
    private static final String LOG_TAG = AttachmentRecyclerView.class.getSimpleName();
    private List<AttachmentRecyclerView.AttachmentContainer> mAttachmentContainerList = new ArrayList();
    private View mParent = this;
    private AttachmentRecyclerView.AttachmentContainerListener mAttachmentContainerListener;
    private AddAttachmentListener mAddAttachmentListener;

    private View mAddAttachmentView;

    public AttachmentRecyclerView(Context context) {
        super(context);
    }

    public AttachmentRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AttachmentRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAttachmentContainerListener(AttachmentRecyclerView.AttachmentContainerListener attachmentContainerListener) {
        this.mAttachmentContainerListener = attachmentContainerListener;
    }

    public void setOnAddAttachmentListener(AddAttachmentListener addAttachmentListener) {
        this.mAddAttachmentListener = addAttachmentListener;
    }

    public void setParent(View parent) {
        this.mParent = parent;
    }

    public void setState(ImageUploadHelper imageUploadHelper) {
        if(imageUploadHelper == null) {
            Logger.d(LOG_TAG, "Please provide a non null ImageUploadHelper", new Object[0]);
        } else {
            HashMap stateMap = imageUploadHelper.getRecentState();
            List uploaded;
            Iterator var4;
            File file;
            if(stateMap.containsKey(AttachmentRecyclerView.AttachmentState.UPLOADING)) {
                uploaded = (List)stateMap.get(AttachmentRecyclerView.AttachmentState.UPLOADING);
                var4 = uploaded.iterator();

                while(var4.hasNext()) {
                    file = (File)var4.next();
                    this.addAttachment(file);
                }
            }

            if(stateMap.containsKey(AttachmentRecyclerView.AttachmentState.UPLOADED)) {
                uploaded = (List)stateMap.get(AttachmentRecyclerView.AttachmentState.UPLOADED);
                var4 = uploaded.iterator();

                while(var4.hasNext()) {
                    file = (File)var4.next();
                    this.addAttachment(file);
                    this.setAttachmentUploaded(file);
                }
            }

        }
    }

    public void setAddAttachmentView() {
        mAddAttachmentView = LayoutInflater.from(mParent.getContext()).inflate(R.layout.item_attachment, this, false);
        this.addView(mAddAttachmentView);

        if (mAddAttachmentView != null) {
            mAddAttachmentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAddAttachmentListener != null) {
                        mAddAttachmentListener.addAttachment();
                    }
                }
            });
        }
    }

    public View getAddAttachmentView() {
        if (mAddAttachmentView != null) {
            return mAddAttachmentView;
        } else {
            return null;
        }
    }

    public void addAttachment(File file) {
        UiUtils.setVisibility(this.mParent, 0);
        AttachmentRecyclerView.AttachmentContainer attachmentContainer = new AttachmentRecyclerView.AttachmentContainer(this.getContext(), file, this);
        attachmentContainer.setId(file.hashCode());
        this.mAttachmentContainerList.add(attachmentContainer);
        this.addView(attachmentContainer, this.getChildCount() - 1);
    }

    public void setAttachmentUploaded(File file) {
        this.setAttachmentState(file, AttachmentRecyclerView.AttachmentState.UPLOADED);
    }

    public void setAttachmentState(File file, AttachmentRecyclerView.AttachmentState attachmentState) {
        Iterator var3 = this.mAttachmentContainerList.iterator();

        while(var3.hasNext()) {
            AttachmentRecyclerView.AttachmentContainer attachmentContainer = (AttachmentRecyclerView.AttachmentContainer)var3.next();
            if(attachmentContainer.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                attachmentContainer.setState(attachmentState);
            }
        }

    }

    public void removeAttachment(File file) {
        ArrayList newAttachmentContainer = new ArrayList();
        Iterator childCount = this.mAttachmentContainerList.iterator();

        while(childCount.hasNext()) {
            AttachmentRecyclerView.AttachmentContainer i = (AttachmentRecyclerView.AttachmentContainer)childCount.next();
            if(!i.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                newAttachmentContainer.add(i);
            }
        }

        this.mAttachmentContainerList = newAttachmentContainer;
        int var6 = this.getChildCount();

        for(int var7 = 0; var7 < var6; ++var7) {
            View childAt = this.getChildAt(var7);
            if(childAt instanceof AttachmentRecyclerView.AttachmentContainer && ((AttachmentRecyclerView.AttachmentContainer)childAt).getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                this.removeViewAt(var7);
                break;
            }
        }

        if(this.getChildCount() < 1) {
            UiUtils.setVisibility(this.mParent, 8);
        }

    }

    public synchronized void removeAttachmentAndNotify(File file) {
        this.removeAttachment(file);
        if(this.mAttachmentContainerListener != null) {
            this.mAttachmentContainerListener.attachmentRemoved(file);
        }

    }

    public void setAttachmentsDeletable(boolean deletable) {
        int childCount = this.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            View childAt = this.getChildAt(i);
            if(childAt instanceof AttachmentRecyclerView.AttachmentContainer) {
                AttachmentRecyclerView.AttachmentContainer attachment = (AttachmentRecyclerView.AttachmentContainer)childAt;
                AttachmentRecyclerView.AttachmentState attachmentState = attachment.getAttachmentState();
                if(deletable && attachmentState == AttachmentRecyclerView.AttachmentState.DISABLE) {
                    attachment.setState(AttachmentRecyclerView.AttachmentState.UPLOADED);
                } else if(!deletable && attachmentState == AttachmentRecyclerView.AttachmentState.UPLOADED) {
                    attachment.setState(AttachmentRecyclerView.AttachmentState.DISABLE);
                }
            }
        }

    }

    public void reset() {
        UiUtils.setVisibility(this.mParent, 8);
        this.removeAllViews();
    }

    class AttachmentContainer extends FrameLayout {
        private File mFile;
        private AttachmentRecyclerView.AttachmentState mAttachmentState;
        private SimpleDraweeView mImageView;
        private ProgressBar mProgressBar;
        private Button mDeleteButton;

        public AttachmentContainer(Context context, final File file, final AttachmentRecyclerView parent) {
            super(context);
            this.mFile = file;
            this.mAttachmentState = AttachmentRecyclerView.AttachmentState.UPLOADING;
            View v = LayoutInflater.from(context).inflate(R.layout.item_attachment, this, false);
            this.mImageView = (SimpleDraweeView)v.findViewById(R.id.attachment_image);
            this.mDeleteButton = (Button)v.findViewById(R.id.attachment_delete);
            this.mProgressBar = (ProgressBar)v.findViewById(R.id.attachment_progress);
            this.mProgressBar.setIndeterminate(true);
            this.mDeleteButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    parent.removeAttachmentAndNotify(file);
                }
            });
            this.attachImage(this.mFile, this.mImageView, parent, context);
            this.addView(v);
        }

        public void setState(AttachmentRecyclerView.AttachmentState attachmentState) {
            this.mAttachmentState = attachmentState;
            switch(attachmentState.ordinal()) {
                case 1:
                    UiUtils.setVisibility(this.mDeleteButton, 8);
                    UiUtils.setVisibility(this.mProgressBar, 0);
                    break;
                case 2:
                    UiUtils.setVisibility(this.mDeleteButton, 0);
                    UiUtils.setVisibility(this.mProgressBar, 8);
                    break;
                case 3:
                    UiUtils.setVisibility(this.mDeleteButton, 8);
                    UiUtils.setVisibility(this.mProgressBar, 8);
            }

        }

        public AttachmentRecyclerView.AttachmentState getAttachmentState() {
            return this.mAttachmentState;
        }

        public File getFile() {
            return this.mFile;
        }

        private void attachImage(File file, SimpleDraweeView imageView, ViewGroup parent, Context context) {
            this.loadFileIntoImageView(imageView, file, context);
            this.setState(this.mAttachmentState);
        }

        private void loadFileIntoImageView(SimpleDraweeView mDraweeView, File file, Context context) {
            Uri uri = Uri.fromFile(file);
            int width = context.getResources().getDimensionPixelSize(R.dimen.attachment_size);
            int height = width;
            ImageRequest request = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();

            PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder();
            builder.setImageRequest(request).setOldController(mDraweeView.getController());
            DraweeController controller = builder.build();
            try {
                mDraweeView.setController(controller);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface AttachmentContainerListener {
        void attachmentRemoved(File var1);
    }

    public interface AddAttachmentListener {
        void addAttachment();
    }

    public static enum AttachmentState {
        UPLOADING,
        UPLOADED,
        DISABLE;

        private AttachmentState() {
        }
    }
}
