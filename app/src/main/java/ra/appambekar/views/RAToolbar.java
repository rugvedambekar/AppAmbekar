package ra.appambekar.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import ra.appambekar.R;
import ra.smarttextview.SmartTextView;

/**
 * Created by rugvedambekar on 2016-02-23.
 */
public class RAToolbar extends Toolbar {

    private SmartTextView mTitleTextView;

    public RAToolbar(Context context) {
        super(context);
    }

    public RAToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RAToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SmartTextView getHeaderView() { return mTitleTextView; }

    @Override
    public void setTitle(CharSequence title) {
        if (mTitleTextView == null) mTitleTextView = (SmartTextView) findViewById(R.id.toolbar_header);

        if (mTitleTextView != null) mTitleTextView.setText(title);
        else super.setTitle(title);
    }
}
