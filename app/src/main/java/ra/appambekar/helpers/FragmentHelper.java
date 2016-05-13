package ra.appambekar.helpers;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by rugvedambekar on 2016-02-22.
 */
public class FragmentHelper {

    private FragmentActivity mActivity;

    public FragmentHelper(FragmentActivity activity) {
        mActivity = activity;
    }

    public void startFragment(int containerId, Fragment frag) {
        if (mActivity == null) return;

        FragmentManager fManager = mActivity.getSupportFragmentManager();
        fManager.beginTransaction().replace(containerId, frag).commit();
    }
}
