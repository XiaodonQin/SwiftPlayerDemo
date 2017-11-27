package sg.com.conversant.swiftplayer.mvp.data;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/16.
 */

public class StreamDetailCluster implements Serializable {

    private int type;
    private StreamItem streamItem;
    private StreamDetail streamDetail;
    private StreamComment streamComment;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public StreamItem getStreamItem() {
        return streamItem;
    }

    public void setStreamItem(StreamItem streamItem) {
        this.streamItem = streamItem;
    }

    public StreamDetail getStreamDetail() {
        return streamDetail;
    }

    public void setStreamDetail(StreamDetail streamDetail) {
        this.streamDetail = streamDetail;
    }

    public StreamComment getStreamComment() {
        return streamComment;
    }

    public void setStreamComment(StreamComment streamComment) {
        this.streamComment = streamComment;
    }
}
