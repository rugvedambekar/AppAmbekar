package ra.appambekar.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import ra.appambekar.fragments.apps.AppProfileFragment;
import ra.appambekar.fragments.apps.AppScreenFragment;
import ra.appambekar.models.AppInfo;
import ra.appambekar.models.AppScreen;

/**
 * Created by rugvedambekar on 2016-02-24.
 */
public class AppsAdapter extends FragmentPagerAdapter {

    private List<AppInfo> mAppInfoList;

    public AppsAdapter(FragmentManager manager, List<AppInfo> apps) {
        super(manager);
        mAppInfoList = apps;
    }

    @Override
    public int getCount() {
        return mAppInfoList.size();
    }

    @Override
    public Fragment getItem(int position) {
        AppInfo thisApp = mAppInfoList.get(position);
        return new AppProfileFragment().forApp(thisApp);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mAppInfoList.get(position).getTitle();
    }
}
