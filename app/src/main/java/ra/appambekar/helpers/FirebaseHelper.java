package ra.appambekar.helpers;

import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import ra.appambekar.AmbekarApplication;
import ra.appambekar.models.events.LoginEvent;

/**
 * Created by rugvedambekar on 2016-04-12.
 */
public class FirebaseHelper {

    private static final String TAG = FirebaseHelper.class.getSimpleName();

    private static final String RootURL = "https://flickering-torch-7021.firebaseio.com";

    public enum FireChild {
        BaseImagesURL, AppProjects, Contact,
        Info, Screens;

        public String extension() {
            return "/" + name();
        }
        public String infoExtension() { return extension() + Info.extension(); }
        public String screensExtension() { return extension() + Screens.extension(); }
    }

    private static FirebaseHelper mInstance = null;

    private FirebaseHelper() { Firebase.setAndroidContext(AmbekarApplication.GetAppContext()); }

    private Firebase mRootRef = null;

    public static FirebaseHelper getInstance() {
        if (mInstance == null) mInstance = new FirebaseHelper();
        return mInstance;
    }

    public void createAccount(final String fullName, final String email, final String pass, final Runnable onAccountCreated) {
        getRootREF().createUser(email, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                SharedPrefsHelper.getInstance().storeAccountDetails(fullName, email, pass);
                Toast.makeText(AmbekarApplication.GetAppContext(), "You have been registered!", Toast.LENGTH_SHORT).show();

                if (onAccountCreated != null) onAccountCreated.run();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Log.d(TAG, firebaseError.toString());
            }
        });
    }

    public void authenticate(final String email, final String pass) {
        getRootREF().authWithPassword(email, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d(TAG, "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                if (!SharedPrefsHelper.getInstance().getUserInfo(SharedPrefsHelper.UserInfo.Email).equals(email)) {
                    SharedPrefsHelper.getInstance().storeAccountDetails("", email, pass);
                }
                EventBus.getDefault().post(new LoginEvent(true));
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                EventBus.getDefault().post(new LoginEvent(firebaseError.getMessage()));
                Log.d(TAG, firebaseError.toString());
            }
        });
    }
    public void unAuthenticate() { getRootREF().unauth(); }
    public boolean isAuthenticated() { return getRootREF().getAuth() != null; }

    public Firebase getRootREF() {
        if (mRootRef == null) mRootRef = new Firebase(RootURL);
        return mRootRef;
    }

    public Firebase getChildREF(FireChild child) {
        if (child == null || getRootREF() == null) return null;

        return getChildREF(child.extension());
    }
    public Firebase getChildREF(String childExt) {
        if (childExt == null || getRootREF() == null) return null;

        return getRootREF().child(childExt);
    }
}
