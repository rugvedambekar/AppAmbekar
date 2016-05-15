package ra.appambekar.fragments.android;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import ra.appambekar.R;
import ra.appambekar.fragments.BaseAsyncFragment;
import ra.appambekar.models.content.Android;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class AndroidFragment extends BaseAsyncFragment {

    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabStrip;

    private FragmentPagerAdapter mAdapter;

    public AndroidFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_android, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager_android);
        mTabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabStrip_android);

        mTabStrip.setTypeface(SmartTextView.getFontTypeface(SmartTextView.FontType.Reg), Typeface.NORMAL);
        mTabStrip.setIndicatorHeight((int) LayoutUtils.Convert.DpToPx(2, getActivity()));
        mTabStrip.setTextSize((int) LayoutUtils.Convert.SpToPx(14, getActivity()));
        mTabStrip.setTextColorResource(android.R.color.tertiary_text_light);
        mTabStrip.setIndicatorColorResource(R.color.toolbar_light);
        mTabStrip.setDividerColorResource(R.color.toolbar_light);
        mTabStrip.setShouldExpand(true);

        return rootView;
    }

    @Override
    protected void doInBackground() {
        mAdapter = Android.getAdapter(this);
    }

    @Override
    protected void onPostExecute() {
        mViewPager.setAdapter(mAdapter);
        mTabStrip.setViewPager(mViewPager);
    }
}
