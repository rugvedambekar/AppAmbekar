package ra.appambekar.helpers;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import ra.appambekar.AmbekarApplication;

/**
 * Created by rugvedambekar on 2016-04-12.
 */
public class VolleyHelper {

    private static VolleyHelper mInstance = null;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleyHelper() {
        mRequestQueue = Volley.newRequestQueue(AmbekarApplication.getAppContext());
        mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public static VolleyHelper getInstance() {
        if (mInstance == null) mInstance = new VolleyHelper();
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }

}
