package ra.appambekar.models.scrolling;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import ra.appambekar.adapters.HolderTrackingAdapter;

/**
 * Created by rugvedambekar on 2016-02-24.
 */
public class SlideScrollListener extends RecyclerView.OnScrollListener {

    private Rect mReuseRect = new Rect();
    private LinearLayoutManager mLayoutManager;
    private HolderTrackingAdapter<SlideViewHolder> mAdapter;

    public SlideScrollListener(LinearLayoutManager recyclerLLM, HolderTrackingAdapter<SlideViewHolder> adapter) {
        mLayoutManager = recyclerLLM;
        mAdapter = adapter;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy == 0) { // no scroll, initial load...
            for (int i = 0; i < mAdapter.attachedViewCount(); i++) mAdapter.getViewHolder(i).presentNow();
            return;
        }

        int bottom = recyclerView.getHeight() - recyclerView.getPaddingBottom() - recyclerView.getPaddingTop();
        int firstPos = mLayoutManager.findFirstVisibleItemPosition();
        int lastPos = mLayoutManager.findLastVisibleItemPosition();

        boolean scrollingUp = dy > 0;

        SlideViewHolder firstHolder = mAdapter.getViewHolder(firstPos);
        SlideViewHolder lastHolder = mAdapter.getViewHolder(lastPos);

        if (firstHolder != null) {
            firstHolder.getHitRect(mReuseRect);
            if (mReuseRect.top < 0) firstHolder.ensureExited(true);
            else firstHolder.ensureEntered(true);
        }

        if (lastHolder != null) {
            lastHolder.getHitRect(mReuseRect);

            Log.d("SCROLL", String.format("LastHolder %d: b=%d (bottom=%d)", lastHolder.getAdapterPosition(), mReuseRect.bottom, bottom));

            if (mReuseRect.bottom > bottom) lastHolder.ensureExited(false);
            else lastHolder.ensureEntered(false);
        }

        for (int i = firstPos + 1; i < lastPos; i++) {
            mAdapter.getViewHolder(i).ensureEntered(!scrollingUp);
        }

    }
}