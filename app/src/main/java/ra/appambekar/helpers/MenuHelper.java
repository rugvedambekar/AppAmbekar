package ra.appambekar.helpers;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ra.appambekar.AmbekarApplication;
import ra.appambekar.R;
import ra.appambekar.models.AppInfo;
import ra.appambekar.models.MenuOption;

import static ra.appambekar.helpers.FirebaseHelper.*;

/**
 * Created by rugvedambekar on 2016-02-22.
 */
public class MenuHelper {

    private static final String TAG = MenuHelper.class.getSimpleName();

    private static MenuHelper mInstance = null;

    public static MenuHelper getInstance() {
        if (mInstance == null) mInstance = new MenuHelper();

        return mInstance;
    }

    private ArrayList<MenuOption> mAllOptions;
    private MenuOption mOtherWorkOption = null;

    private MenuHelper() {
        mAllOptions = new ArrayList<>();

        loadStaticMenu();
        loadDynamicMenu();
    }

    private void loadStaticMenu() {
        mAllOptions.add(new MenuOption(R.string.m_home));
        mAllOptions.add(new MenuOption(R.string.m_qualifications));
        mAllOptions.add(new MenuOption(R.string.m_apps));
        mAllOptions.add(new MenuOption(R.string.m_work).forHeading());
    }

    private void loadDynamicMenu() {
        if (!AmbekarApplication.hasActiveConnection()) {
            mAllOptions.add(new MenuOption().forNoConnection());

        } else FirebaseHelper.getInstance().getChildREF(FireChild.AppProjects).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSS : dataSnapshot.getChildren()) {
                    AppInfo appInfo = dataSS.child(FireChild.Info.extension()).getValue(AppInfo.class);
                    appInfo.setAppFirebaseEXT(dataSS.getKey());

                    if (appInfo.getShowcase()) mAllOptions.add(new MenuOption(dataSS.getKey()).withApp(appInfo));
                    else addOtherWork(appInfo);
                }

                if (mOtherWorkOption != null) mAllOptions.add(mOtherWorkOption);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.toString());
            }
        });
    }

    private void addOtherWork(AppInfo appInfo) {
        if (mOtherWorkOption == null) mOtherWorkOption = new MenuOption(R.string.m_more_work).withoutIndex();
        mOtherWorkOption.withApp(appInfo);
    }

    public List<MenuOption> getOptions() { return mAllOptions; }

}