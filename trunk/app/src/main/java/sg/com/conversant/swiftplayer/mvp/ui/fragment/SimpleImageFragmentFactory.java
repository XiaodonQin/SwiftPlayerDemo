package sg.com.conversant.swiftplayer.mvp.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;

import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.mvp.data.StreamItem;
import sg.com.conversant.swiftplayer.utils.L;
import sg.com.conversant.swiftplayer.utils.Utils;

public class SimpleImageFragmentFactory {
    private static final String LOG_TAG = SimpleImageFragmentFactory.class.getSimpleName();

    private static final String IMAGE_URL = "image_url";
    private static final String IMAGE_RESOURCE_ID = "image_position";
    private static final String POSTER = "poster";


    public static Fragment newInstance(String imageUrl) {
        Fragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_URL, imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(StreamItem poster) {
        Fragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(POSTER, poster);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(int imageResID) {
        Fragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IMAGE_RESOURCE_ID, imageResID);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static class ImageFragment extends Fragment {
        private Activity mActivity;
        int mImagePosition = 0;
        int mImageResId = -1;
        String mImageUrl;
        StreamItem mPoster;
        ImageView mCover;
        FrameLayout.LayoutParams layoutParams;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            if (context instanceof Activity) {
                mActivity = (Activity) context;
            } else {
                mActivity = getActivity();
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Bundle mBundle = getArguments();
            if (mBundle != null) {
                mImageUrl = mBundle.getString(IMAGE_URL);
                mImagePosition = mBundle.getInt(IMAGE_RESOURCE_ID);
                mPoster = (StreamItem) mBundle.getSerializable(POSTER);
            }

            WindowManager wm = mActivity.getWindowManager();
            Point outSize = new Point();
            wm.getDefaultDisplay().getSize(outSize);

            int cardMargin = mActivity.getResources().getDimensionPixelSize(R.dimen.default_padding);
            int coverWidth = outSize.x - 2 * cardMargin;
            int coverHeight = coverWidth * 9/16;

            layoutParams = new FrameLayout.LayoutParams(coverWidth, coverHeight);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            rootView = LayoutInflater.from(mActivity).inflate(R.layout.simple_image, container,
                    false);
            if (mImageResId <= 0) {
                mImageResId = R.mipmap.default_poster;
            }

            mCover = rootView.findViewById(R.id.image);
            mCover.setLayoutParams(layoutParams);
            if (mPoster != null) {
                Utils.showPosterWithGlide(mPoster.getThumbnailPath(), mCover);
            } else {
                Utils.showPosterWithGlide(mImageResId, mCover);
            }

            mCover.setOnClickListener(v -> viewDetail());

            return rootView;
        }

        public void viewDetail() {
            Intent intent = null;
            Bundle bundle = new Bundle();
            if (mPoster != null) {

            } else {
                L.e(LOG_TAG + " viewDetail mPoster is null");
            }
        }
    }
}
