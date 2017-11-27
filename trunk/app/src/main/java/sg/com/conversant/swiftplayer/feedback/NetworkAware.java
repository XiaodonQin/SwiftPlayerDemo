package sg.com.conversant.swiftplayer.feedback;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/23.
 */

public interface NetworkAware {
    void onNetworkAvailable();

    void onNetworkUnavailable();
}
