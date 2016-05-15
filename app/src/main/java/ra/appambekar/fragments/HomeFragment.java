package ra.appambekar.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import ra.appambekar.R;
import ra.appambekar.activities.BaseToolbarActivity;
import ra.appambekar.views.ParallaxSkyLayout;
import ra.smarttextview.SmartTextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ParallaxSkyLayout mParallaxSky;
    private SmartTextView mContactInfo;

    private boolean mScrollLocked;

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mContactInfo = (SmartTextView) rootView.findViewById(R.id.tv_contact);
        mParallaxSky = (ParallaxSkyLayout) rootView.findViewById(R.id.pl_background);
        ScrollView scrollContainer = (ScrollView) rootView.findViewById(R.id.scrollContainer_intro);

        mParallaxSky.registerScrollView(scrollContainer);
        scrollContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return mScrollLocked;
            }
        });

        mContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ((BaseToolbarActivity) getActivity()).addContactFragment();
                mContactInfo.animate().alpha(0.25f).setDuration(150).withEndAction(new Runnable() {
                    @Override public void run() { mContactInfo.animate().alpha(1).setDuration(150).start(); }
                }).start();
            }
        });

        mScrollLocked = true;

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mParallaxSky.destroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        mParallaxSky.destroySky();
        mScrollLocked = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mParallaxSky.buildSky(new ParallaxSkyLayout.BuildObserver() {
            @Override public void onComplete() {
                mScrollLocked = false;
            }
        });
    }
}
