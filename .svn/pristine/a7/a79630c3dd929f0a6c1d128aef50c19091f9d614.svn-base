/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/16.
 */
package sg.com.conversant.swiftplayer.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zendesk.belvedere.Belvedere;
import com.zendesk.belvedere.BelvedereCallback;
import com.zendesk.belvedere.BelvedereResult;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.attachment.AttachmentHelper;
import com.zendesk.sdk.attachment.ImageUploadHelper;
import com.zendesk.sdk.attachment.ZendeskBelvedereProvider;
import com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ZendeskFeedbackConnector;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.request.CreateRequest;
import com.zendesk.sdk.model.request.UploadResponse;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.sdk.network.Retryable;
import com.zendesk.sdk.network.SettingsHelper;
import com.zendesk.sdk.network.SubmissionListener;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.ui.EmailAddressAutoCompleteTextView;
import com.zendesk.sdk.ui.TextWatcherAdapter;
import com.zendesk.sdk.util.NetworkUtils;
import com.zendesk.service.ErrorResponse;
import com.zendesk.service.ErrorResponseAdapter;
import com.zendesk.service.SafeZendeskCallback;
import com.zendesk.service.ZendeskCallback;
import com.zendesk.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.activity.ZendeskFeedbackActivity;
import sg.com.conversant.swiftplayer.feedback.ZendeskAttachmentHelper;
import sg.com.conversant.swiftplayer.views.AttachmentFlexboxLayout;

/**
 * TODO
 *
 * @author Xiaodong
 */
public class ZendeskFeedbackFragment extends Fragment {
    private static final String LOG_TAG = ZendeskFeedbackFragment.class.getSimpleName();
    public static final String EXTRA_CONFIGURATION = "EXTRA_CONFIGURATION";
    public static final String EXTRA_CONFIGURATION_TAGS = "EXTRA_CONFIGURATION_TAGS";
    public static final String EXTRA_CONFIGURATION_ADDITIONAL_INFO = "EXTRA_CONFIGURATION_ADDITIONAL";
    public static final String EXTRA_CONFIGURATION_REQUEST_SUBJECT = "EXTRA_CONFIGURATION_SUBJECT";
    private static final int FULL_OPACTITY = 255;
    private static final int FOURTY_PERCENT_OPACITY = 102;
    private EditText descriptionEditText;
    private EmailAddressAutoCompleteTextView emailEditText;
    private ProgressBar progressBar;
    private ViewGroup container;
    private Button submitBtn;
    private View addAttachmentBtn;

    private ZendeskFeedbackFragment.ContactZendeskFeedbackConfiguration contactZendeskFeedbackConfiguration;
    private SubmissionListener feedbackListener;
    private Retryable retryable;
    private ImageUploadHelper imageUploadService;
    private AttachmentFlexboxLayout attachmentFlexboxLayout;
    private ImageUploadHelper.ImageUploadProgressListener imageUploadProgressListener;
    private AttachmentFlexboxLayout.AttachmentContainerListener attachmentContainerListener;
    private AttachmentFlexboxLayout.AddAttachmentListener addAttachmentListener;
    private BelvedereCallback<List<BelvedereResult>> imagesExtracted;
    private SafeZendeskCallback<CreateRequest> requestCallback;
    private SafeZendeskCallback<SafeMobileSettings> settingsCallback;
    private boolean isEmailFieldVisible;
    private SafeMobileSettings mobileSettings;
    private SettingsHelper settingsHelper;

    public ZendeskFeedbackFragment() {
    }

    public static ZendeskFeedbackFragment newInstance(ZendeskFeedbackConfiguration configuration) {
        Bundle fragmentArgs = new Bundle();
        if (configuration == null) {
            Logger.e(LOG_TAG, "Cannot instantiate a ContactZendeskFragment with no configuration", new Object[0]);
            return null;
        } else {
            fragmentArgs.putBundle("EXTRA_CONFIGURATION", configurationToBundle(configuration.getTags(), configuration.getRequestSubject(), configuration.getAdditionalInfo()));
            ZendeskFeedbackFragment contactZendeskFragment = new ZendeskFeedbackFragment();
            contactZendeskFragment.setArguments(fragmentArgs);
            contactZendeskFragment.setRetainInstance(true);
            return contactZendeskFragment;
        }
    }

