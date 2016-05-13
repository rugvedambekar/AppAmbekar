package ra.appambekar.models.content;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import ra.appambekar.R;
import ra.appambekar.adapters.base.BaseFragPagerAdapter;
import ra.appambekar.fragments.android.DevelopmentFragment;
import ra.appambekar.fragments.android.LibrariesFragment;

/**
 * Created by rugvedambekar on 2016-04-22.
 */
public enum Android {
    Development(R.string.a_development),
    Libraries(R.string.a_libraries);

    private int TitleId;
    private Android(int rId) { TitleId = rId; }

    public static FragmentPagerAdapter getAdapter(Fragment parentFragment) {

        return new BaseFragPagerAdapter(parentFragment) {

            @Override
            public int getCount() {
                return Android.values().length;
            }

            @Override
            public Fragment getItem(int position) {
                switch (Android.values()[position]) {
                    case Development:
                        return new DevelopmentFragment();
                    case Libraries:
                        return new LibrariesFragment();
                }

                return null;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mContext.getString(Android.values()[position].TitleId);
            }
        };
    }
}
