package ra.appambekar.views;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

import ra.appambekar.R;
import ra.appambekar.utilities.LayoutUtils;

/**
 * Created by rugvedambekar on 2016-04-15.
 */
public class ParallaxSkyLayout extends RelativeLayout {

    private static final String TAG = ParallaxSkyLayout.class.getSimpleName();

    private static final int[] Clouds = { R.drawable.cloud1, R.drawable.cloud2, R.drawable.cloud3 };

    private View mSunView = null;
    private ImageView mMoonView = null;
    private ArrayList<ParallaxImageView> mCloudViews = new ArrayList<>();

    private ScrollView mScrollView = null;
    private OnScrollChangedListener mScrollListener = null;

    private float mMaxScroll = 0;
    private int mCol_daySky = 0, mCol_nightSky = 0;

    private ArgbEvaluator mColEvaluator = new ArgbEvaluator();
    private ObjectAnimator mScrollOscillator;

    public ParallaxSkyLayout(Context context) {
        super(context);
    }
    public ParallaxSkyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ParallaxSkyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void buildSky(BuildObserver observer) {
        mCol_daySky = getResources().getColor(R.color.sky_day);
        mCol_nightSky = getResources().getColor(R.color.sky_night);

        setBackgroundColor(mCol_daySky);

        new BuildParallaxTask().execute(observer);
    }
    public void destroySky() {
        // Deallocate all memory used by Clouds
        for (ParallaxImageView cloudView : mCloudViews) cloudView.destroy();
    }

    public void destroyView() {
        if (mScrollView != null && mScrollListener != null) {
            mScrollView.getViewTreeObserver().removeOnScrollChangedListener(mScrollListener);
            mScrollListener = null;
            mScrollView = null;
        }

        mSunView = null;
        mMoonView = null;
        mCloudViews.clear();

        removeAllViews();
    }
    public void registerScrollView(ScrollView scrollView) {
        mScrollView = scrollView;
        mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                mScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mMaxScroll = mScrollView.getChildAt(0).getHeight() - mScrollView.getHeight();

                Log.d(TAG, "GlobalLayoutListener :: MaxScroll=" + mMaxScroll);
            }
        });
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(mScrollListener = new OnScrollChangedListener() {

            float lastScrollY = -1;

            @Override public void onScrollChanged() {
                if (lastScrollY == mScrollView.getScrollY()) return;
                lastScrollY = mScrollView.getScrollY();

                float percentScrolledY = Math.min(lastScrollY / mMaxScroll, 1);

//                Log.d(TAG, String.format("onScrolled:: scrolledY=%f maxScrollY=%f", lastScrollY, mMaxScroll));

                reactToScroll(percentScrolledY);
            }
        });
    }

    public void startOscillation() {
        if (mScrollOscillator != null) return;

        Log.d(TAG, "Oscillating Scroll : 0 to " + mMaxScroll);

        mScrollOscillator = ObjectAnimator.ofInt(mScrollView, "scrollY", (int) mMaxScroll);
        mScrollOscillator.setRepeatMode(ValueAnimator.REVERSE);
        mScrollOscillator.setRepeatCount(ValueAnimator.INFINITE);
        mScrollOscillator.setDuration(40000);
        mScrollOscillator.start();
    }
    public void endOscillation() {
        if (mScrollOscillator == null) return;

        mScrollOscillator.cancel();
        mScrollOscillator = null;

        mScrollView.smoothScrollTo(0, 0);
    }

    private void showMoon(float percentScrolled) {
        if (mMoonView == null) return;

        if (percentScrolled < 0.5) mMoonView.setImageAlpha(0);
        else {
            double alphaPercent = (percentScrolled - 0.5) * 2;
            mMoonView.setImageAlpha((int) (100 * alphaPercent));
        }
    }

    private void reactToScroll(float percentScrolledY) {
        showMoon(percentScrolledY);
        setBackgroundColor((Integer) mColEvaluator.evaluate(percentScrolledY, mCol_daySky, mCol_nightSky));

        if (mSunView != null) mSunView.setAlpha(Math.max(0, 1 - (percentScrolledY * 2)));

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof ParallaxImageView)
                ((ParallaxImageView) child).setVerticalPercent(percentScrolledY);
        }
    }

    private RecyclerView.OnScrollListener mRecyclerScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int scrollOffsetY = recyclerView.computeVerticalScrollOffset();
            int maxScrollY = recyclerView.computeVerticalScrollRange() - recyclerView.getHeight();

            float percentScrolledY = Math.min(((float)scrollOffsetY) / maxScrollY, 1);

            reactToScroll(percentScrolledY);

//            Log.d(TAG, String.format("onScrolled:: scrolledY=%d maxScrollY=%d", scrollOffsetY, maxScrollY));
        }
    };

    private class BuildParallaxTask extends AsyncTask<BuildObserver, Void, Void> {

        private BuildObserver mObserver;

        @Override
        protected Void doInBackground(BuildObserver... observers) {
            if (mSunView == null) {
                RelativeLayout.LayoutParams sunParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                sunParams.addRule(ALIGN_PARENT_TOP);

                mSunView = new View(getContext());
                mSunView.setBackgroundResource(R.drawable.solar_gradient);
                mSunView.setLayoutParams(sunParams);
            }

            if (mMoonView == null) {
                int moonDIM = LayoutUtils.Retrieve.ScreenWidth(getContext()) / 2;

                RelativeLayout.LayoutParams moonParams = new LayoutParams(moonDIM, moonDIM);
                moonParams.addRule(ALIGN_PARENT_BOTTOM);
                moonParams.addRule(ALIGN_PARENT_RIGHT);

                mMoonView = new ImageView(getContext());
                mMoonView.setImageResource(R.drawable.moon);
                mMoonView.setLayoutParams(moonParams);
            }

            if (mCloudViews.isEmpty()) {
                for (int imgId : Clouds) {
                    ParallaxImageView piv = new ParallaxImageView(getContext());
                    piv.setImage(imgId);
                    mCloudViews.add(piv);
                }

            } else for (ParallaxImageView cloudView : mCloudViews) cloudView.build();

            if (observers != null) mObserver = observers[0];

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            removeAllViews();

            addView(mSunView);
            addView(mMoonView);
            for (ParallaxImageView cloudView : mCloudViews) {
                cloudView.setAlpha(0);
                addView(cloudView);

                cloudView.animate().alpha(1).setDuration(1000).start();
            }

            if (mObserver != null) mObserver.onComplete();
        }
    };

    public interface BuildObserver {
        void onComplete();
    }

}
