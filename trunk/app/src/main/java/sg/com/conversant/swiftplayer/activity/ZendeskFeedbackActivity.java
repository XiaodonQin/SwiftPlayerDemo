/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/16.
 */
package sg.com.conversant.swiftplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.feedback.BaseZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.WrappedZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.network.SubmissionListener;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.ui.NetworkAwareActionbarActivity;
import com.zendesk.sdk.ui.ToolbarSherlock;
import com.zendesk.sdk.util.UiUtils;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.fragment.ZendeskFeedbackFragment;

/**
 * TODO
 *
 * @author Xiaodong
 */
public class ZendeskFeedbackActivity extends NetworkAwareActionbarActivity {
    private static final String LOG_TAG = "ZendeskFeedbackActivity";
    private static final String FRAGMENT_TAG = ZendeskFeedbackActivity.class.getSimpleName();
    private ZendeskFeedbackFragment contactZendeskFragment;
    public static final String RESULT_ERROR_REASON = "extra_error_reason";
    public static final String RESULT_ERROR_STATUS_CODE = "extra_status_code";
    public static final String RESULT_ERROR_IS_NETWORK_ERROR = "extra_is_nw_error";
    public static final String EXTRA_CONTACT_CONFIGURATION = "extra_contact_configuration";

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.toolbar_title)
    TextView toolbarTitle;

    private final SubmissionListener mSubmissionListener = new SubmissionListener() {
        public void onSubmissionStarted() {
        }

        public void onSubmissionCompleted() {
            ZendeskFeedbackActivity.this.setResult(-1, new Intent());
            ZendeskFeedbackActivity.this.finish();
        }

        public void onSubmissionCancel() {
        }

        public void onSubmissionError(ErrorResponse errorResponse) {
            ZendeskFeedbackActivity.this.setResult(0, ZendeskFeedbackActivity.this.getErrorIntent(errorResponse));
        }
    };

    public ZendeskFeedbackActivity() {
    }

    public static void startActivity(Context context, @Nullable ZendeskFeedbackConfiguration configuration) {
        if(context == null) {
            Logger.e("ContactZendeskActivity", "Context is null, cannot start the context.", new Object[0]);
        } else {
            Intent intent = new Intent(context, ZendeskFeedbackActivity.class);
            boolean configurationRequiresWrapping = configuration != null && !(configuration instanceof WrappedZendeskFeedbackConfiguration);
            if(configurationRequiresWrapping) {
                configuration = new WrappedZendeskFeedbackConfiguration((ZendeskFeedbackConfiguration)configuration);
            }

            intent.putExtra("extra_contact_configuration", (Serializable)configuration);
            context.startActivity(intent);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zendesk_feedback);
        ButterKnife.inject(this);

        initViews();
        initContent();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText(R.string.zendesk_feedback_text);
    }

    private void initContent() {
        boolean hasSuppliedContactConfiguration = this.getIntent().hasExtra("extra_contact_configuration") && this.getIntent().getSerializableExtra("extra_contact_configuration") instanceof ZendeskFeedbackConfiguration;
        Object configuration;
        if(hasSuppliedContactConfiguration) {
            configuration = (ZendeskFeedbackConfiguration)this.getIntent().getSerializableExtra("extra_contact_configuration");
        } else {
            String fragmentManager = "Contact configuration was not provided. Will use default configuration...";
            Logger.d("ContactZendeskActivity", "Contact configuration was not provided. Will use default configuration...", new Object[0]);
            configuration = new ZendeskFeedbackActivity.DefaultContactConfiguration();
        }

        FragmentManager fragmentManager1 = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager1.findFragmentByTag(FRAGMENT_TAG);
        if(fragment != null && fragment instanceof ZendeskFeedbackFragment) {
            this.contactZendeskFragment = (ZendeskFeedbackFragment) fragment;
        } else {
            this.contactZendeskFragment = ZendeskFeedbackFragment.newInstance((ZendeskFeedbackConfiguration)configuration);
            FragmentTransaction fragmentTransaction = fragmentManager1.beginTransaction();
            fragmentTransaction.add(R.id.activity_zendesk_feedback_root, this.contactZendeskFragment, FRAGMENT_TAG);
            fragmentTransaction.commit();
        }

        this.contactZendeskFragment.setFeedbackListener(this.mSubmissionListener);
    }

    protected void onDestroy() {
        super.onDestroy();
        if(this.contactZendeskFragment != null && this.isFinishing()) {
            Logger.d("ContactZendeskActivity", "Deleting unused attachments", new Object[0]);
            this.contactZendeskFragment.deleteUnusedAttachmentsBeforeShutdown();
        }

    }

    private Intent getErrorIntent(ErrorResponse errorResponse) {
        Intent intent = new Intent();
        intent.putExtra("extra_is_nw_error", errorResponse.isNetworkError());
        intent.putExtra("extra_error_reason", errorResponse.getReason());
        intent.putExtra("extra_status_code", errorResponse.getStatus());
        return intent;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 16908332) {
            this.onBackPressed();
            return true;
        } else {
            return false;
        }
    }

    public void onNetworkAvailable() {
        super.onNetworkAvailable();
        this.contactZendeskFragment.onNetworkAvailable();
    }

    public void onNetworkUnavailable() {
        super.onNetworkUnavailable();
        this.contactZendeskFragment.onNetworkUnavailable();
    }

    class DefaultContactConfiguration extends BaseZendeskFeedbackConfiguration {
        DefaultContactConfiguration() {
        }

        public String getRequestSubject() {
            return ZendeskFeedbackActivity.this.getString(com.zendesk.sdk.R.string.contact_fragment_request_subject);
        }
    }
}
