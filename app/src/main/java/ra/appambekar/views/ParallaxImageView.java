package ra.appambekar.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import ra.appambekar.R;
import ra.appambekar.utilities.LayoutUtils;

/**
 * Created by rugvedambekar on 2016-04-15.
 */
public class ParallaxImageView extends View {

    private static final String TAG = ParallaxImageView.class.getSimpleName();

    private static final double DEFAULT_SLIDE_RATE = 0.05;

    private boolean mTrack = false;

    private Bitmap mImageBitmap;
    private Rect mSrcRect = new Rect(), mDstRect = new Rect();

    private int mImgId = -1;
    private int mMaxOffsetY = 0;
    private int mCurrOffsetY = 0, mTargetOffsetY = 0;

    private boolean mSliding = false;
    private Handler mSlideHandler = new Handler(Looper.getMainLooper());

    public ParallaxImageView(Context context) {
        super(context);
    }

    public ParallaxImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImage(int imgId) {
        mImgId = imgId;
        build();
    }
    public void build() {
        if (mImageBitmap != null || mImgId == -1) return;
        mImageBitmap = LayoutUtils.Bitmap.DecodeSampledBitmap(getContext().getResources(), mImgId, LayoutUtils.Retrieve.ScreenWidth(getContext()), 0);
    }
    public void destroy() {
        if (mImageBitmap == null) return;

        mImageBitmap.recycle();
        mImageBitmap = null;
    }

    public void logTack() {
        mTrack = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mImageBitmap != null) {

            mMaxOffsetY = mImageBitmap.getHeight() - getMeasuredHeight();

            if (mTrack) Log.d(TAG, String.format("onMeasure:: View=%dx%d Bitmap=%dx%d MaxOffsetY=%d",
                    getMeasuredWidth(), getMeasuredHeight(), mImageBitmap.getWidth(), mImageBitmap.getHeight(), mMaxOffsetY));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mImageBitmap == null) return;

        super.onDraw(canvas);

        canvas.getClipBounds(mDstRect);

        mSrcRect.left = mDstRect.left;
        mSrcRect.right = Math.min(mDstRect.right, mImageBitmap.getWidth());
        mSrcRect.top = mDstRect.top + mCurrOffsetY;
        mSrcRect.bottom = mDstRect.bottom + mCurrOffsetY;

        canvas.drawBitmap(mImageBitmap, mSrcRect, mDstRect, null);

        if (mTrack)  Log.d(TAG, "onDraw:: DST=" + mDstRect.toString() + " SRC=" + mSrcRect.toString());

        if (mCurrOffsetY != mTargetOffsetY) {
            int unitsToSlide = (int) Math.max(Math.abs(mTargetOffsetY - mCurrOffsetY) * DEFAULT_SLIDE_RATE, 1);

            if (mCurrOffsetY < mTargetOffsetY) mCurrOffsetY = Math.min(mTargetOffsetY, mCurrOffsetY + unitsToSlide);
            else if (mCurrOffsetY > mTargetOffsetY) mCurrOffsetY = Math.max(mTargetOffsetY, mCurrOffsetY - unitsToSlide);
            invalidate();

        }
    }

    public void setVerticalPercent(float vPercent) {
        if (vPercent < 0 || vPercent > 1) return;

        mTargetOffsetY = (int) (vPercent * mMaxOffsetY);

        if (!mSliding) mSlideHandler.post(new Runnable() {
                @Override public void run() {
                    mSliding = true;
                    invalidate();
                    mSliding = false;
                }
            });
    }
}
