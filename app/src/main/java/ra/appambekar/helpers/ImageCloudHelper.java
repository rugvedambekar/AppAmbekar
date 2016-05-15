package ra.appambekar.helpers;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by rugvedambekar on 2016-04-12.
 */
public class ImageCloudHelper {

    private static final String TAG = ImageCloudHelper.class.getSimpleName();

    private static final String DefaultCloudURL = "http://res.cloudinary.com/dpea6h1qz/image/upload";

    private static ImageCloudHelper mInstance = null;

    public static ImageCloudHelper getInstance() {
        if (mInstance == null) mInstance = new ImageCloudHelper();
        return mInstance;
    }

    private String mBaseImagesURL;

    private ImageCloudHelper() {
        mBaseImagesURL = DefaultCloudURL;
        FirebaseHelper.getInstance().getChildREF(FirebaseHelper.FireChild.BaseImagesURL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mBaseImagesURL = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, firebaseError.toString());
            }
        });
    }

    public String getBaseImagesURL() { return mBaseImagesURL; }
    public String getHeightTransformURL(int height) { return mBaseImagesURL + "/h_" + height; }


}
