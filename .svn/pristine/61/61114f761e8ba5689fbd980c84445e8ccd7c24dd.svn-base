package sg.com.conversant.swiftplayer.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.sdk.Constants;

/**
 * Created by Xiaodong on 2017/3/15.
 */

public class SettingActivity extends AppCompatActivity {
    private static String LOG_TAG = "SettingActivity";

    private SharedPreferences sharedPreferences;
    private String serverUrl;

    private ScrollView rootView;
    private AutoCompleteTextView serverView;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        serverUrl = sharedPreferences.getString(API.CONFIG.SERVER_URL, Constants.SERVER_URL);

        setupUI();
    }

    private void setupUI() {
        rootView = (ScrollView) findViewById(R.id.setting_form);
        serverView = (AutoCompleteTextView) findViewById(R.id.server);
        saveButton = (Button) findViewById(R.id.save_button);

        serverView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.save || id == EditorInfo.IME_NULL) {
                    attemptSave();
                    return true;
                }
                return false;
            }
        });
        serverView.setText(serverUrl);
        serverView.setSelection(serverUrl.length());
        saveButton.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save_button:
                    attemptSave();
                    break;
            }
        }
    };

    private void attemptSave() {
        // Reset errors.
        serverView.setError(null);

        // Store values at the time of the login attempt.
        String server = serverView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(server)) {
            serverView.setError(getString(R.string.error_field_required));
            focusView = serverView;
            cancel = true;
        } else if (!isWebUrlValid(server)) {
            serverView.setError(getString(R.string.error_invalid_web));
            focusView = serverView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(serverView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(API.CONFIG.SERVER_URL, server);
            editor.apply();

            Constants.SERVER_URL = server;
            Snackbar.make(rootView, getResources().getString(R.string.save_server_success), Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean isWebUrlValid(String serverUrl) {
        //TODO: Replace this with your own logic
        return (serverUrl.startsWith("http://") || serverUrl.startsWith("https://")) && serverUrl.endsWith("/");
    }

}
