package ra.appambekar.models.scrolling;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by rugvedambekar on 2016-02-24.
 */
public class SlideViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = SlideViewHolder.class.getSimpleName();
    private static final int ANIM_DUR = 250;

    private Rect mReuseRect = new Rect();
    private boolean mEntering = false, mExiting = false;

    public SlideViewHolder(View v) {
        super(v);
    }

    public void getHitRect(Rect hitRect) {
        itemView.getHitRect(hitRect);
    }

    public View getView() {
        return itemView;
    }

    public void onBind() {
//        itemView.setAlpha(0);
        itemView.animate().cancel();
        mEntering = mExiting = false;
    }

    public void presentNow() {
        itemView.getLocalVisibleRect(mReuseRect);

        if (mReuseRect.height() == itemView.getHeight()) {
            itemView.animate().alpha(1).setDuration(ANIM_DUR / 2).setStartDelay(getAdapterPosition() * 50).start();
        }

        Log.d(TAG, String.format("Rect Print :: %d (%dx%d)", getAdapterPosition(), itemView.getWidth(), itemView.getHeight()));
        Log.d(TAG, String.format("VisibilityRect : b=%d t=%d : %dx%d", mReuseRect.bottom, mReuseRect.top, mReuseRect.width(), mReuseRect.height()));
    }

    public void ensureEntered(boolean fromTop) {
        if (!mEntering && itemView.getAlpha() == 0) {
            mEntering = true;

            itemView.setTranslationY(fromTop ? -itemView.getHeight() : itemView.getHeight());
            itemView.animate().alpha(1).translationY(0).setDuration(ANIM_DUR)
                    .withEndAction(new Runnable() { @Override public void run() { mEntering = false; } })
                    .start();
        }
    }

    public void ensureExited(boolean fromTop) {
        if (!mExiting && itemView.getAlpha() == 1) {
            mExiting = true;

            itemView.setTranslationY(0);
            itemView.animate().alpha(0).translationY(fromTop ? -itemView.getHeight() : itemView.getHeight()).setDuration(ANIM_DUR)
                    .withEndAction(new Runnable() { @Override public void run() { mExiting = false; } })
                    .start();
        }
    }

}