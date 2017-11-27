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

import com.zendesk.belvedere.Belvedere;
import com.zendesk.belvedere.BelvedereCallback;
import com.zendesk.belvedere.BelvedereResult;

import java.io.File;
import java.util.List;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.activity.CustomFeedbackActivity;
import sg.com.conversant.swiftplayer.feedback.CustomBelvedereProvider;
import sg.com.conversant.swiftplayer.feedback.ImagePickerHelper;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.feedback.NetworkUtils;
import sg.com.conversant.swiftplayer.feedback.Retryable;
import sg.com.conversant.swiftplayer.feedback.StringUtils;
import sg.com.conversant.swiftplayer.feedback.SubmissionListener;
import sg.com.conversant.swiftplayer.feedback.TextWatcherAdapter;
import sg.com.conversant.swiftplayer.views.AttachmentFlexboxLayout;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/23.
 */

public class CustomFeedbackFragment extends Fragment {
    private static final String LOG_TAG = CustomFeedbackFragment.class.getSimpleName();
    private EditText descriptionEditText;
    private ProgressBar progressBar;
    private ViewGroup container;
    private Button submitBtn;
    private View addAttachmentBtn;

    private AttachmentFlexboxLayout attachmentFlexboxLayout;
    private AttachmentFlexboxLayout.AttachmentContainerListener attachmentContainerListener;
    private AttachmentFlexboxLayout.AddAttachmentListener addAttachmentListener;
    private BelvedereCallback<List<BelvedereResult>> imagesExtracted;
    private boolean isEmailFieldVisible;

    private SubmissionListener feedbackListener;

    private Retryable retryable;
    private ImagePickerHelper imagePickerHelper;

    public CustomFeedbackFragment() {
    }

    public static CustomFeedbackFragment newInstance() {
        return new CustomFeedbackFragment();
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
    }

