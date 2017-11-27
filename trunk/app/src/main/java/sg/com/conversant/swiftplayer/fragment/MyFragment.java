/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/4/14.
 */
package sg.com.conversant.swiftplayer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.zendesk.sdk.feedback.ui.ContactZendeskActivity;
import com.zendesk.sdk.support.SupportActivity;
import com.zendesk.sdk.requests.RequestActivity;

import io.doorbell.android.Doorbell;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.activity.CustomFeedbackActivity;
import sg.com.conversant.swiftplayer.activity.LoginActivity;
import sg.com.conversant.swiftplayer.activity.MainActivity;
import sg.com.conversant.swiftplayer.activity.MyProfileActivity;
import sg.com.conversant.swiftplayer.activity.OpenSourceDetailActivity;
import sg.com.conversant.swiftplayer.activity.SettingActivity;
import sg.com.conversant.swiftplayer.activity.ZendeskFeedbackActivity;
import sg.com.conversant.swiftplayer.sample.TestActivity;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * TODO
 *
 * @author Xiaodong
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    private static final String LOG_TAG = "MyFragment";
    public static final int REQUEST_LOGIN_CODE = 1;
    public static final int REQUEST_PROFILE_CODE = 2;
    public static final int RESULT_LOGIN_CODE = 1;
    public static final int RESULT_LOGOUT_CODE = 2;
    private View mRootView;

    private SimpleDraweeView simpleDraweeView;
    private TextView loginNameView;

    private TextView versionView;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_my, container, false);

        View mProfileView = mRootView.findViewById(R.id.my_profile_layout);
        View mSettingsView = mRootView.findViewById(R.id.my_setting_layout);
        View mTestView = mRootView.findViewById(R.id.test_layout);
        View mGitHubView = mRootView.findViewById(R.id.open_source_layout);
        View mDoorbellFeedbackView = mRootView.findViewById(R.id.doorbell_feedback_layout);
        View mZendeskFeedbackView = mRootView.findViewById(R.id.zendesk_feedback_layout);
        View mCustomFeedbackView = mRootView.findViewById(R.id.custom_feedback_layout);
        mProfileView.setOnClickListener(this);
        mSettingsView.setOnClickListener(this);
        mTestView.setOnClickListener(this);
        mGitHubView.setOnClickListener(this);
        mDoorbellFeedbackView.setOnClickListener(this);
        mZendeskFeedbackView.setOnClickListener(this);
        mCustomFeedbackView.setOnClickListener(this);

        simpleDraweeView = (SimpleDraweeView) mRootView.findViewById(R.id.avatar_view);
        loginNameView = (TextView) mRootView.findViewById(R.id.login_text_view);

        versionView = (TextView) mRootView.findViewById(R.id.version);

        String token = sharedPreferences.getString(API.USER.USER_TOKEN, null);
        String name = sharedPreferences.getString(API.USER.USER_NAME, null);
        String avatar = sharedPreferences.getString(API.USER.USER_AVATAR, null);
        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(avatar) && !TextUtils.isEmpty(name)) {
            simpleDraweeView.setController(getDraweeController(avatar));
            loginNameView.setText(name);
        }

        try {
            PackageInfo pInfo = mActivity.getPackageManager().getPackageInfo(
                    mActivity.getPackageName(), 0);

            String versionName = pInfo.versionName;

            versionView.setText(getResources().getString(R.string.version, versionName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        printPX();

        return mRootView;
    }

    private void printPX() {
        /**
         * 计算工具
         */
        L.i(LOG_TAG + " Device: " + Utils.getDeviceBrand() + " " + Utils.getUserAgent());
        L.i(LOG_TAG + " AndroidOS: " + Utils.getAndroidVersionName());
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        L.i(LOG_TAG + " status bar height: " + statusBarHeight1);

        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //通常我们在使用DisplayMetrics时，都是直接获取内部变量来使用。所以下面直接列出各个内部变量。

        float screenYdpi = dm.ydpi;     //得到物理屏幕上 Y 轴方向每英寸的像素
        float screenXdpi = dm.xdpi;     //得到物理屏幕上 X 轴方向每英寸的像素
        //ps:  其实这两个大多数情况下都是相同的
        //你能想象上面像素密度大很清晰 下面密度小跟马赛克一样吗 233333

        float density = dm.density;           //获取当前设备的基准比例
        int densityDpi = dm.densityDpi;        //获取系统dpi，随着 build.prop 文件中的代码而改变。

        int screenWidthPixels = dm.widthPixels;       //获取屏幕宽度的像素数量

        //获取屏幕高度的像素数量！
        //注意 - 因为这里会自动减去32dp的像素数量，根据分辨率不同的设备，减去的像素数量也不同，但是可以根据公式推算完整（px = dp x 基准比例）。
        /* 为啥不用dm.densityDpi / 160 得到基准比例？
         * 因为那个会随着build.prop文件代码变更而更改，算出来的不一定准确
        */
        float screenHeightPixels = dm.heightPixels + 32 * dm.ydpi / 160;
        L.i(LOG_TAG + " screenYdpi: " + screenYdpi + ", screenXdpi: " + screenXdpi + "\n"
                + "density: " + density + ", densityDpi: " + densityDpi + "\n"
                + "screenWidthPixels: " + screenWidthPixels + ", screenHeightPixels: " + screenHeightPixels);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = new Intent();
        switch (v.getId()) {
            case R.id.my_profile_layout:

                if (TextUtils.isEmpty(sharedPreferences.getString(API.USER.USER_TOKEN, null))) {
                    mIntent.setClassName(mActivity, LoginActivity.class.getName());
                    startActivityForResult(mIntent, REQUEST_LOGIN_CODE);
                } else {
                    mIntent.setClassName(mActivity, MyProfileActivity.class.getName());
                    startActivityForResult(mIntent, REQUEST_PROFILE_CODE);
                }


                break;

            case R.id.my_setting_layout:

                mIntent.setClassName(mActivity, SettingActivity.class.getName());
                startActivity(mIntent);

                break;

            case R.id.test_layout:

                mIntent.setClassName(mActivity, TestActivity.class.getName());
                startActivity(mIntent);

                break;

            case R.id.open_source_layout:

                mIntent.setClassName(mActivity, OpenSourceDetailActivity.class.getName());
                startActivity(mIntent);

                break;

            case R.id.doorbell_feedback_layout:

                new Doorbell(mActivity, 6835, "RvIgQbnFrndZ4FiknSYGCgABMoRQNlPtQmcdlBDhxD11EwHulgdKxqlLeuaWas2h").show();

                break;

            case R.id.zendesk_feedback_layout:

                // Start SupportActivity to browse Help Center and create/update requests**
//                new SupportActivity.Builder().show(mActivity);

//                mIntent.setClassName(mActivity, ContactZendeskActivity.class.getName());
                mIntent.setClassName(mActivity, ZendeskFeedbackActivity.class.getName());
                startActivity(mIntent);
                break;

            case R.id.custom_feedback_layout:
                mIntent.setClassName(mActivity, CustomFeedbackActivity.class.getName());
                startActivity(mIntent);
                break;
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MyFragment.REQUEST_LOGIN_CODE && resultCode == MyFragment.RESULT_LOGIN_CODE) {
            String token = sharedPreferences.getString(API.USER.USER_TOKEN, null);
            String name = sharedPreferences.getString(API.USER.USER_NAME, null);
            String avatar = sharedPreferences.getString(API.USER.USER_AVATAR, null);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(avatar) && !TextUtils.isEmpty(name)) {
                simpleDraweeView.setController(getDraweeController(avatar));
                loginNameView.setText(name);
            }
        } else if (requestCode == MyFragment.REQUEST_PROFILE_CODE && resultCode == MyFragment.RESULT_LOGOUT_CODE) {
            simpleDraweeView.setController(getDraweeController(""));
            loginNameView.setText(R.string.my_login_prompt);
        }
    }
}
