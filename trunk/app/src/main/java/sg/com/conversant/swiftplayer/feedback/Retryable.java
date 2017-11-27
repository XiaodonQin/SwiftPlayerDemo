package sg.com.conversant.swiftplayer.feedback;

import android.view.View;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/23.
 */

public interface Retryable {
    void onRetryAvailable(String var1, View.OnClickListener var2);

    void onRetryUnavailable();
}
