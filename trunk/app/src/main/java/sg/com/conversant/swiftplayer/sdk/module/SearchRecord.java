/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/5/9.
 */
package sg.com.conversant.swiftplayer.sdk.module;

import com.orm.dsl.Table;

/**
 * TODO
 *
 * @author Xiaodong

 */
@Table
public class SearchRecord {

    private String name;

    public SearchRecord() {

    }

    public SearchRecord(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
