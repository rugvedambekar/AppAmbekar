package ra.appambekar.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import ra.appambekar.R;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;

/**
 * Created by rugvedambekar on 2016-03-02.
 */
public class IntroductionLayout extends LinearLayout {

    private SmartTextView mTV_header, mTV_intro;

    public IntroductionLayout(Context context) {
        super(context);
        initialize(null);
    }

    public IntroductionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public IntroductionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        inflate(getContext(), R.layout.layout_introduction, this);

        mTV_header = (SmartTextView) findViewById(R.id.tv_introHeading);
        mTV_intro = (SmartTextView) findViewById(R.id.tv_introContent);

        if (attrs != null) {
            String introHeading = null, intro = null;
            TypedArray attrArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.IntroductionLayout, 0, 0);
            if (attrArray != null) {
                mTV_intro.setJustified(attrArray.getBoolean(R.styleable.IntroductionLayout_justifyIntro, true));
                introHeading = attrArray.getString(R.styleable.IntroductionLayout_introHeading);
                intro = attrArray.getString(R.styleable.IntroductionLayout_intro);
            }

            if (introHeading != null) mTV_header.setText(introHeading);
            if (intro != null) mTV_intro.setText(intro);
        }
    }
}
