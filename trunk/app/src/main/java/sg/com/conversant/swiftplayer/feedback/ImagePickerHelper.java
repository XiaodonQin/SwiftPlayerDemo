package sg.com.conversant.swiftplayer.feedback;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.zendesk.belvedere.BelvedereResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.views.AttachmentFlexboxLayout;


/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/24.
 */

public class ImagePickerHelper {
    private static final String LOG_TAG = ImagePickerHelper.class.getSimpleName();
    private List<File> mAddedForUpload = new ArrayList();
    private Map<File, UploadResponse> uploadedAttachments = new HashMap();

    public ImagePickerHelper() {
    }

    public void addImage(BelvedereResult file, String mimeType) {
        this.mAddedForUpload.add(file.getFile());
    }

    public void removeImage(File file) {
        this.mAddedForUpload.remove(file);
    }

    public HashMap<AttachmentFlexboxLayout.AttachmentState, List<File>> getRecentState() {
        ArrayList uploaded = new ArrayList(this.uploadedAttachments.keySet());
        ArrayList uploading = new ArrayList();
        Iterator attachmentTypeListHashMap = this.mAddedForUpload.iterator();

        while(attachmentTypeListHashMap.hasNext()) {
            File f = (File)attachmentTypeListHashMap.next();
            if(!uploaded.contains(f)) {
                uploading.add(f);
            }
        }

        HashMap attachmentTypeListHashMap1 = new HashMap();
        attachmentTypeListHashMap1.put(AttachmentFlexboxLayout.AttachmentState.UPLOADED, uploaded);
        attachmentTypeListHashMap1.put(AttachmentFlexboxLayout.AttachmentState.UPLOADING, uploading);
        return attachmentTypeListHashMap1;
    }

    public List<BelvedereResult> removeDuplicateFilesFromList(List<BelvedereResult> files) {
        L.i(LOG_TAG + " removeDuplicateFilesFromList files.size: " + files.size());
        ArrayList uniqueFiles = new ArrayList();
        Iterator var3 = files.iterator();

        while(true) {
            BelvedereResult file;
            do {
                if(!var3.hasNext()) {
                    return uniqueFiles;
                }

                file = (BelvedereResult)var3.next();
            } while(file == null);

            boolean contains = false;
            Iterator var6 = this.mAddedForUpload.iterator();
            L.i(LOG_TAG + " mAddedForUpload.size: " + mAddedForUpload.size());

            while(var6.hasNext()) {
                File fn = (File)var6.next();
                if(fn.getAbsolutePath().equals(file.getFile().getAbsolutePath())) {
                    contains = true;
                }
            }

            if(!contains) {
                uniqueFiles.add(file);
            }
        }
    }

    public void processAndUploadSelectedFiles(List<BelvedereResult> selectedFiles, Context context, AttachmentFlexboxLayout attachmentFlexboxLayout) {
        L.i(LOG_TAG + " processAndUploadSelectedFiles selectedFiles.size: " + selectedFiles.size());
        List uniqueFiles = removeDuplicateFilesFromList(selectedFiles);
        if (uniqueFiles != null) {
            L.i(LOG_TAG + " uniqueFiles.size: " + uniqueFiles.size());
        }
        if(uniqueFiles.size() != selectedFiles.size()) {
            Toast.makeText(context, context.getString(R.string.attachment_upload_error_file_already_added), Toast.LENGTH_LONG).show();
            L.e(LOG_TAG + " Files already added " + new Object[0]);
        }

        Iterator var6 = uniqueFiles.iterator();

        while(true) {
            while(var6.hasNext()) {
                BelvedereResult file = (BelvedereResult)var6.next();
                String fileName;
                if(file != null && file.getFile().exists()) {
                    addImage(file, getMimeType(context, file.getUri()));
                    attachmentFlexboxLayout.addAttachment(file.getFile());
                } else {
                    Toast.makeText(context, context.getString(R.string.attachment_upload_error_file_not_found), Toast.LENGTH_LONG).show();
                    fileName = file == null?"no filename":file.getFile().getName();
                    L.e(LOG_TAG + " File not found: " + fileName + " " + new Object[0]);
                }
            }

            return;
        }
    }


    private static String getMimeType(Context context, Uri file) {
        ContentResolver cr = context.getContentResolver();
        return file != null?cr.getType(file):"application/octet-stream";
    }

    public List<File> getAddedForUpload() {
        return mAddedForUpload;
    }
}
