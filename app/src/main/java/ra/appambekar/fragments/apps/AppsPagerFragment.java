package ra.appambekar.fragments.apps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import ra.appambekar.R;
import ra.appambekar.adapters.AppsAdapter;
import ra.appambekar.models.AppInfo;

/**
 * Created by rugvedambekar on 2016-04-20.
 */
public class AppsPagerFragment extends Fragment {

    private List<AppInfo> mAppInfoList;

    public AppsPagerFragment forApps(List<AppInfo> appInfoList) {
        mAppInfoList = appInfoList;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_full_pager, container, false);

        ViewPager appsPager = (ViewPager) rootView.findViewById(R.id.viewPager_main);
        appsPager.setAdapter(new AppsAdapter(getChildFragmentManager(), mAppInfoList));

        CirclePageIndicator pageIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator_main);
        pageIndicator.setViewPager(appsPager);

        return rootView;
    }
}
