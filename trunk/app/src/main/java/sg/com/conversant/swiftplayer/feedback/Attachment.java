package sg.com.conversant.swiftplayer.feedback;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/25.
 */

public class Attachment {
    private Long id;
    private String url;
    private String fileName;
    private String contentUrl;
    private String mappedContentUrl;
    private String contentType;
    private Long size;
    private List<Attachment> thumbnails;

    public Attachment() {
    }

    @Nullable
    public String getUrl() {
        return this.url;
    }

    @Nullable
    public Long getId() {
        return this.id;
    }

    @Nullable
    public String getFileName() {
        return this.fileName;
    }

    @Nullable
    public String getContentUrl() {
        return this.contentUrl;
    }

    @Nullable
    public String getContentType() {
        return this.contentType;
    }

    public Long getSize() {
        return this.size;
    }

    @NonNull
    public List<Attachment> getThumbnails() {
        return CollectionUtils.copyOf(this.thumbnails);
    }
}
