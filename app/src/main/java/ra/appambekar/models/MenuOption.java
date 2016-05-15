package ra.appambekar.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rugvedambekar on 2016-04-14.
 */
public class MenuOption {

    private int mTitleID = -1;
    private String mTitle = "";

    private boolean mNoConnection = false, mIsHeading = false;
    private ArrayList<AppInfo> mAppInfoList = null;

    public MenuOption() { }
    public MenuOption(int titleId) {
        mTitleID = titleId;
    }
    public MenuOption(String title) {
        mTitle = title;
    }

    public MenuOption forHeading() {
        mIsHeading = true;
        return this;
    }
    public MenuOption forNoConnection() {
        mNoConnection = true;
        return this;
    }
    public MenuOption withApp(AppInfo appInfo) {
        if (mAppInfoList == null) mAppInfoList = new ArrayList<>();

        mAppInfoList.add(appInfo);
        return this;
    }

    public int getTitleID() { return mTitleID; }
    public String getTitle() { return mTitle; }

    public List<AppInfo> getAppInfoList() { return mAppInfoList; }
    public AppInfo getAppInfo() { return mAppInfoList == null ? null : mAppInfoList.get(0); }
    public int getAppCount() { return mAppInfoList == null ? 0 : mAppInfoList.size(); }

    public boolean isHeading() { return mIsHeading; }
    public boolean isNoConnection() { return mNoConnection; }
    public boolean isApp() { return mAppInfoList != null; }
}