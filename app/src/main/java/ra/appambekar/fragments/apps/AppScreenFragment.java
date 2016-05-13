package ra.appambekar.fragments.apps;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

import ra.appambekar.R;
import ra.appambekar.helpers.VolleyHelper;
import ra.appambekar.models.AppScreen;
import ra.smarttextview.SmartTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppScreenFragment extends Fragment {

    private AppScreen mAppScreen = null;

    private SmartTextView mTV_screenTitle;

    public AppScreenFragment() { }
    public AppScreenFragment withAppScreen(AppScreen screen) {
        mAppScreen = screen;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_app_screen, container, false);

        NetworkImageView niv_screen = (NetworkImageView) rootView.findViewById(R.id.niv_screen);
        mTV_screenTitle = (SmartTextView) rootView.findViewById(R.id.tv_screenTitle);

        if (mAppScreen != null) {
            niv_screen.setImageUrl(mAppScreen.getImageURL(), VolleyHelper.getInstance().getImageLoader());
            niv_screen.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) { toastTitle(); }
            });

            mTV_screenTitle.setText(mAppScreen.getTitle());
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toastTitle();
    }

    public void toastTitle() {
        if (mTV_screenTitle == null) return;

        mTV_screenTitle.setAlpha(1);
        mTV_screenTitle.animate().alpha(0.25f).setDuration(500).setStartDelay(1500).start();
    }

}
