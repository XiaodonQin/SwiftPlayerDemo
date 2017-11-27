package sg.com.conversant.swiftplayer.mvp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsDetail;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/13.
 */

@Dao
public interface MainContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StreamItem> items);

    @Query("SELECT * FROM stream_table WHERE type = :type ORDER BY id ASC")
    List<StreamItem> queryAllStreamByType(int type);

}
