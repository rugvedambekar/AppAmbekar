package ra.appambekar.adapters;

import java.util.HashMap;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by rugvedambekar on 2016-02-24.
 */
public abstract class HolderTrackingAdapter<T extends ViewHolder> extends Adapter<T> {

    private HashMap<Integer, T> mAttachedHolders = new HashMap<>();

    @Override
    public void onViewAttachedToWindow(T holder) {
        super.onViewAttachedToWindow(holder);
        mAttachedHolders.put(holder.getAdapterPosition(), holder);
    }

    @Override
    public void onViewDetachedFromWindow(T holder) {
        super.onViewDetachedFromWindow(holder);
        mAttachedHolders.remove(holder.getAdapterPosition());
    }

    public T getViewHolder(int pos) { return mAttachedHolders.get(pos); }
    public int attachedViewCount() { return mAttachedHolders.size(); }

}