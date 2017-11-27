/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2016/3/25.
 */
package sg.com.conversant.swiftplayer.sdk.module;

import java.util.List;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class Content {

    /**
     * 0, recommends
     * 1, series
     * 2, movie
     * 3, live
     * 4, music
     */
    private int tag;

    private List<ContentItem> contentItems;

    public Content() {
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public List<ContentItem> getContentItems() {
        return contentItems;
    }

    public void setContentItems(List<ContentItem> contentItems) {
        this.contentItems = contentItems;
    }
}
