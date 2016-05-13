package ra.appambekar.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import ra.smarttextview.SmartTextView;

/**
 * Created by rugvedambekar on 2016-02-22.
 */
public class LayoutUtils {

    public static class Retrieve {

        public static int ScreenWidth(Context context) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }

    }

    public static class Convert {

        public static float PxToDp(float px, Context context) {
            if (context == null) return 0f;

            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();

            return px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }

        public static float DpToPx(float dp, Context context){
            if (context == null) return 0f;

            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();

            return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }

        public static float SpToPx(float sp, Context context) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    sp, context.getResources().getDisplayMetrics());
        }
    }

    public static class View {

        public static void SetPadding(android.view.View view, int dpPadding) {
            if (view == null) return;

            int padding = (int) Convert.DpToPx(dpPadding, view.getContext());
            view.setPadding(padding, padding, padding, padding);
        }

        public static void SetSize(android.view.View view, int dpSize) {
            if (view == null) return;

            int size = (int) Convert.DpToPx(dpSize, view.getContext());
            view.getLayoutParams().height = size;
            view.getLayoutParams().width = size;
        }

        public static int GetMeasuredHeight(android.view.View view) {
            view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            return view.getMeasuredHeight();
        }
    }

    public static class Bitmap {

        public static int CalcInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        public static android.graphics.Bitmap DecodeSampledBitmap(Resources res, int resId, int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = CalcInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            android.graphics.Bitmap bmFinal = null;

            try {
                bmFinal = BitmapFactory.decodeResource(res, resId, options);
            } catch (OutOfMemoryError ome) {
                Log.e(Bitmap.class.getSimpleName(), "OutOfMemoryError for IMG : " + reqWidth + "x" + reqHeight);
                return DecodeSampledBitmap(res, resId, (int) (reqWidth * 0.9), (int) (reqHeight * 0.9));
            }

            return bmFinal;
        }
    }
}
