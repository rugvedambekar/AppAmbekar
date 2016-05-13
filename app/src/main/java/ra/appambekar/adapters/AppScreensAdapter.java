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
    private HashMap<Integer, AppScreenFragment> mFragmentREFs;

    public AppScreensAdapter(FragmentManager manager, List<AppScreen> screens) {
        super(manager);

        mAppScreens = screens;
        mFragmentREFs = new HashMap<>();
    }

    @Override
    public int getCount() {
        return mAppScreens.size();
    }

    @Override
    public AppScreenFragment getItem(int position) {
        AppScreen thisScreen = mAppScreens.get(position);
        mFragmentREFs.put(position, new AppScreenFragment().withAppScreen(thisScreen));

        return mFragmentREFs.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove reference to maintain memory efficiency of adapter;
        mFragmentREFs.remove(position);

        super.destroyItem(container, position, object);
    }

    public AppScreenFragment getFragmentReference(int position) {
        return mFragmentREFs.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mAppScreens.get(position).getTitle();
    }
}
