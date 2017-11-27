package sg.com.conversant.swiftplayer.mvp.data.database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;

import java.util.concurrent.atomic.AtomicBoolean;

import sg.com.conversant.swiftplayer.utils.L;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/9/14.
 */

public class DatabaseCreator {
    private static String LOG_TAG = DatabaseCreator.class.getSimpleName();

    @Nullable
    private static DatabaseCreator INSTANCE;

    private AppDatabase mDb;

    private AtomicBoolean mIsInitializing = new AtomicBoolean(true);
    private AtomicBoolean mIsDbCreated = new AtomicBoolean(false);


    //for singleton instantiation
    private static final Object LOCK = new Object();

    private DatabaseCreator() {
    }

    public synchronized static DatabaseCreator getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new DatabaseCreator();
                }
            }
        }

        return INSTANCE;
    }

    public void createDb(@NonNull Context context) {
        L.i(LOG_TAG + " createDb");
        L.i(LOG_TAG + " current thread: " + Thread.currentThread().getName());

        if (!mIsInitializing.compareAndSet(true, false)) {
            return;
        }

        new AsyncTask<Context, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                mIsDbCreated.set(true);
            }

            @Override
            protected Void doInBackground(Context... params) {
                Context ctx = params[0].getApplicationContext();
                mDb = Room.databaseBuilder(ctx, AppDatabase.class, AppDatabase.DB_NAME).build();

                return null;
            }
        }.execute(context);
    }

    @Nullable
    public AppDatabase getDatabase() {
        return mDb;
    }

    public boolean isDbCreated() {
        return mIsDbCreated.get();
    }
}