    @SuppressLint({"SetTextI18n"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Object fragmentView;

        fragmentView = inflater.inflate(R.layout.fragment_custom_feedback, container, false);
        this.attachmentFlexboxLayout = (AttachmentFlexboxLayout) ((View) fragmentView).findViewById(R.id.fragment_attachments_view);
        this.attachmentFlexboxLayout.setAttachmentContainerListener(this.attachmentContainerListener);
        this.attachmentFlexboxLayout.setOnAddAttachmentListener(this.addAttachmentListener);
        this.descriptionEditText = (EditText) ((View) fragmentView).findViewById(R.id.contact_fragment_description);
        this.descriptionEditText.addTextChangedListener(new TextWatcherAdapter() {
            public void afterTextChanged(Editable editable) {
                CustomFeedbackFragment.this.checkSendButtonState();
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

        return (View) fragmentView;
    }

    private void setupAttachmentView() {
        this.attachmentFlexboxLayout.setAddAttachmentView();
        this.addAttachmentBtn = this.attachmentFlexboxLayout.getAddAttachmentView();
    }

    private void preloadSettingsAndInit() {
        this.progressBar.setVisibility(View.VISIBLE);
        checkSettingStatus();
    }

    private void checkSettingStatus() {
        CustomFeedbackFragment.this.progressBar.setVisibility(View.GONE);
        CustomFeedbackFragment.this.container.setVisibility(View.VISIBLE);
        if (CustomFeedbackFragment.this.submitBtn != null && CustomFeedbackFragment.this.addAttachmentBtn != null) {
            CustomFeedbackFragment.this.disableSendButton();
            CustomFeedbackFragment.this.updateAttachmentButtonState();
        }

        if (!NetworkUtils.isConnected(CustomFeedbackFragment.this.getContext())) {
            CustomFeedbackFragment.this.networkNotAvailable();
            L.d(CustomFeedbackFragment.LOG_TAG + " " + "Preload settings: Network not available." + " " + new Object[0]);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.feedback_send_btn:
                    CustomFeedbackFragment.this.sendFeedback();
                    break;
            }
        }
    };

    public void onDetach() {
        super.onDetach();
        this.tearDownCallbacks();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.disableSendButton();
        Belvedere belvedere = CustomBelvedereProvider.INSTANCE.getBelvedere(this.getContext());
        belvedere.getFilesFromActivityOnResult(requestCode, resultCode, data, this.imagesExtracted);
    }

    private void checkSendButtonState() {
        boolean descriptionPresent = this.descriptionEditText != null && StringUtils.hasLength(this.descriptionEditText.getText().toString());

        if (descriptionPresent) {
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
            L.d(LOG_TAG + " " + "Ignoring enableAttachmentButton() because there is no network connection", new Object[0]);
            return false;
        } else if (!CustomBelvedereProvider.INSTANCE.getBelvedere(this.getContext()).oneOrMoreSourceAvailable()) {
            L.d(LOG_TAG + " " + "Ignoring enableAttachmentButton() because we don\'t have permissions for the camera or gallery", new Object[0]);
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
        L.i(LOG_TAG + " sendFeedback");
        if (this.feedbackListener != null) {
            this.feedbackListener.onSubmissionStarted();
        }

        this.progressBar.setVisibility(View.VISIBLE);
        this.descriptionEditText.setEnabled(false);
        this.disableSendButton();
        this.attachmentFlexboxLayout.setAttachmentsDeletable(false);

        //send feedback
        if (imagePickerHelper == null) return;
        List<File> attachments = imagePickerHelper.getAddedForUpload();
        if (attachments != null) L.i(LOG_TAG + " attachments.size: " + attachments.size());
    }

    private void disableSendButton() {
        if (this.submitBtn != null) {
            this.submitBtn.setEnabled(false);
        }
    }

    private void enableSendButton() {
        if (!NetworkUtils.isConnected(this.getActivity())) {
            L.d(LOG_TAG + " " + "Ignoring enableSendButton() because there is no network connection", new Object[0]);
        } else {
            if (this.submitBtn != null) {
                this.submitBtn.setEnabled(true);
            }
        }
    }

    public void networkNotAvailable() {
        FragmentActivity activity = this.getActivity();
        if (activity != null && activity instanceof CustomFeedbackActivity) {
            ((CustomFeedbackActivity) activity).onNetworkUnavailable();
        }
    }

    public void onNetworkAvailable() {
        this.checkSendButtonState();
        this.updateAttachmentButtonState();
    }

    public void onNetworkUnavailable() {
        this.disableAttachmentButton();
        this.disableSendButton();
    }

    private void tearDownCallbacks() {
        this.imagesExtracted.cancel();
        this.imagesExtracted = null;
        if (this.attachmentFlexboxLayout != null) {
            this.attachmentFlexboxLayout.setAttachmentContainerListener((AttachmentFlexboxLayout.AttachmentContainerListener) null);
        }
    }

    private void setUpCallbacks() {
        imagePickerHelper = new ImagePickerHelper();
        this.imagesExtracted = new BelvedereCallback() {
            @Override
            public void success(Object o) {
                List<BelvedereResult> result = null;
                result = (List<BelvedereResult>) o;
                if (result != null && result.size() > 0) {
                    L.i(LOG_TAG + " result.size: " + result.size());
                    imagePickerHelper.processAndUploadSelectedFiles(result, CustomFeedbackFragment.this.getActivity(), CustomFeedbackFragment.this.attachmentFlexboxLayout);
                    CustomFeedbackFragment.this.checkSendButtonState();
                    for (BelvedereResult file : result) {
                        CustomFeedbackFragment.this.attachmentFlexboxLayout.setAttachmentUploaded(file.getFile());
                    }
                    L.i(LOG_TAG + " child size: " + attachmentFlexboxLayout.getChildCount());
                }
            }
        };
        this.attachmentContainerListener = new AttachmentFlexboxLayout.AttachmentContainerListener() {
            public void attachmentRemoved(File file) {
                imagePickerHelper.removeImage(file);
            }
        };
        this.addAttachmentListener = new AttachmentFlexboxLayout.AddAttachmentListener() {
            @Override
            public void addAttachment() {
                Belvedere belvedere = CustomBelvedereProvider.INSTANCE.getBelvedere(CustomFeedbackFragment.this.getContext());
                belvedere.showDialog(CustomFeedbackFragment.this.getChildFragmentManager());
            }
        };
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
