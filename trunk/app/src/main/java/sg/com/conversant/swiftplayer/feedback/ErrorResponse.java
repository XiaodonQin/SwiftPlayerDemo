package sg.com.conversant.swiftplayer.feedback;


import java.util.List;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/23.
 */

public interface ErrorResponse {
    int NON_HTTP_ERROR = -1;

    /** @deprecated */
    @Deprecated
    boolean isNetworkError();

    /** @deprecated */
    @Deprecated
    boolean isConversionError();

    boolean isHTTPError();

    String getReason();

    int getStatus();

    String getUrl();

    String getResponseBody();

    String getResponseBodyType();

    List<Header> getResponseHeaders();
}
