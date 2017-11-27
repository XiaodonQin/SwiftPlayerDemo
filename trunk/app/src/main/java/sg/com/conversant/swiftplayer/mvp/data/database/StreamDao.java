package sg.com.conversant.swiftplayer.mvp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/9.
 */

@Dao
public interface StreamDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(StreamDetail detail);

    @Query("SELECT * FROM stream_detail_table WHERE id = :id")
    StreamDetail queryStreamDetailById(long id);
}
