package ra.appambekar.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ra.appambekar.R;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;

import static ra.smarttextview.SmartTextView.FontType;

/**
 * Created by rugvedambekar on 2016-03-01.
 */
public class IconListItem extends LinearLayout {

    private ImageView mIV_icon;
    private SmartTextView mTV_content;

    public IconListItem(Context context) {
        super(context);
        initialize(null);
    }

    public IconListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public IconListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        inflate(getContext(), R.layout.item_icon_list, this);

        mIV_icon = (ImageView) findViewById(R.id.iv_icon);
        mTV_content = (SmartTextView) findViewById(R.id.tv_weTitle);

        if (attrs != null) {
            int iconId = -1, contentId = -1, typeIndex = 0;
            TypedArray attrArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.IconListItem, 0, 0);
            if (attrArray != null) {
                typeIndex = attrArray.getInt(R.styleable.IconListItem_itemType, 0);
                iconId = attrArray.getResourceId(R.styleable.IconListItem_iconSrc, -1);
                contentId = attrArray.getResourceId(R.styleable.IconListItem_contentSrc, -1);
            }

            if (iconId != -1) mIV_icon.setImageResource(iconId);
            if (contentId != -1) mTV_content.setText(contentId);
            setType(Type.values()[typeIndex]);
        }
    }

    public void setType(Type type) {
        LayoutUtils.View.SetSize(mIV_icon, type.IconSz);
        ((LayoutParams)mIV_icon.getLayoutParams()).setMargins(0, 0, (int) LayoutUtils.Convert.DpToPx(type.Spacing, getContext()), 0);

        type.setTextView(mTV_content);
    }
    public void setIcon(int iconId) {
        if (iconId == -1) mIV_icon.setVisibility(GONE);
        else mIV_icon.setImageResource(iconId);
    }

    public void setContent(int contentId) {
        mTV_content.setText(contentId);
    }
    public void setContent(String content) {
        mTV_content.setText(content);
    }

    public enum Type {
        Light(32, 10, 18),
        Heavy(36, 15, 15);

        int IconSz, Spacing, TextSz;
        FontType fType;

        private Type(int ic, int sp, int txt) {
            IconSz = ic;
            Spacing = sp;
            TextSz = txt;
        }

        private void setTextView(SmartTextView stv) {
            stv.setFontType(this.fType);
            stv.setTextSize(this.TextSz);
            if (this == Heavy) {
                stv.setJustified(true);
            }
        }
    }
}
