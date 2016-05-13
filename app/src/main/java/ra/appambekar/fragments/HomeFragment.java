package ra.appambekar.fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import ra.appambekar.R;
import ra.appambekar.views.ParallaxSkyLayout;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ParallaxSkyLayout mParallaxSky;
    private ScrollView mScrollContainer;

    private boolean mScrollLocked;

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mParallaxSky = (ParallaxSkyLayout) rootView.findViewById(R.id.pl_background);
        mScrollContainer = (ScrollView) rootView.findViewById(R.id.scrollContainer_intro);

        mParallaxSky.registerScrollView(mScrollContainer);
        mScrollContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                return mScrollLocked;
            }
        });

        mScrollLocked = true;

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mParallaxSky.destroy();
        mScrollLocked = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mParallaxSky.build(new ParallaxSkyLayout.BuildObserver() {
            @Override public void onComplete() {
                mScrollLocked = false;
            }
        });
    }
}
