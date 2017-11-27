package sg.com.conversant.swiftplayer.mvp.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsDetail;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/13.
 */

@Dao
public interface ZhihuDailyNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ZhihuDailyNews> news);

    @Query("SELECT * FROM zhihu_news_table WHERE timestamp BETWEEN (:timestamp - 24*60*60*1000 + 1) AND :timestamp ORDER BY ga_prefix DESC")
    List<ZhihuDailyNews> queryAllByDate(long timestamp);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateZhihuNews(ZhihuDailyNews item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ZhihuDailyNewsDetail detail);

    @Query("SELECT * FROM zhihu_news_detail WHERE id = :id")
    ZhihuDailyNewsDetail queryById(long id);
}
