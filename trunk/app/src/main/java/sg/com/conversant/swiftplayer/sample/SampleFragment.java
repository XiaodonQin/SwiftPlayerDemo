/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sg.com.conversant.swiftplayer.sample;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.bugly.crashreport.CrashReport;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.sdk.API;
import sg.com.conversant.swiftplayer.utils.L;

public class SampleFragment extends Fragment {
    private static final String LOG_TAG = "SampleFragment";

    private static final String ARG_POSITION = "position";

    private Activity mActivity;

    @InjectView(R.id.card_content)
    LinearLayout cardContentLayout;

    @InjectView(R.id.textView)
    TextView mTextView;

    @InjectView(R.id.crash_button)
    Button mCrashButton;

    private int position;
    private boolean mIsTabletMode = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        } else {
            mActivity = getActivity();
        }
    }

    public static SampleFragment newInstance(int position) {
        SampleFragment f = new SampleFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);

        mIsTabletMode = mActivity.getResources().getBoolean(com.roughike.bottombar.R.bool.bb_bottom_bar_is_tablet_mode);
        L.i(LOG_TAG + " mIsTabletMode: " + mIsTabletMode);

        mTextView.setText("Fragment #" + position);
        return rootView;
    }

    public void forceCrash(View view) {
//        throw new RuntimeException("This is a crash");

        CrashReport.testJavaCrash();
    }


}
