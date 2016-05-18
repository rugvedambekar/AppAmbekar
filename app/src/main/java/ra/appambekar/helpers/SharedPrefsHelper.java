package ra.appambekar.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import ra.appambekar.AmbekarApplication;
import ra.appambekar.R;

/**
 * Created by rugvedambekar on 2016-04-12.
 */
public class SharedPrefsHelper {

    private static final String TAG = SharedPrefsHelper.class.getSimpleName();

    private static final String FileIdentifier = "AppAmbekarSharedPrefs";

    private static SharedPrefsHelper mInstance = null;

    public static SharedPrefsHelper getInstance() {
        if (mInstance == null) mInstance = new SharedPrefsHelper();
        return mInstance;
    }

    private SharedPreferences mSharedPreferences;

    private SharedPrefsHelper() {
        mSharedPreferences = AmbekarApplication.getAppContext().getSharedPreferences(FileIdentifier, Context.MODE_PRIVATE);
    }

    public void storeAccountDetails(String fullName, String email, String pass) {
        SharedPreferences.Editor prefsEditor = mSharedPreferences.edit();
        prefsEditor.putString(UserInfo.FullName.name(), fullName);
        prefsEditor.putString(UserInfo.Email.name(), email);
        prefsEditor.putString(UserInfo.Password.name(), pass);
        prefsEditor.commit();
    }

    public String getUserInfo(UserInfo info) { return mSharedPreferences.getString(info.name(), ""); }

    public enum UserInfo { FullName, Email, Password }

}
