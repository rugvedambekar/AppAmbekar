package ra.appambekar.fragments.apps;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import ra.appambekar.R;
import ra.appambekar.activities.AppScreenActivity;
import ra.appambekar.helpers.FirebaseHelper;
import ra.appambekar.helpers.VolleyHelper;
import ra.appambekar.models.AppInfo;
import ra.appambekar.models.AppScreen;
import ra.appambekar.models.comparators.AppScreenComparator;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;


public class AppProfileFragment extends Fragment {

    private static final String TAG = AppProfileFragment.class.getSimpleName();

    private AppInfo mAppInfo;
    private ArrayList<AppScreen> mAppScreens;

    private LinearLayout mAppScreensLayout;
    private NetworkImageView mAppLogo;
    private SmartTextView mAppTitle, mAppSummary, mGooglePlay;

    public AppProfileFragment() { }
    public AppProfileFragment forApp(AppInfo appInfo) {
        mAppInfo = appInfo;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mAppInfo == null || !mAppInfo.getShowcase()) return;

        mAppScreens = new ArrayList<>();
        FirebaseHelper.getInstance().getChildREF(mAppInfo.getScreensEXT()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSS : dataSnapshot.getChildren()) {
                    mAppScreens.add(dataSS.getValue(AppScreen.class));
                }

                loadAppScreens();
            }

            @Override public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_profile, container, false);

        mAppScreensLayout = (LinearLayout) rootView.findViewById(R.id.ll_screenThumbs);
        mAppTitle = (SmartTextView) rootView.findViewById(R.id.tv_appTitle);
        mAppLogo = (NetworkImageView) rootView.findViewById(R.id.iv_appLogo);
        mAppSummary = (SmartTextView) rootView.findViewById(R.id.tv_appSummary);
        mGooglePlay = (SmartTextView) rootView.findViewById(R.id.tv_googlePlayLink);

        loadAppInfo();

        if (mAppInfo.getShowcase()) loadAppScreens();
        else rootView.findViewById(R.id.container_screens).setVisibility(View.GONE);

        return rootView;
    }

    private void loadAppInfo() {
        if (mAppInfo == null) return;

        if (mAppLogo != null) mAppLogo.setImageUrl(mAppInfo.getLogoURL(), VolleyHelper.getInstance().getImageLoader());
        if (mAppTitle != null) mAppTitle.setText(mAppInfo.getTitle());
        if (mAppSummary != null) mAppSummary.setText(mAppInfo.getSummary());
        if (mGooglePlay != null) {
            if (mAppInfo.getGooglePlayLink() == null) {
                mGooglePlay.setText(R.string.link_google_play_na);
                mGooglePlay.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                mGooglePlay.setText(R.string.link_google_play);
                mGooglePlay.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                mGooglePlay.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mAppInfo.getGooglePlayLink())));
                    }
                });
            }
        }
    }

    private void loadAppScreens() {
        if (mAppScreensLayout == null || mAppScreens == null || mAppScreens.isEmpty()) return;

        Collections.sort(mAppScreens, new AppScreenComparator());
        mAppScreensLayout.removeAllViews();

        FirebaseHelper.getInstance().getChildREF(mAppInfo.getScreensEXT());
        for (AppScreen screen : mAppScreens) mAppScreensLayout.addView(getThumbView(screen));
    }

    private NetworkImageView getThumbView(final AppScreen screen) {
        NetworkImageView niv_screenThumb = new NetworkImageView(getActivity());
        int thumbHeight = mAppScreensLayout.getMeasuredHeight() - mAppScreensLayout.getPaddingBottom() - mAppScreensLayout.getPaddingTop();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, thumbHeight);
        layoutParams.setMargins(0, 0, (int) LayoutUtils.Convert.DpToPx(20, getActivity()), 0);

        niv_screenThumb.setLayoutParams(layoutParams);
        niv_screenThumb.setImageUrl(screen.getThumbnailURL(thumbHeight), VolleyHelper.getInstance().getImageLoader());
        niv_screenThumb.setScaleType(ImageView.ScaleType.FIT_CENTER);
        niv_screenThumb.setBackgroundColor(Color.DKGRAY);

        niv_screenThumb.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent fullViewIntent = new Intent(getActivity(), AppScreenActivity.class);

                Bundle screensBundle = new Bundle();
                screensBundle.putInt(AppScreenActivity.KEY_CURR_INDEX, mAppScreens.indexOf(screen));
                screensBundle.putParcelableArrayList(AppScreenActivity.KEY_SCREENS, mAppScreens);

                fullViewIntent.putExtras(screensBundle);
                getActivity().startActivity(fullViewIntent);
            }
        });

        Log.d(TAG, "Added Thumbnail: " + screen.getTitle() + " (" + screen.getThumbnailURL(thumbHeight) + ")");

        return niv_screenThumb;
    }
}
