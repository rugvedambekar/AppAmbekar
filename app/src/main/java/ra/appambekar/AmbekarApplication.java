package ra.appambekar;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import ra.appambekar.helpers.CloudinaryHelper;
import ra.appambekar.helpers.FirebaseHelper;
import ra.appambekar.helpers.VolleyHelper;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;

/**
 * Created by rugvedambekar on 2016-02-23.
 */
public class AmbekarApplication extends Application {

    private static Context mApplicationContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationContext = getApplicationContext();

        AssetManager assets = getAssets();
        SmartTextView.registerFont(SmartTextView.FontType.Thin, Typeface.createFromAsset(assets, "fonts/Roboto-Thin.ttf"));
        SmartTextView.registerFont(SmartTextView.FontType.Reg, Typeface.createFromAsset(assets, "fonts/Roboto-Light.ttf"));
        SmartTextView.registerFont(SmartTextView.FontType.Thick, Typeface.createFromAsset(assets, "fonts/Roboto-Reg.ttf"));

        FirebaseHelper.getInstance();
        VolleyHelper.getInstance();
        CloudinaryHelper.getInstance();
    }

    public static Context getAppContext() {
        return mApplicationContext;
    }

    public static boolean hasActiveConnection() {
        if (mApplicationContext == null) return false;

        ConnectivityManager conMgr = (ConnectivityManager) mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }
}
