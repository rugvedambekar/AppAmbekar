package ra.appambekar.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

import ra.appambekar.fragments.apps.AppScreenFragment;
import ra.appambekar.models.AppScreen;

/**
 * Created by rugvedambekar on 2016-02-24.
 */
public class AppScreensAdapter extends FragmentPagerAdapter {

    private List<AppScreen> mAppScreens;

    public AppScreensAdapter(FragmentManager manager, List<AppScreen> screens) {
        super(manager);
        mAppScreens = screens;
    }

    @Override
    public int getCount() {
        return mAppScreens.size();
    }

    @Override
    public AppScreenFragment getItem(int position) {
        return new AppScreenFragment().withAppScreen(mAppScreens.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mAppScreens.get(position).getTitle();
    }
}
