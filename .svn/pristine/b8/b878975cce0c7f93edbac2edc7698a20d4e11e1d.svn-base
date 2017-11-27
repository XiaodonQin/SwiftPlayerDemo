package sg.com.conversant.swiftplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.flaviofaria.kenburnsview.KenBurnsView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.PermissionsChecker;

/**
 * Created by Xiaodong on 2015/10/22.
 */
public class SplashActivity extends AppCompatActivity {
    private static String LOG_TAG = "SplashActivity";

    private final int SPLASH_DISPLAY_LENGHT = 6000;

    private SharedPreferences sharedPreferences;
    private PermissionsChecker mPermissionsChecker;

    public static final int REQUEST_CODE = 0; // 请求码
    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    private boolean isRequireCheck; // 是否需要系统权限检测, 防止和系统提示框重叠
    public static final int PERMISSIONS_GRANTED = 0; // 权限授权
    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    @InjectView(R.id.img)
    KenBurnsView kenBurnsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);

        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);

        mPermissionsChecker = new PermissionsChecker(this);
        isRequireCheck = true;

        kenBurnsView.setOnClickListener(clickListener);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (kenBurnsView != null) {
                kenBurnsView.pause();
            }
            SplashActivity.this.finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (isRequireCheck) {
            if (Build.VERSION.SDK_INT >= 23 && mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                requestPermissions(PERMISSIONS); // 请求权限
            } else {
                allPermissionsGranted(); // 全部权限都已获取

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (kenBurnsView != null) {
                            kenBurnsView.pause();
                        }
                        SplashActivity.this.finish();
                    }

                }, SPLASH_DISPLAY_LENGHT);
            }
        } else {
            isRequireCheck = true;
        }
    }

    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    // 全部权限均已获取
    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        L.i(LOG_TAG + " onRequestPermissionsResult");

        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            allPermissionsGranted();
        } else {
            isRequireCheck = false;
            showMissingPermissionDialog();
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title(getResources().getString(R.string.help));
        builder.content(getResources().getString(R.string.string_help_text));
        builder.cancelable(false);
        builder.positiveText(R.string.settings)
                .negativeText(R.string.quit)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        startAppSettings();
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {

                        setResult(PERMISSIONS_DENIED);
                        finish();

                    }
                })
                .show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
}
