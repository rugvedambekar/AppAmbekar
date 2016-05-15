package ra.appambekar.helpers;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import ra.appambekar.AmbekarApplication;

/**
 * Created by rugvedambekar on 2016-04-12.
 */
public class FirebaseHelper {

    private static final String TAG = FirebaseHelper.class.getSimpleName();

    private static final String RootURL = "https://flickering-torch-7021.firebaseio.com";

    public enum FireChild {
        BaseImagesURL, AppProjects, Contact,
        Info, Screens,
        showcase;

        public String extension() {
            return "/" + name();
        }
        public String infoExtension() { return extension() + Info.extension(); }
        public String screensExtension() { return extension() + Screens.extension(); }
    }

    private static FirebaseHelper mInstance = null;

    private FirebaseHelper() { Firebase.setAndroidContext(AmbekarApplication.getAppContext()); }

    private Firebase mRootRef;

    public static FirebaseHelper getInstance() {
        if (mInstance == null) mInstance = new FirebaseHelper();
        return mInstance;
    }

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
