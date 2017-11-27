package sg.com.conversant.swiftplayer.feedback;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zendesk.belvedere.Belvedere;
import com.zendesk.belvedere.BelvedereLogger;

import sg.com.conversant.swiftplayer.utils.L;


/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/23.
 */

public enum CustomBelvedereProvider {
    INSTANCE;

    private Belvedere mBelvedere;

    private CustomBelvedereProvider() {
    }

    public Belvedere getBelvedere(Context context) {
        if(this.mBelvedere == null) {
            Class var2 = CustomBelvedereProvider.class;
            synchronized(CustomBelvedereProvider.class) {
                this.mBelvedere = Belvedere.from(context).withAllowMultiple(true).withContentType("image/*").withCustomLogger(new CustomBelvedereProvider.BelvedereZendeskLogger()).withDebug(L.isWriteLogs()).build();
            }
        }

        return this.mBelvedere;
    }

    static class BelvedereZendeskLogger implements BelvedereLogger {
        boolean logging = false;

        BelvedereZendeskLogger() {
        }

        public void d(@NonNull String tag, @NonNull String msg) {
            if(this.logging) {
                L.d(tag + " " + msg + " " + new Object[0]);
            }

        }

        public void w(@NonNull String tag, @NonNull String msg) {
            if(this.logging) {
                L.d(tag + " " + msg + " " + new Object[0]);
            }

        }

        public void e(@NonNull String tag, @NonNull String msg) {
            if(this.logging) {
                L.d(tag + " " + msg + " " + new Object[0]);
            }

        }

        public void e(@NonNull String tag, @NonNull String msg, @NonNull Throwable throwable) {
            if(this.logging) {
                L.d(tag + " " + msg + " " + new Object[0]);
            }

        }

        public void setLoggable(boolean b) {
            this.logging = b;
        }
    }
}