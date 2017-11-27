/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/5/9.
 */
package sg.com.conversant.swiftplayer.utils;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import sg.com.conversant.swiftplayer.sdk.module.SearchRecord;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class DBHelper {

    public void addSearchRecord(String name) {
        SearchRecord searchRecord = new SearchRecord(name);
        SugarRecord.save(searchRecord);
    }

    public void addSearchRecords(List<String> names) {
        List<SearchRecord> searchRecords = new ArrayList<>();
        for (String name : names) {
            searchRecords.add(new SearchRecord(name));
        }
        SugarRecord.saveInTx(searchRecords);
    }

    public void deleteSearchRecord(String name) {
        List<SearchRecord> records = SugarRecord.find(SearchRecord.class, "name = ?", name);
        SugarRecord.deleteInTx(records);
    }

    public void deleteAllSearchRecords() {
        SugarRecord.deleteAll(SearchRecord.class);
    }

    public List<SearchRecord> getSearchRecords() {
        return SugarRecord.listAll(SearchRecord.class);
    }

    public boolean isSearchRecordExist(String name) {
        List<SearchRecord> records = SugarRecord.find(SearchRecord.class, "name = ?", name);

        if (records == null || records.size() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
