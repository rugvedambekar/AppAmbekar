package ra.appambekar.helpers;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by rugvedambekar on 2016-04-12.
 */
public class CloudinaryHelper {

    private static final String TAG = CloudinaryHelper.class.getSimpleName();

    private static CloudinaryHelper mInstance = null;

    private String mBaseImagesURL;

    private CloudinaryHelper() {
        FirebaseHelper.getInstance().getChildREF(FirebaseHelper.FireChild.BaseImagesURL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBaseImagesURL = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.toString());
                mBaseImagesURL = "";
            }
        });
    }

    public static CloudinaryHelper getInstance() {
        if (mInstance == null) mInstance = new CloudinaryHelper();
        return mInstance;
    }

    public String getBaseImagesURL() { return mBaseImagesURL; }
    public String getHeightTransformURL(int height) { return mBaseImagesURL + "/h_" + height; }
}
