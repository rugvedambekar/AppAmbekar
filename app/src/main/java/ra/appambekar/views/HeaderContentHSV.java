package ra.appambekar.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ra.appambekar.R;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;

/**
 * Created by rugvedambekar on 2016-03-01.
 */
public class HeaderContentHSV extends LinearLayout implements View.OnClickListener {

    private static final String TAG = HeaderContentHSV.class.getName();

    private HorizontalScrollView mHSV_content;
    private RelativeLayout mRL_headers;
    private LinearLayout mLL_content;
    private View mLastSelected;

    private ArrayList<View> mFirstViews;

    private Drawable mContentBack_on, mContentBack_off;
    private int mContentSpacing, mContentPadding;
    private int mTextCol_on, mTextCol_off;

    private Rect scrollBounds = new Rect();

    private ContentObserver mObserver;

    public HeaderContentHSV(Context context) {
        super(context);
        initialize();
    }

    public HeaderContentHSV(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public HeaderContentHSV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        inflate(getContext(), R.layout.layout_header_content_hsv, this);

        mFirstViews = new ArrayList<>();

        mHSV_content = (HorizontalScrollView) findViewById(R.id.hsv_content);
        mRL_headers = (RelativeLayout) findViewById(R.id.ll_headers);
        mLL_content = (LinearLayout) findViewById(R.id.ll_content);

        mContentSpacing = (int) LayoutUtils.Convert.DpToPx(10, getContext());
        mContentPadding = (int) LayoutUtils.Convert.DpToPx(5, getContext());

        mLL_content.setPadding(0, mContentPadding, 0, mContentPadding);

        Resources res = getResources();

        mContentBack_on = res.getDrawable(R.drawable.rounded_solid);
        mContentBack_off = res.getDrawable(R.drawable.rounded_border);

        mTextCol_off = res.getColor(R.color.toolbar_light);
        mTextCol_on = res.getColor(R.color.toolbar_main);

        mHSV_content.getViewTreeObserver().addOnScrollChangedListener(mContentScrollListener);
    }

    public void initializeOnVisible() {
        mHSV_content.getHitRect(scrollBounds);
        if (mLastSelected == null && switchOn(mFirstViews.get(0)) && mObserver != null) {
            mObserver.onClick((SmartTextView) mFirstViews.get(0));
        }

        Log.d(TAG, String.format("ScrollBounds=(l=%d,r=%d), ScrollView=(l=%d,r=%d)", scrollBounds.left, scrollBounds.right, mHSV_content.getLeft(), mHSV_content.getRight()));
        cleanHeaderViews();
    }

    public void setContentObserver(ContentObserver observer) { mObserver = observer; }
    public void addHeaderAndContent(int headerId, int contentArrayId) {
        int contentWidth = 0;

        String header = getResources().getString(headerId);
        String[] content = getResources().getStringArray(contentArrayId);

        for (int i = 0; i < content.length; i++) {
            final SmartTextView contentView = getView(false, content[i]);
            contentView.setOnClickListener(this);
            contentView.setId(contentArrayId);
            contentView.setIndex(i);

            contentView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            contentWidth += contentView.getMeasuredWidth() + mContentSpacing;

            if (i == 0) mFirstViews.add(contentView);

            mLL_content.addView(contentView);
        }

        SmartTextView headerView = getView(true, header);
        headerView.setLayoutParams(getLayoutParams(-1, 0, 0));
        headerView.setId(contentWidth);
        headerView.setAlpha(0);

        mRL_headers.addView(headerView);

        mFirstViews.get(mFirstViews.size() - 1).setTag(headerView);
    }

    public void scrollToSelected() {
        if (mLastSelected == null) return;

        int duration = Math.abs(mLastSelected.getLeft() - mHSV_content.getScrollX()) / 2;
        ObjectAnimator.ofInt(mHSV_content, "scrollX", mLastSelected.getLeft()).setDuration(duration).start();
    }

    private void cleanHeaderViews() {
        for (View view : mFirstViews) {
            View headerView = (View) view.getTag();

            if (view.getX() >= mHSV_content.getLeft() && view.getX() < mHSV_content.getRight()) {
                headerView.setX(view.getX());
                headerView.setTag(true);
                headerView.setAlpha(1);

                Log.d(TAG, "Adding Header: " + ((TextView) headerView).getText() + " @ " + view.getX());

            } else {
                headerView.setTag(false);
                headerView.setAlpha(0);
            }
        }
    }

