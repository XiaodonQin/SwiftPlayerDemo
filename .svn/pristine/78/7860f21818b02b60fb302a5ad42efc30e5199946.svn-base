package sg.com.conversant.swiftplayer.feedback;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import com.zendesk.logger.Logger;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/23.
 */

public class NetworkUtils {
    private static final String LOG_TAG = com.zendesk.sdk.util.NetworkUtils.class.getSimpleName();

    private NetworkUtils() {
    }

    public static boolean isConnected(Context context) {
        boolean isConnected = false;
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        if(networkInfo != null) {
            isConnected = networkInfo.isConnected();
        }

        return isConnected;
    }

    public static boolean isMobile(Context context) {
        boolean isMobile = false;
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        if(networkInfo != null) {
            isMobile = 0 == networkInfo.getType();
        }

        return isMobile;
    }

    @Nullable
    public static ConnectivityManager getConnectivityManager(Context context) {
        if(context == null) {
            Logger.w(LOG_TAG, "Context is null. Cannot get ConnectivityManager", new Object[0]);
            return null;
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
            if(connectivityManager == null) {
                Logger.w(LOG_TAG, "Connectivity manager is null", new Object[0]);
            }

            return connectivityManager;
        }
    }

    @Nullable
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        NetworkInfo networkInfo = null;
        ConnectivityManager connectivityManager = getConnectivityManager(context);
        if(connectivityManager != null && context != null) {
            if(0 == context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE")) {
                Logger.i(LOG_TAG, "Getting active network information", new Object[0]);
                networkInfo = connectivityManager.getActiveNetworkInfo();
            } else {
                Logger.w(LOG_TAG, "Will not return if network is available because we do not have the permission to do so: ACCESS_NETWORK_STATE", new Object[0]);
            }
        }

        return networkInfo;
    }
}
