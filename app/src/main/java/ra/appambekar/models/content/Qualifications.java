package ra.appambekar.models.content;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import ra.appambekar.R;
import ra.appambekar.adapters.base.BaseFragPagerAdapter;
import ra.appambekar.fragments.qualifications.ExperienceFragment;
import ra.appambekar.fragments.qualifications.WorkEthicFragment;

/**
 * Created by rugvedambekar on 2016-02-24.
 */
public enum Qualifications {
    Experience(R.string.q_experience),
    WorkEthic(R.string.q_workethic);

    private int TitleId;

    private Qualifications(int rId) { TitleId = rId; }

    public static FragmentPagerAdapter getAdapter(Fragment parentFragment) {

        return new BaseFragPagerAdapter(parentFragment) {

            @Override
            public int getCount() {
                return Qualifications.values().length;
            }

            @Override
            public Fragment getItem(int position) {
                switch (Qualifications.values()[position]) {
                    case Experience:
                        return new ExperienceFragment();
                    case WorkEthic:
                        return new WorkEthicFragment();
                }

                return null;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mContext.getString(Qualifications.values()[position].TitleId);
            }
        };
    }
}