    private SmartTextView getView(boolean header, String conStr) {
        SmartTextView contentView = new SmartTextView(getContext());
        contentView.setFontType(header ? SmartTextView.FontType.Thick : SmartTextView.FontType.Reg);
        contentView.setTextColor(header ? mTextCol_on : mTextCol_off);
        contentView.setTextSize(header ? 12 : 20);
        contentView.setText(conStr);

        if (!header) {
            contentView.setBackground(mContentBack_off);
            contentView.setLayoutParams(getLayoutParams(-1, 0, mContentSpacing));
            contentView.setPadding(mContentPadding, mContentPadding, mContentPadding, mContentPadding);
        }

        return contentView;
    }

    private LinearLayout.LayoutParams getLayoutParams(int width, int mLeft, int mRight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (width <= 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : width), ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(mLeft, 0, mRight, 0);

        return params;
    }

    @Override
    public void onClick(View v) {
        if (switchOn(v) && mObserver != null) mObserver.onClick((SmartTextView) v);
    }

    private boolean switchOn(View v) {
        if (mLastSelected == v) return false;

        if (mLastSelected != null) turnOff();
        mLastSelected = v;

        mLastSelected.setBackground(mContentBack_on);
        ((SmartTextView) mLastSelected).setTextColor(mTextCol_on);
        return true;
    }
    private void turnOff() {
        if (mLastSelected == null) return;

        mLastSelected.setBackground(mContentBack_off);
        ((SmartTextView) mLastSelected).setTextColor(mTextCol_off);
        mLastSelected = null;
    }

    private ViewTreeObserver.OnScrollChangedListener mContentScrollListener = new ViewTreeObserver.OnScrollChangedListener() {

        private int lastScrollPos = 0;
        private Rect viewVisRect = new Rect();

        @Override
        public void onScrollChanged() {
            if (lastScrollPos == mHSV_content.getScrollX()) return;
            lastScrollPos = mHSV_content.getScrollX();

            int visibleViewIndex = -1;
            while (++visibleViewIndex < mFirstViews.size()) {
                View contentView = mFirstViews.get(visibleViewIndex);
                if (contentView.getLocalVisibleRect(scrollBounds)) break;
            }

            if (visibleViewIndex >= mFirstViews.size()) return;

            View firstHeaderView = visibleViewIndex > 0 ? (View) mFirstViews.get(visibleViewIndex - 1).getTag() : null;
            if (firstHeaderView != null && !(boolean) firstHeaderView.getTag()) firstHeaderView = null;

            for (int i = visibleViewIndex; i < mFirstViews.size(); i++) {
                View contentView = mFirstViews.get(i);
                View headerView = (View) contentView.getTag();

                // If first ContentView of the group is showing on screen
                if (contentView.getLocalVisibleRect(scrollBounds)) {
                    contentView.getLocalVisibleRect(viewVisRect);

//                    Log.d(TAG, String.format("%s :: ViewVisRect=(l=%d,r=%d) HeaderX=%d", ((TextView) contentView).getText(), viewVisRect.left, viewVisRect.right, (int)contentView.getX() - lastScrollPos));

                    // Align HeaderView with ContentView
                    if (viewVisRect.left > 0) headerView.setX(0);
                    else headerView.setX((int)contentView.getX() - lastScrollPos);

                    headerView.setTag(true);
                    headerView.setAlpha(1);

                    if (firstHeaderView != null && firstHeaderView != headerView) {

                        // Check for collision with FirstHeaderView; hide if colliding
                        if (headerView.getX() < firstHeaderView.getWidth()) {
                            firstHeaderView.animate().alpha(0).setDuration(250).start();
                            firstHeaderView.setTag(false);
                        }

                    } else if (firstHeaderView == null && i > 0) {

                        View prevHeaderView = (View) mFirstViews.get(i - 1).getTag();

                        // Check if we can reshow PreviousHeaderView; show if there is enough space
                        if (headerView.getX() > prevHeaderView.getWidth()) {
                            prevHeaderView.animate().alpha(1).setDuration(250).start();
                            prevHeaderView.setTag(true);
                        }
                    }

                // if first ContentView of the group is off screen (always off to the right)
                } else {
                    headerView.setAlpha(0);
                    headerView.setTag(false);
                }
            }
        }
    };

    public interface ContentObserver {
        public void onClick(SmartTextView contentView);
    }

}
