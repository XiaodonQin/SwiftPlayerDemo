/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2016/4/25.
 */
package sg.com.conversant.swiftplayer.activity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.fragment.MyFragment;
import sg.com.conversant.swiftplayer.sdk.API;

/**
 * TODO
 *
 * @author Xiaodong

 */
public class MyProfileActivity extends AppCompatActivity {

    private static String LOG_TAG = "MyProfileActivity";

    private SharedPreferences sharedPreferences;
    private SimpleDraweeView simpleDraweeView;
    private TextView loginNameView;

    private View logoutView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);

        toolbar.setNavigationOnClickListener(clickListener);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.avatar_view);
        loginNameView = (TextView) findViewById(R.id.login_text_view);

        logoutView = findViewById(R.id.logout_layout);
        logoutView.setOnClickListener(mClickListener);

        String token = sharedPreferences.getString(API.USER.USER_TOKEN, null);
        String name = sharedPreferences.getString(API.USER.USER_NAME, null);
        String avatar = sharedPreferences.getString(API.USER.USER_AVATAR, null);
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(avatar) && !TextUtils.isEmpty(name)) {
            simpleDraweeView.setController(getDraweeController(avatar));
            loginNameView.setText(name);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.logout_layout:
                    showLogoutDialog();

                    break;
            }

        }
    };

    private void showLogoutDialog() {
        new MaterialDialog.Builder(this)
                .content(R.string.logout_prompt)
                .positiveText(R.string.logout_ok)
                .negativeText(R.string.logout_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        if (sharedPreferences != null) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove(API.USER.USER_NAME).remove(API.USER.USER_TOKEN)
                                    .remove(API.USER.USER_AVATAR).remove(API.USER.USER_TYPE)
                                    .apply();
                        }
                        setResult(MyFragment.RESULT_LOGOUT_CODE);
                        finish();
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {

                    }
                })
                .show();
    }

    private DraweeController getDraweeController(String url) {
        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();
        return controller;
    }
}
