package ra.appambekar.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import ra.appambekar.R;
import ra.smarttextview.SmartTextView;

/**
 * Created by rugvedambekar on 2016-02-29.
 */
public class WorkEthicItem extends LinearLayout implements View.OnClickListener, ValueAnimator.AnimatorUpdateListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = WorkEthicItem.class.getSimpleName();

    private SmartTextView mTV_preTitle, mTV_title;
    private SmartTextView mTV_content1, mTV_content2;

    private View mContentContainer;
    private int mContentHeight;

    private WEIObserver mObserver;

    public WorkEthicItem(Context context) {
        super(context);
        initialize();
    }
    public WorkEthicItem(Context context, WEIObserver observer) {
        super(context);

        mObserver = observer;
        initialize();
    }

    private void initialize() {
        inflate(getContext(), R.layout.item_work_ethic, this);

        mTV_preTitle = (SmartTextView) findViewById(R.id.tv_preTitle);
        mTV_title = (SmartTextView) findViewById(R.id.tv_weTitle);

        mTV_content1 = (SmartTextView) findViewById(R.id.tv_content1);
        mTV_content2 = (SmartTextView) findViewById(R.id.tv_content2);

        mContentContainer = findViewById(R.id.ll_weContent);

        setOnClickListener(this);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public WorkEthicItem withTitle(int preTitleId, int titleId) {
        mTV_preTitle.setText(preTitleId);
        mTV_title.setText(titleId);
        return this;
    }

    public WorkEthicItem withContent(int con1Id, int con2Id) {
        mTV_content1.setText(con1Id);
        mTV_content2.setText(con2Id);
        return this;
    }

    public WorkEthicItem withObserver(WEIObserver observer) {
        mObserver = observer;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (mContentContainer.getVisibility() == GONE) expand();
        else collapse();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mContentContainer.getLayoutParams().height = (Integer) animation.getAnimatedValue();
        mContentContainer.requestLayout();
    }

    @Override
    public void onGlobalLayout() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        mContentHeight = mContentContainer.getMeasuredHeight();
        mContentContainer.setVisibility(GONE);
    }

    public void expand() {
        Log.d(TAG, "Expanding Item : " + mTV_title.getText());

        ValueAnimator animator = ValueAnimator.ofInt(0, mContentHeight).setDuration(250);
        animator.addUpdateListener(this);

        mContentContainer.getLayoutParams().height = 0;
        mContentContainer.setVisibility(VISIBLE);
        animator.start();

        if (mObserver != null) mObserver.expanded(this);
    }

    public void collapse() {
        Log.d(TAG, "Collapsing Item : " + mTV_title.getText());

        final int initialHeight = mContentContainer.getMeasuredHeight();
        ValueAnimator animator = ValueAnimator.ofInt(initialHeight, 0).setDuration(250);
        animator.addUpdateListener(this);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mContentContainer.setVisibility(GONE);
            }
        });

        animator.start();
    }

    public interface WEIObserver {
        void expanded(WorkEthicItem item);
    }
}
