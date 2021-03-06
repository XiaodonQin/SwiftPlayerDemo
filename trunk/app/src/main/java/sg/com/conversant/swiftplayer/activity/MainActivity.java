/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p/>
 * Created on 2016/3/22.
 */
package sg.com.conversant.swiftplayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import sg.com.conversant.swiftplayer.MainUIConfiguration;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.fragment.RecommendFragment;
import sg.com.conversant.swiftplayer.jpush.ExampleUtil;
import sg.com.conversant.swiftplayer.jpush.LocalBroadcastManager;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.Utils;

/**
 * TODO
 *
 * @author Xiaodong
 */
public class MainActivity extends AppCompatActivity {
    private static String LOG_TAG = "MainActivity";
    public static boolean isForeground = false;

    public static final String START_SPLASH = "start_splash";
    public static final int START_NO_SPLASH = 1;

    private long firstTime = 0;

    private BottomBar mBottomBar;

    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.search_btn)
    ImageView mSearchBtn;

    @InjectView(R.id.notification_btn)
    ImageView mNotificationBtn;

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        // before restart this app,show splash activity
        if (intent != null
                && intent.getIntExtra(START_SPLASH, 0) == START_NO_SPLASH) {

        } else {
            Intent mIntentToSplash = new Intent(this, SplashActivity.class);
            startActivityForResult(mIntentToSplash, SplashActivity.REQUEST_CODE, null);
        }

        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mFragmentManager = getSupportFragmentManager();
        mCurrentFragment = new RecommendFragment();

        setupUI();
        setupBottomBar(savedInstanceState);
        registerMessageReceiver();  // used for receive msg
    }

    private void setupUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setContentInsetsAbsolute(0, 0);

        mSearchBtn.setOnClickListener(clickListener);
        mNotificationBtn.setOnClickListener(clickListener);
    }

    private void setupBottomBar(Bundle bundle) {
        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.main_content), findViewById(R.id.recycler_view), bundle);
        mBottomBar.setDefaultTabPosition(0);
        mBottomBar.noTabletGoodness();
        mBottomBar.useFixedMode();
        mBottomBar.noNavBarGoodness();
        mBottomBar.noResizeGoodness();
        mBottomBar.useOnlyStatusBarTopOffset();

        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, menuTabClickListener);

        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.white));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.white));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.white));
        mBottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.white));
    }

    OnMenuTabClickListener menuTabClickListener = new OnMenuTabClickListener() {
        @Override
        public void onMenuTabSelected(@IdRes int menuItemId) {
            showContentFragment(menuItemId);
        }

        @Override
        public void onMenuTabReSelected(@IdRes int menuItemId) {

        }
    };

    View.OnClickListener clickListener = v -> {
        switch (v.getId()) {
            case R.id.search_btn:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.notification_btn:
                break;
        }
    };

    public void setBottomBarMenuSelected(int position) {
        if (mBottomBar != null) {
            mBottomBar.selectTabAtPosition(position, false);
        }
    }

    private void showContentFragment(int key) {
        Fragment fragment = getFragment(key);
        String fragmentName = MainUIConfiguration.getFragmentName(key);
        if (mCurrentFragment != fragment) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            if (!fragment.isAdded()) {
                fragmentTransaction.hide(mCurrentFragment).add(R.id.main_container, fragment, fragmentName)
                        .commit();
            } else {
                fragmentTransaction.hide(mCurrentFragment).show(fragment).commit();
            }

            mCurrentFragment = fragment;
        }

    }

    private Fragment getFragment(int id) {
        String fragmentName = MainUIConfiguration.getFragmentName(id);
        Fragment fragment = mFragmentManager.findFragmentByTag(fragmentName);

        if (fragment != null) {
            return fragment;
        }

        return Utils.getFragment(fragmentName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == SplashActivity.REQUEST_CODE && resultCode == SplashActivity.PERMISSIONS_DENIED) {
            finish();
        }

        if (mCurrentFragment != null) {
            mCurrentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {//如果两次按键时间间隔大于800毫秒，则不退出
                Toast toast = Toast.makeText(MainActivity.this, R.string.exit_prompt,
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                firstTime = secondTime;//更新firstTime
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();

        if (mToolbar != null) mToolbar.getBackground().setAlpha(255);
        JPushInterface.onResume(this);
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        JPushInterface.onPause(this);
    }


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    L.i(LOG_TAG + " show message: " + showMsg);
                }
            } catch (Exception e){
            }
        }
    }
}