    private static Bundle configurationToBundle(List<String> tags, String subject, String additionalInfo) {
        Bundle bundle = new Bundle();
        bundle.putString("EXTRA_CONFIGURATION_SUBJECT", subject);
        bundle.putString("EXTRA_CONFIGURATION_ADDITIONAL", additionalInfo);
        bundle.putStringArrayList("EXTRA_CONFIGURATION_TAGS", tags == null ? null : new ArrayList(tags));
        return bundle;
    }

    private static ZendeskFeedbackConfiguration bundleToZendeskFeedbackConfiguration(final Bundle bundle) {
        return new ZendeskFeedbackConfiguration() {
            public List<String> getTags() {
                return bundle.getStringArrayList("EXTRA_CONFIGURATION_TAGS");
            }

            public String getAdditionalInfo() {
                return bundle.getString("EXTRA_CONFIGURATION_ADDITIONAL");
            }

            public String getRequestSubject() {
                return bundle.getString("EXTRA_CONFIGURATION_SUBJECT");
            }
        };
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Retryable) {
            this.retryable = (Retryable) context;
        }

        this.setUpCallbacks();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.settingsHelper = ZendeskConfig.INSTANCE.provider().uiSettingsHelper();
        if (this.getArguments() != null && this.getArguments().containsKey("EXTRA_CONFIGURATION")) {
            this.contactZendeskFeedbackConfiguration = new ZendeskFeedbackFragment.ContactZendeskFeedbackConfiguration(bundleToZendeskFeedbackConfiguration(this.getArguments().getBundle("EXTRA_CONFIGURATION")));
        } else {
            Logger.w(LOG_TAG, "No configuration passed to the fragment, this will result in no feedback being sent", new Object[0]);
        }

