package ra.appambekar.views;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import ra.appambekar.R;
import ra.smarttextview.SmartTextView;

/**
 * Created by rugvedambekar on 2016-04-20.
 */
public class VerticalViewSwitcher extends RelativeLayout {

    private static final String TAG = VerticalViewSwitcher.class.getSimpleName();

    private GestureDetectorCompat mFlingDetector;

    private LinearLayout mLL_aboveXp, mLL_belowXp;
    private ViewFlipper mVF_xpFlipper;

    private Animation mAnim_upOut, mAnim_upIn, mAnim_downOut, mAnim_downIn;

    public VerticalViewSwitcher(Context context) {
        super(context);
        initialize(null);
    }

    public VerticalViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public VerticalViewSwitcher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        inflate(getContext(), R.layout.layout_verical_view_switcher, this);

        mFlingDetector = new GestureDetectorCompat(getContext(), new VVSFlingListener());

        mLL_aboveXp = (LinearLayout) findViewById(R.id.ll_aboveXp);
        mLL_belowXp = (LinearLayout) findViewById(R.id.ll_belowXp);
        mVF_xpFlipper = (ViewFlipper) findViewById(R.id.vf_xpFlipper);

        mAnim_downIn = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down_in);
        mAnim_downOut = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down_out);
        mAnim_upIn = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_in);
        mAnim_upOut = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_out);

        mLL_aboveXp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mVF_xpFlipper.setOutAnimation(mAnim_downOut);
                mVF_xpFlipper.setInAnimation(mAnim_downIn);
                mVF_xpFlipper.showPrevious();
                updatedInternalNav();
            }
        });

        mLL_belowXp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mVF_xpFlipper.setOutAnimation(mAnim_upOut);
                mVF_xpFlipper.setInAnimation(mAnim_upIn);
                mVF_xpFlipper.showNext();
                updatedInternalNav();
            }
        });
    }

    private void updatedInternalNav() {
        View currView = mVF_xpFlipper.getCurrentView();
        int currIndex = mVF_xpFlipper.indexOfChild(currView);
        int prevIndex = currIndex -1, nextIndex = currIndex + 1;

        if (currIndex == 0) {
            ((SmartTextView) mLL_aboveXp.findViewById(R.id.tv_aboveXp)).setText("");
            mLL_aboveXp.setClickable(false);
            mLL_aboveXp.setAlpha(0.15f);

        } else if (currIndex == mVF_xpFlipper.getChildCount() - 1) {
            ((SmartTextView) mLL_belowXp.findViewById(R.id.tv_belowXp)).setText("");
            mLL_belowXp.setClickable(false);
            mLL_belowXp.setAlpha(0.15f);

        }

        if (prevIndex >= 0) {
            VVSView prevView = (VVSView) mVF_xpFlipper.getChildAt(prevIndex);
            ((SmartTextView) mLL_aboveXp.findViewById(R.id.tv_aboveXp)).setText(prevView.getHeading());
            mLL_aboveXp.setClickable(true);
            mLL_aboveXp.setAlpha(1f);
        }

        if (nextIndex < mVF_xpFlipper.getChildCount()) {
            VVSView nextView = (VVSView) mVF_xpFlipper.getChildAt(nextIndex);
            ((SmartTextView) mLL_belowXp.findViewById(R.id.tv_belowXp)).setText(nextView.getHeading());
            mLL_belowXp.setClickable(true);
            mLL_belowXp.setAlpha(1f);
        }
    }

    class VVSFlingListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(TAG, "onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            Log.d(TAG, "onFling: " + event1.toString() + event2.toString());
            Toast.makeText(getContext(), "FLUNG @ " + velocityY, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public <T extends View & VVSView> void addVVSView(T view) {
        if (mVF_xpFlipper == null || view == null) return;

        mVF_xpFlipper.addView(view);
    }

    public void doneAddingViews() {
        updatedInternalNav();
    }

    public interface VVSView {
        public CharSequence getHeading();
    }
}
