package sg.com.conversant.swiftplayer.feedback;

import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/25.
 */

public class UploadResponse {
    private String token;
    private Date expiresAt;
    private Attachment attachment;

    public UploadResponse() {
    }

    @Nullable
    public String getToken() {
        return this.token;
    }

    @Nullable
    public Date getExpiresAt() {
        return this.expiresAt == null?null:new Date(this.expiresAt.getTime());
    }

    @Nullable
    public Attachment getAttachment() {
        return this.attachment;
    }
}
