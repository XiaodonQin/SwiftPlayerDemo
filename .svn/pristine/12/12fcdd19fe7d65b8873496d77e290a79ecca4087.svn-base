package sg.com.conversant.swiftplayer.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.feedback.AnimationListenerAdapter;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.feedback.NetworkAware;
import sg.com.conversant.swiftplayer.feedback.NetworkUtils;
import sg.com.conversant.swiftplayer.feedback.Retryable;
import sg.com.conversant.swiftplayer.feedback.StringUtils;


/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/23.
 */

public abstract class NetworkAwareActionbarActivity extends AppCompatActivity implements NetworkAware, Retryable {
    private static final String LOG_TAG = NetworkAwareActionbarActivity.class.getSimpleName();
    private BroadcastReceiver mBroadcastReceiver;
    private ConnectivityManager.NetworkCallback mNetworkCallback;
    private View mNoNetworkView;
    private View mRetryView;
    private boolean mNetworkPreviouslyUnavailable;
    protected boolean mNetworkAvailable;

    public NetworkAwareActionbarActivity() {
    }

    public void onNetworkAvailable() {
        L.d(LOG_TAG + " " + "Network is available.", new Object[0]);
        this.mNetworkAvailable = true;
        if(this.findNoNetworkView() == null) {
            L.w(LOG_TAG + " " + "The no network view is null, nothing to do.", new Object[0]);
        } else if(!this.mNetworkPreviouslyUnavailable) {
            L.d(LOG_TAG + " " + "Network was not previously unavailable, no need to perform animation", new Object[0]);
        } else {
            this.mNetworkPreviouslyUnavailable = false;
            Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.info_slide_out);
            slideOut.setAnimationListener(new AnimationListenerAdapter() {
                public void onAnimationEnd(Animation animation) {
                    NetworkAwareActionbarActivity.this.findNoNetworkView().setVisibility(View.GONE);
                }
            });
            this.findNoNetworkView().startAnimation(slideOut);
        }
    }

    public void onNetworkUnavailable() {
        L.d(LOG_TAG + " " + "Network is unavailable.", new Object[0]);
        this.mNetworkAvailable = false;
        this.mNetworkPreviouslyUnavailable = true;
        if(this.findRetryView() != null) {
            L.d(LOG_TAG + " " + "No network: ensuring that the retry view is gone.", new Object[0]);
            this.findRetryView().setVisibility(View.GONE);
        }

        if(this.findNoNetworkView() == null) {
            L.w(LOG_TAG + " " + "No network: cannot show view because it is missing.", new Object[0]);
        } else {
            Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.info_slide_in);
            slideIn.setAnimationListener(new AnimationListenerAdapter() {
                public void onAnimationStart(Animation animation) {
                    NetworkAwareActionbarActivity.this.findNoNetworkView().setVisibility(View.VISIBLE);
                }
            });
            this.findNoNetworkView().startAnimation(slideIn);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mNetworkAvailable = NetworkUtils.isConnected(this);
    }

    protected void onResume() {
        super.onResume();
        this.registerForNetworkCallbacks();
    }

    protected void onPause() {
        super.onPause();
        this.unregisterForNetworkCallbacks();
    }

    public void onRetryAvailable(String message, View.OnClickListener retryAction) {
        this.setRetryViewState(true, message, retryAction);
    }

    public void onRetryUnavailable() {
        this.setRetryViewState(false, (String)null, (View.OnClickListener)null);
    }

    @TargetApi(21)
    private void registerForNetworkCallbacks() {
        if(Build.VERSION.SDK_INT < 21) {
            L.d(LOG_TAG + " " + "Adding pre-Lollipop network callbacks...", new Object[0]);
            this.mBroadcastReceiver = new NetworkAvailabilityBroadcastReceiver();
            this.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        } else {
            L.d(LOG_TAG + " " + "Adding Lollipop network callbacks...", new Object[0]);
            ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
            final Handler handler = new Handler(Looper.myLooper());
            this.mNetworkCallback = new ConnectivityManager.NetworkCallback() {
                public void onAvailable(Network network) {
                    handler.post(new Runnable() {
                        public void run() {
                            NetworkAwareActionbarActivity.this.onNetworkAvailable();
                        }
                    });
                }

                public void onLost(Network network) {
                    handler.post(new Runnable() {
                        public void run() {
                            NetworkAwareActionbarActivity.this.onNetworkUnavailable();
                        }
                    });
                }
            };
            connectivityManager.registerNetworkCallback((new NetworkRequest.Builder()).build(), this.mNetworkCallback);
        }

    }

    @TargetApi(21)
    private void unregisterForNetworkCallbacks() {
        if(this.mBroadcastReceiver != null) {
            this.unregisterReceiver(this.mBroadcastReceiver);
        }

        if(Build.VERSION.SDK_INT >= 21 && this.mNetworkCallback != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
        }

    }

    private void setRetryViewState(boolean shouldShow, String message, View.OnClickListener retryAction) {
        if(!this.mNetworkAvailable) {
            L.e(LOG_TAG + " " + "Network is not available, will not alter retry view state", new Object[0]);
        } else if(this.findRetryView() == null) {
            L.e(LOG_TAG + " " + "No retry view was found, will not alter view state", new Object[0]);
        } else {
            if(shouldShow) {
                View slideOut = this.findViewById(R.id.retry_view_button);
                if(slideOut != null) {
                    slideOut.setOnClickListener(retryAction);
                }

                TextView retryText = (TextView)this.findViewById(R.id.retry_view_text);
                if(retryText != null) {
                    retryText.setText(message);
                }

                if(StringUtils.isEmpty(message)) {
                    L.e(LOG_TAG + " " + "Slide in requested but we have no message or action to perform", new Object[0]);
                    return;
                }

                L.d(LOG_TAG + " " + "Sliding in retry view...", new Object[0]);
                Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.info_slide_in);
                slideIn.setAnimationListener(new AnimationListenerAdapter() {
                    public void onAnimationStart(Animation animation) {
                        NetworkAwareActionbarActivity.this.findRetryView().setVisibility(View.VISIBLE);
                    }
                });
                this.findRetryView().startAnimation(slideIn);
            }

            if(!shouldShow && View.VISIBLE == this.findRetryView().getVisibility()) {
                L.d(LOG_TAG + " " + "Sliding out retry view...", new Object[0]);
                Animation slideOut1 = AnimationUtils.loadAnimation(this, R.anim.info_slide_out);
                slideOut1.setAnimationListener(new AnimationListenerAdapter() {
                    public void onAnimationEnd(Animation animation) {
                        NetworkAwareActionbarActivity.this.findRetryView().setVisibility(View.GONE);
                    }
                });
                this.findRetryView().startAnimation(slideOut1);
            }

        }
    }

    private View findRetryView() {
        if(this.mRetryView == null) {
            this.mRetryView = this.findViewById(R.id.retry_view_container);
        }

        return this.mRetryView;
    }

    private View findNoNetworkView() {
        if(this.mNoNetworkView == null) {
            this.mNoNetworkView = this.findViewById(R.id.activity_network_no_connectivity);
        }

        return this.mNoNetworkView;
    }

    class NetworkAvailabilityBroadcastReceiver extends BroadcastReceiver {
        private final String LOG_TAG = NetworkAvailabilityBroadcastReceiver.class.getSimpleName();

        NetworkAvailabilityBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if(intent != null && "android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                boolean noConnection = intent.getBooleanExtra("noConnectivity", false);
                if(noConnection) {
                    NetworkAwareActionbarActivity.this.onNetworkUnavailable();
                } else {
                    NetworkAwareActionbarActivity.this.onNetworkAvailable();
                }

            } else {
                L.w(this.LOG_TAG + " " + "onReceive: intent was null or getAction() was mismatched", new Object[0]);
            }
        }
    }
}

