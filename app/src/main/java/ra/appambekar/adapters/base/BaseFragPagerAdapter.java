package ra.appambekar.adapters.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by rugvedambekar on 2016-04-22.
 */
public abstract class BaseFragPagerAdapter extends FragmentPagerAdapter {

    protected Context mContext;

    public BaseFragPagerAdapter(Fragment parentFragment) {
        super(parentFragment.getChildFragmentManager());

        mContext = parentFragment.getActivity();
    }

}