        if (this.imageUploadService == null) {
            this.imageUploadService = new ImageUploadHelper(this.imageUploadProgressListener, ZendeskConfig.INSTANCE.provider().uploadProvider());
        } else {
            this.imageUploadService.setImageUploadProgressListener(this.imageUploadProgressListener);
        }

    }

    @SuppressLint({"SetTextI18n"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean hasIdentity = ZendeskConfig.INSTANCE.storage().identityStorage().getIdentity() != null;
        Object fragmentView;
        if (hasIdentity) {
            fragmentView = inflater.inflate(R.layout.fragment_zendesk_feedback, container, false);
            this.attachmentFlexboxLayout = (AttachmentFlexboxLayout) ((View) fragmentView).findViewById(R.id.fragment_attachments_view);
            this.attachmentFlexboxLayout.setState(this.imageUploadService);
            this.attachmentFlexboxLayout.setAttachmentContainerListener(this.attachmentContainerListener);
            this.attachmentFlexboxLayout.setOnAddAttachmentListener(this.addAttachmentListener);
            this.emailEditText = (EmailAddressAutoCompleteTextView) ((View) fragmentView).findViewById(R.id.contact_fragment_email);
            this.emailEditText.addTextChangedListener(new TextWatcherAdapter() {
                public void afterTextChanged(Editable s) {
                    ZendeskFeedbackFragment.this.checkSendButtonState();
                }
            });
            this.descriptionEditText = (EditText) ((View) fragmentView).findViewById(R.id.contact_fragment_description);
            this.descriptionEditText.addTextChangedListener(new TextWatcherAdapter() {
                public void afterTextChanged(Editable editable) {
                    ZendeskFeedbackFragment.this.checkSendButtonState();
                }
            });
            this.progressBar = (ProgressBar) ((View) fragmentView).findViewById(R.id.contact_fragment_progress);
            this.submitBtn = (Button) ((View) fragmentView).findViewById(R.id.feedback_send_btn);
            this.container = (ViewGroup) ((View) fragmentView).findViewById(R.id.contact_fragment_container);
            this.submitBtn.setOnClickListener(clickListener);
            this.setupAttachmentView();
            this.updateAttachmentButtonState();
            this.checkSendButtonState();
            this.preloadSettingsAndInit();
        } else {
            TextView errorView = new TextView(inflater.getContext());
            errorView.setText("Error, please check that an identity or email address has been set");
            fragmentView = errorView;
        }

        return (View) fragmentView;
    }

    private void setupAttachmentView() {
        this.attachmentFlexboxLayout.setAddAttachmentView();
        this.addAttachmentBtn = this.attachmentFlexboxLayout.getAddAttachmentView();
    }

    private void preloadSettingsAndInit() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.settingsHelper.loadSetting(this.settingsCallback);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.feedback_send_btn:
                    ZendeskFeedbackFragment.this.sendFeedback();
                    break;
            }
        }
    };

    public void onDetach() {
        super.onDetach();
        this.retryable = null;
        this.tearDownCallbacks();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.disableSendButton();
        Belvedere belvedere = ZendeskBelvedereProvider.INSTANCE.getBelvedere(this.getContext());
        belvedere.getFilesFromActivityOnResult(requestCode, resultCode, data, this.imagesExtracted);
    }

    public void deleteUnusedAttachmentsBeforeShutdown() {
        if (this.imageUploadService != null) {
            this.imageUploadService.deleteAllAttachmentsBeforeShutdown();
        }

    }

    private void checkSendButtonState() {
        boolean descriptionPresent = this.descriptionEditText != null && StringUtils.hasLength(this.descriptionEditText.getText().toString());
        boolean imagesUploaded = this.imageUploadService.isImageUploadCompleted();
        boolean emailFieldValidOrHidden;
        if (this.isEmailFieldVisible) {
            emailFieldValidOrHidden = this.emailEditText != null && this.emailEditText.isInputValid();
        } else {
            emailFieldValidOrHidden = true;
        }

        if (descriptionPresent && imagesUploaded && emailFieldValidOrHidden) {
            this.enableSendButton();
        } else {
            this.disableSendButton();
        }

    }

    private void updateAttachmentButtonState() {
        if (this.shouldEnableAttachmentButton()) {
            this.enableAttachmentButton();
        } else {
            this.disableAttachmentButton();
        }

    }

    private void disableAttachmentButton() {
        if (this.addAttachmentBtn != null) {
            this.addAttachmentBtn.setEnabled(false);
        }
    }

    private boolean shouldEnableAttachmentButton() {
        if (!NetworkUtils.isConnected(this.getActivity())) {
            Logger.d(LOG_TAG, "Ignoring enableAttachmentButton() because there is no network connection", new Object[0]);
            return false;
        } else if (!AttachmentHelper.isAttachmentSupportEnabled(this.mobileSettings)) {
            Logger.d(LOG_TAG, "Ignoring enableAttachmentButton() because attachment support is disabled", new Object[0]);
            return false;
        } else if (!ZendeskBelvedereProvider.INSTANCE.getBelvedere(this.getContext()).oneOrMoreSourceAvailable()) {
            Logger.d(LOG_TAG, "Ignoring enableAttachmentButton() because we don\'t have permissions for the camera or gallery", new Object[0]);
            return false;
        } else {
            return true;
        }
    }

    private void enableAttachmentButton() {
        if (this.addAttachmentBtn != null) {
            this.addAttachmentBtn.setEnabled(true);
        }
    }

    public void setFeedbackListener(SubmissionListener feedbackListener) {
        this.feedbackListener = feedbackListener;
    }

    private void sendFeedback() {
        if (this.feedbackListener != null) {
            this.feedbackListener.onSubmissionStarted();
        }

        if (this.isEmailFieldVisible) {
            this.updateIdentity();
        }

        if (this.contactZendeskFeedbackConfiguration == null) {
            Logger.e(LOG_TAG, "Configuration is null, cannot send feedback...", new Object[0]);
            if (this.feedbackListener != null) {
                this.feedbackListener.onSubmissionError(new ErrorResponseAdapter("Configuration is null, cannot send feedback..."));
            }

        } else {
            this.progressBar.setVisibility(View.VISIBLE);
            this.descriptionEditText.setEnabled(false);
            this.emailEditText.setEnabled(false);
            this.disableSendButton();
            this.attachmentFlexboxLayout.setAttachmentsDeletable(false);
            ZendeskFeedbackConnector feedbackConnector = ZendeskFeedbackConnector.defaultConnector(this.getActivity(), this.contactZendeskFeedbackConfiguration, this.mobileSettings.getContactZendeskTags());
            feedbackConnector.sendFeedback(this.descriptionEditText.getText().toString(), this.imageUploadService.getUploadTokens(), this.requestCallback);
        }
    }

    private void updateIdentity() {
        Logger.d(LOG_TAG, "Updating existing anonymous identity with an email address", new Object[0]);
        Identity identity = ZendeskConfig.INSTANCE.storage().identityStorage().getIdentity();
        String emailAddress = this.emailEditText.getText().toString();
        if (identity != null && identity instanceof AnonymousIdentity) {
            AnonymousIdentity anonymousIdentity = (AnonymousIdentity) identity;
            AnonymousIdentity.Builder newIdentity = new AnonymousIdentity.Builder();
            newIdentity.withEmailIdentifier(emailAddress);
            if (StringUtils.hasLength(anonymousIdentity.getName())) {
                newIdentity.withNameIdentifier(anonymousIdentity.getName());
            }

            ZendeskConfig.INSTANCE.setIdentity(newIdentity.build());
        } else {
            Logger.d(LOG_TAG, "No valid identity found ", new Object[0]);
        }
    }

    private void disableSendButton() {
        if (this.submitBtn != null) {
            this.submitBtn.setEnabled(false);
        }
    }

    private void enableSendButton() {
        if (!NetworkUtils.isConnected(this.getActivity())) {
            Logger.d(LOG_TAG, "Ignoring enableSendButton() because there is no network connection", new Object[0]);
        } else {
            if (this.submitBtn != null) {
                this.submitBtn.setEnabled(true);
            }
        }
    }

    public void networkNotAvailable() {
        FragmentActivity activity = this.getActivity();
        if (activity != null && activity instanceof ZendeskFeedbackActivity) {
            ((ZendeskFeedbackActivity) activity).onNetworkUnavailable();
        }

    }

    public void onNetworkAvailable() {
        this.checkSendButtonState();
        this.updateAttachmentButtonState();
        if (this.mobileSettings == null) {
            this.preloadSettingsAndInit();
        }

    }

    public void onNetworkUnavailable() {
        this.disableAttachmentButton();
        this.disableSendButton();
    }

    private void tearDownCallbacks() {
        this.requestCallback.cancel();
        this.requestCallback = null;
        this.imagesExtracted.cancel();
        this.imagesExtracted = null;
        if (this.attachmentFlexboxLayout != null) {
            this.attachmentFlexboxLayout.setAttachmentContainerListener((AttachmentFlexboxLayout.AttachmentContainerListener) null);
        }

        this.settingsCallback.cancel();
        this.settingsCallback = null;
    }

    private void setUpCallbacks() {
        RequestCallback mRequestCallback = new RequestCallback();

        this.requestCallback = SafeZendeskCallback.from(mRequestCallback);
        this.imagesExtracted = new BelvedereCallback() {
            @Override
            public void success(Object o) {
                List<BelvedereResult> result = null;
                result = (List<BelvedereResult>) o;
                SafeMobileSettings storedSettings = ZendeskConfig.INSTANCE.getMobileSettings();
                if (storedSettings != null) {
                    ZendeskAttachmentHelper.processAndUploadSelectedFiles(result, ZendeskFeedbackFragment.this.imageUploadService, ZendeskFeedbackFragment.this.getActivity(), ZendeskFeedbackFragment.this.attachmentFlexboxLayout, storedSettings);
                }

                ZendeskFeedbackFragment.this.checkSendButtonState();
            }
        };
        this.attachmentContainerListener = new AttachmentFlexboxLayout.AttachmentContainerListener() {
            public void attachmentRemoved(File file) {
                ZendeskFeedbackFragment.this.imageUploadService.removeImage(file);
            }
        };
        this.addAttachmentListener = new AttachmentFlexboxLayout.AddAttachmentListener() {
            @Override
            public void addAttachment() {
                Belvedere belvedere = ZendeskBelvedereProvider.INSTANCE.getBelvedere(ZendeskFeedbackFragment.this.getContext());
                belvedere.showDialog(ZendeskFeedbackFragment.this.getChildFragmentManager());
            }
        };
        this.imageUploadProgressListener = new ImageUploadHelper.ImageUploadProgressListener() {
            public void allImagesUploaded(Map<File, UploadResponse> responses) {
                ZendeskFeedbackFragment.this.checkSendButtonState();
            }

            public void imageUploaded(UploadResponse uploadResponse, BelvedereResult file) {
                ZendeskFeedbackFragment.this.attachmentFlexboxLayout.setAttachmentUploaded(file.getFile());
            }

            public void imageUploadError(ErrorResponse errorResponse, BelvedereResult file) {
                Logger.e(ZendeskFeedbackFragment.LOG_TAG, String.format(Locale.US, "Image upload error; Reason: %s, Status: %s, isNetworkError: %s", new Object[]{errorResponse.getReason(), Integer.valueOf(errorResponse.getStatus()), Boolean.valueOf(errorResponse.isNetworkError())}), new Object[0]);
                ZendeskAttachmentHelper.showAttachmentTryAgainDialog(ZendeskFeedbackFragment.this.getActivity(), file, errorResponse, ZendeskFeedbackFragment.this.imageUploadService, ZendeskFeedbackFragment.this.attachmentFlexboxLayout);
            }
        };
        this.settingsCallback = SafeZendeskCallback.from(new ZendeskCallback() {
            @Override
            public void onSuccess(Object o) {
                SafeMobileSettings mobileSettings = null;
                if (o instanceof SafeMobileSettings) {
                    mobileSettings = (SafeMobileSettings) o;
                }
                ZendeskFeedbackFragment.this.mobileSettings = mobileSettings;
                ZendeskFeedbackFragment.this.isEmailFieldVisible = ZendeskFeedbackFragment.this.isEmailFieldEnabled(mobileSettings);
                ZendeskFeedbackFragment.this.emailEditText.setVisibility(ZendeskFeedbackFragment.this.isEmailFieldVisible ? View.VISIBLE : View.GONE);
                ZendeskFeedbackFragment.this.progressBar.setVisibility(View.GONE);
                ZendeskFeedbackFragment.this.container.setVisibility(View.VISIBLE);
                if (ZendeskFeedbackFragment.this.submitBtn != null && ZendeskFeedbackFragment.this.addAttachmentBtn != null) {
                    ZendeskFeedbackFragment.this.disableSendButton();
                    ZendeskFeedbackFragment.this.updateAttachmentButtonState();
                }

                if (!NetworkUtils.isConnected(ZendeskFeedbackFragment.this.getContext())) {
                    ZendeskFeedbackFragment.this.networkNotAvailable();
                    Logger.d(ZendeskFeedbackFragment.LOG_TAG, "Preload settings: Network not available.", new Object[0]);
                }
            }

            public void onError(ErrorResponse errorResponse) {
                ZendeskFeedbackFragment.this.progressBar.setVisibility(View.GONE);
                if (!NetworkUtils.isConnected(ZendeskFeedbackFragment.this.getContext())) {
                    ZendeskFeedbackFragment.this.networkNotAvailable();
                    Logger.d(ZendeskFeedbackFragment.LOG_TAG, "Preload settings: Network not available.", new Object[0]);
                } else if (ZendeskFeedbackFragment.this.retryable != null) {
                    ZendeskFeedbackFragment.this.retryable.onRetryAvailable(ZendeskFeedbackFragment.this.getString(com.zendesk.sdk.R.string.rate_my_app_dialog_feedback_send_error_toast), new View.OnClickListener() {
                        public void onClick(View v) {
                            ZendeskFeedbackFragment.this.preloadSettingsAndInit();
                            FragmentActivity parentActivity = ZendeskFeedbackFragment.this.getActivity();
                            if (parentActivity instanceof Retryable) {
                                ((Retryable) parentActivity).onRetryUnavailable();
                            }

                        }
                    });
                }

            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        this.imageUploadService.setImageUploadProgressListener((ImageUploadHelper.ImageUploadProgressListener) null);
    }

    boolean isEmailFieldEnabled(SafeMobileSettings safeSettings) {
        if (safeSettings == null) {
            return false;
        } else {
            boolean conversationsEnabled = safeSettings.isConversationsEnabled();
            if (conversationsEnabled) {
                return false;
            } else {
                Identity identity = ZendeskConfig.INSTANCE.storage().identityStorage().getIdentity();
                if (identity != null && identity instanceof AnonymousIdentity) {
                    AnonymousIdentity anonymousIdentity = (AnonymousIdentity) identity;
                    if (StringUtils.hasLength(anonymousIdentity.getEmail())) {
                        return false;
                    } else {
                        boolean coppaEnabled = ZendeskConfig.INSTANCE.isCoppaEnabled();
                        if (coppaEnabled) {
                            Logger.w(LOG_TAG, "This ticket will not be viewable by the user after submission. Conversations are not supported and COPPA removes any personal information associated with the ticket", new Object[0]);
                            return false;
                        } else {
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            }
        }
    }

    private void reinitializeFragment() {
        this.progressBar.setVisibility(View.GONE);
        this.attachmentFlexboxLayout.reset();
        this.imageUploadService.reset();
        this.isEmailFieldVisible = this.isEmailFieldEnabled(this.mobileSettings);
        this.descriptionEditText.setEnabled(true);
        this.descriptionEditText.setText("");
        this.emailEditText.setEnabled(true);
        this.emailEditText.setText("");
        this.checkSendButtonState();
        this.updateAttachmentButtonState();
    }

    private class ContactZendeskFeedbackConfiguration implements ZendeskFeedbackConfiguration {
        private final ZendeskFeedbackConfiguration mConfiguration;

        public ContactZendeskFeedbackConfiguration(ZendeskFeedbackConfiguration configuration) {
            this.mConfiguration = configuration;
        }

        public List<String> getTags() {
            return this.mConfiguration.getTags();
        }

        public String getAdditionalInfo() {
            return this.mConfiguration.getAdditionalInfo();
        }

        public String getRequestSubject() {
            return this.mConfiguration.getRequestSubject();
        }
    }

    public class RequestCallback extends ZendeskCallback<CreateRequest> {
        RequestCallback() {
        }

        public void onSuccess(CreateRequest result) {
            if (result != null && result.getId() != null) {
                ZendeskConfig.INSTANCE.storage().requestStorage().setCommentCount(result.getId(), 1);
            } else {
                Logger.e(ZendeskFeedbackFragment.LOG_TAG, "Attempted to store a null request in callback.", new Object[0]);
            }

            if (ZendeskFeedbackFragment.this.feedbackListener != null) {
                ZendeskFeedbackFragment.this.feedbackListener.onSubmissionCompleted();
            }

            if (ZendeskFeedbackFragment.this.isAdded() && ZendeskFeedbackFragment.this.getActivity() != null) {
                ZendeskFeedbackFragment.this.reinitializeFragment();
            }

        }

        public void onError(ErrorResponse error) {
            ZendeskFeedbackFragment.this.enableSendButton();
            if (ZendeskFeedbackFragment.this.feedbackListener != null) {
                ZendeskFeedbackFragment.this.feedbackListener.onSubmissionError(error);
            }

            ZendeskFeedbackFragment.this.progressBar.setVisibility(View.GONE);
            ZendeskFeedbackFragment.this.descriptionEditText.setEnabled(true);
            ZendeskFeedbackFragment.this.emailEditText.setEnabled(true);

            if (ZendeskFeedbackFragment.this.retryable != null) {
                ZendeskFeedbackFragment.this.retryable.onRetryAvailable(ZendeskFeedbackFragment.this.getString(com.zendesk.sdk.R.string.rate_my_app_dialog_feedback_send_error_toast), new View.OnClickListener() {
                    public void onClick(View v) {
                        ZendeskFeedbackFragment.this.sendFeedback();
                    }
                });
            }

        }
    }
}

