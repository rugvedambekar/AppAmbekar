package ra.appambekar.utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

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

        public static final int ANIM_DUR = 150;

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

        public static void AdjustTextSizeForWidth(TextView textView, int desiredWidth) {
            Paint paint = new Paint();
            Rect bounds = new Rect();

            paint.setTypeface(textView.getTypeface());
            float textSize = textView.getTextSize();
            paint.setTextSize(textSize);
            String text = textView.getText().toString();
            paint.getTextBounds(text, 0, text.length(), bounds);

            if (desiredWidth <= 0) {
                desiredWidth = textView.getMeasuredWidth() - textView.getPaddingLeft() - textView.getPaddingRight();
                if (desiredWidth <= 0) return;
            }

            while (bounds.width() > desiredWidth) {
                textSize--;
                paint.setTextSize(textSize);
                paint.getTextBounds(text, 0, text.length(), bounds);
            }

            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        public static void Show(final android.view.View view, boolean show) {
            view.setAlpha(show ? 0 : 1);
            view.setVisibility(android.view.View.VISIBLE);
            view.animate().alpha(show ? 1 : 0).setDuration(ANIM_DUR).setStartDelay(show ? ANIM_DUR : 0).withEndAction(new Runnable() {
                @Override public void run() {
                    if (view.getAlpha() == 0) view.setVisibility(android.view.View.GONE);
                }
            }).start();
        }

        public static void SetAsButton(android.view.View view, final Runnable runOnClick) {
            view.setOnClickListener(new android.view.View.OnClickListener() {
                @Override public void onClick(final android.view.View v) {
                    v.animate().alpha(0.25f).setDuration(ANIM_DUR)
                            .withStartAction(runOnClick)
                            .withEndAction(new Runnable() { @Override public void run() {
                                v.animate().alpha(1).setDuration(ANIM_DUR).start();
                            } }).start();
                }
            });
        }

        public static AppCompatActivity getActivity(android.view.View view) {
            Context context = view.getContext();
            while (context instanceof ContextWrapper) {
                if (context instanceof AppCompatActivity) return (AppCompatActivity) context;
                context = ((ContextWrapper) context).getBaseContext();
            }
            return null;
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
