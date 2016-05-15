package ra.appambekar.fragments.qualifications;


import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import ra.appambekar.R;
import ra.appambekar.activities.BaseToolbarActivity;
import ra.appambekar.fragments.BaseAsyncFragment;
import ra.appambekar.models.content.Qualifications;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;

public class QualificationsFragment extends BaseAsyncFragment {

    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabStrip;

    private FragmentPagerAdapter mAdapter;

    public QualificationsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qualifications, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager_qualifications);

        mTabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabStrip_qualifications);
        mTabStrip.setTypeface(SmartTextView.getFontTypeface(SmartTextView.FontType.Thin), Typeface.BOLD);
        mTabStrip.setIndicatorHeight((int) LayoutUtils.Convert.DpToPx(4, getActivity()));
        mTabStrip.setTextSize((int) LayoutUtils.Convert.SpToPx(17, getActivity()));
        mTabStrip.setTextColorResource(android.R.color.primary_text_dark);
        mTabStrip.setIndicatorColorResource(R.color.toolbar_light);
        mTabStrip.setDividerColorResource(R.color.toolbar_main);

        return rootView;
    }

    @Override
    protected void doInBackground() {
        mAdapter = Qualifications.getAdapter(this);
    }

    @Override
    protected void onPostExecute() {
        mViewPager.setAdapter(mAdapter);
        mTabStrip.setViewPager(mViewPager);
    }
}
