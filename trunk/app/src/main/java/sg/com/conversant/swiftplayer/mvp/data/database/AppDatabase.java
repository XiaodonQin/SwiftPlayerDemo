package sg.com.conversant.swiftplayer.mvp.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import sg.com.conversant.swiftplayer.mvp.data.StreamDetail;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNews;
import sg.com.conversant.swiftplayer.mvp.data.ZhihuDailyNewsDetail;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/13.
 */

@Database(entities = {ZhihuDailyNews.class, ZhihuDailyNewsDetail.class,
        StreamItem.class, StreamDetail.class},
        version = 6)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "swiftplayer-db";

    public abstract ZhihuDailyNewsDao zhihuDailyNewsDao();

    public abstract MainContentDao mainContentDao();

    public abstract StreamDao streamDao();

}
