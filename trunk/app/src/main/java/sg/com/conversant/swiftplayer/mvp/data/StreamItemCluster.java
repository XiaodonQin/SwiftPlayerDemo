package sg.com.conversant.swiftplayer.mvp.data;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/2.
 */

public class StreamItemCluster implements Serializable {

    private int type;

    private List<StreamItem> streamItems;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<StreamItem> getStreamItems() {
        return streamItems;
    }

    public void setStreamItems(List<StreamItem> streamItems) {
        this.streamItems = streamItems;
    }
}
