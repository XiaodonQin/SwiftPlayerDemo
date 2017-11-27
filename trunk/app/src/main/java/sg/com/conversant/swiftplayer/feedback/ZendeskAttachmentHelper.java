/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/17.
 */
package sg.com.conversant.swiftplayer.feedback;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.widget.Toast;

import com.zendesk.belvedere.BelvedereResult;
import com.zendesk.logger.Logger;
import com.zendesk.sdk.attachment.AttachmentHelper;
import com.zendesk.sdk.attachment.ImageUploadHelper;
import com.zendesk.sdk.model.settings.SafeMobileSettings;
import com.zendesk.service.ErrorResponse;
import com.zendesk.util.FileUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import sg.com.conversant.swiftplayer.views.AttachmentFlexboxLayout;
import sg.com.conversant.swiftplayer.views.AttachmentRecyclerView;

/**
 * TODO
 *
 * @author Xiaodong
 */
public class ZendeskAttachmentHelper extends AttachmentHelper {
    private static final String LOG_TAG = ZendeskAttachmentHelper.class.getSimpleName();

    public static void showAttachmentTryAgainDialog(final Context context, final BelvedereResult file, ErrorResponse errorResponse, final ImageUploadHelper imageUploadHelper, final AttachmentRecyclerView attachmentRecyclerView) {
        Logger.e(LOG_TAG, "Attachment failed to upload: %s", new Object[]{file.getFile().getName()});
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(com.zendesk.sdk.R.string.attachment_upload_error_upload_failed));
        builder.setCancelable(false);
        builder.setNegativeButton(context.getString(com.zendesk.sdk.R.string.attachment_upload_error_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                attachmentRecyclerView.removeAttachment(file.getFile());
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(context.getString(com.zendesk.sdk.R.string.attachment_upload_error_try_again), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                imageUploadHelper.uploadImage(file, ZendeskAttachmentHelper.getMimeType(context, file.getUri()));
                attachmentRecyclerView.setAttachmentState(file.getFile(), AttachmentRecyclerView.AttachmentState.UPLOADING);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static void processAndUploadSelectedFiles(List<BelvedereResult> selectedFiles, ImageUploadHelper imageUploadHelper, Context context, AttachmentRecyclerView attachmentRecyclerView, SafeMobileSettings storedSettings) {
        List uniqueFiles = imageUploadHelper.removeDuplicateFilesFromList(selectedFiles);
        if(uniqueFiles.size() != selectedFiles.size()) {
            Toast.makeText(context, context.getString(com.zendesk.sdk.R.string.attachment_upload_error_file_already_added), Toast.LENGTH_LONG).show();
            Logger.e(LOG_TAG, "Files already added", new Object[0]);
        }

        Iterator var6 = uniqueFiles.iterator();

        while(true) {
            while(var6.hasNext()) {
                BelvedereResult file = (BelvedereResult)var6.next();
                String fileName;
                if(file != null && file.getFile().exists()) {
                    if(isFileEligibleForUpload(file.getFile(), storedSettings)) {
                        imageUploadHelper.uploadImage(file, getMimeType(context, file.getUri()));
                        attachmentRecyclerView.addAttachment(file.getFile());
                    } else {
                        fileName = "";
                        if(storedSettings != null) {
                            fileName = FileUtils.humanReadableFileSize(Long.valueOf(storedSettings.getMaxAttachmentSize()));
                        }

                        Toast.makeText(context, String.format(Locale.US, context.getString(com.zendesk.sdk.R.string.attachment_upload_error_file_too_big), new Object[]{fileName}), Toast.LENGTH_LONG).show();
                        Logger.e(LOG_TAG, "File is too big: " + file.getFile().getName(), new Object[0]);
                    }
                } else {
                    Toast.makeText(context, context.getString(com.zendesk.sdk.R.string.attachment_upload_error_file_not_found), Toast.LENGTH_LONG).show();
                    fileName = file == null?"no filename":file.getFile().getName();
                    Logger.e(LOG_TAG, "File not found: " + fileName, new Object[0]);
                }
            }

            return;
        }
    }

    public static void showAttachmentTryAgainDialog(final Context context, final BelvedereResult file, ErrorResponse errorResponse, final ImageUploadHelper imageUploadHelper, final AttachmentFlexboxLayout attachmentFlexboxLayout) {
        Logger.e(LOG_TAG, "Attachment failed to upload: %s", new Object[]{file.getFile().getName()});
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(com.zendesk.sdk.R.string.attachment_upload_error_upload_failed));
        builder.setCancelable(false);
        builder.setNegativeButton(context.getString(com.zendesk.sdk.R.string.attachment_upload_error_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                attachmentFlexboxLayout.removeAttachment(file.getFile());
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(context.getString(com.zendesk.sdk.R.string.attachment_upload_error_try_again), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                imageUploadHelper.uploadImage(file, ZendeskAttachmentHelper.getMimeType(context, file.getUri()));
                attachmentFlexboxLayout.setAttachmentState(file.getFile(), AttachmentFlexboxLayout.AttachmentState.UPLOADING);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public static void processAndUploadSelectedFiles(List<BelvedereResult> selectedFiles, ImageUploadHelper imageUploadHelper, Context context, AttachmentFlexboxLayout attachmentFlexboxLayout, SafeMobileSettings storedSettings) {
        List uniqueFiles = imageUploadHelper.removeDuplicateFilesFromList(selectedFiles);
        if(uniqueFiles.size() != selectedFiles.size()) {
            Toast.makeText(context, context.getString(com.zendesk.sdk.R.string.attachment_upload_error_file_already_added), Toast.LENGTH_LONG).show();
            Logger.e(LOG_TAG, "Files already added", new Object[0]);
        }

        Iterator var6 = uniqueFiles.iterator();

        while(true) {
            while(var6.hasNext()) {
                BelvedereResult file = (BelvedereResult)var6.next();
                String fileName;
                if(file != null && file.getFile().exists()) {
                    if(isFileEligibleForUpload(file.getFile(), storedSettings)) {
                        imageUploadHelper.uploadImage(file, getMimeType(context, file.getUri()));
                        attachmentFlexboxLayout.addAttachment(file.getFile());
                    } else {
                        fileName = "";
                        if(storedSettings != null) {
                            fileName = FileUtils.humanReadableFileSize(Long.valueOf(storedSettings.getMaxAttachmentSize()));
                        }

                        Toast.makeText(context, String.format(Locale.US, context.getString(com.zendesk.sdk.R.string.attachment_upload_error_file_too_big), new Object[]{fileName}), Toast.LENGTH_LONG).show();
                        Logger.e(LOG_TAG, "File is too big: " + file.getFile().getName(), new Object[0]);
                    }
                } else {
                    Toast.makeText(context, context.getString(com.zendesk.sdk.R.string.attachment_upload_error_file_not_found), Toast.LENGTH_LONG).show();
                    fileName = file == null?"no filename":file.getFile().getName();
                    Logger.e(LOG_TAG, "File not found: " + fileName, new Object[0]);
                }
            }

            return;
        }
    }

    private static String getMimeType(Context context, Uri file) {
        ContentResolver cr = context.getContentResolver();
        return file != null?cr.getType(file):"application/octet-stream";
    }
}
