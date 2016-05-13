package ra.appambekar.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ra.appambekar.R;
import ra.appambekar.models.content.Experience;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;


public class HeaderListView extends RelativeLayout implements VerticalViewSwitcher.VVSView {

    private SmartTextView mTV_header, mTV_tag;
    private LinearLayout mLL_content;

    public HeaderListView(Context context, Experience exp) {
        super(context);
        init(null, 0);

        setHeader(exp.TitleId, exp.TadId);
        setList(exp.ListId, exp.ImgListId);
    }

    public HeaderListView(Context context) {
        super(context);
        init(null, 0);
    }

    public HeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HeaderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setHeader(int headerId, int tagId) {
        mTV_header.setText(headerId);
        mTV_tag.setText(tagId);
    }
    public void setList(int listId, int imgListId) {
        mLL_content.removeAllViews();

        String[] contentList = getResources().getStringArray(listId);
        TypedArray imgList = imgListId > 0 ? getResources().obtainTypedArray(imgListId) : null;

        for (int i = 0; i < contentList.length; i++) {

            IconListItem itemView = new IconListItem(getContext());
            int imgResId = imgList == null ? -1 : imgList.getResourceId(i, -1);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, (int) LayoutUtils.Convert.DpToPx(5, getContext()));

            itemView.setLayoutParams(layoutParams);
            itemView.setType(IconListItem.Type.Light);
            itemView.setContent(contentList[i]);
            itemView.setIcon(imgResId);

            mLL_content.addView(itemView);
        }
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.layout_header_list_view, this);

        mTV_header = (SmartTextView) findViewById(R.id.tv_hlbHeader);
        mTV_tag = (SmartTextView) findViewById(R.id.tv_hlbTag);

        mLL_content = (LinearLayout) findViewById(R.id.ll_hlbContent);
    }

    @Override
    public CharSequence getHeading() {
        return mTV_header.getText();
    }

}
