package ra.appambekar.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import ra.appambekar.R;
import ra.appambekar.models.content.Experience;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;


public class HeaderListView extends RelativeLayout implements VerticalViewSwitcher.VVSView {

    private SmartTextView mTV_header, mTV_tag;
    private ScrollView mScrollContainer;

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
        mScrollContainer.removeAllViews();

        LinearLayout innerLayout = new LinearLayout(getContext());
        innerLayout.setOrientation(LinearLayout.VERTICAL);

        int padding = (int) LayoutUtils.Convert.DpToPx(5, getContext());
        String[] contentList = getResources().getStringArray(listId);
        TypedArray imgList = imgListId > 0 ? getResources().obtainTypedArray(imgListId) : null;

        for (int i = 0; i < contentList.length; i++) {

            IconListItem itemView = new IconListItem(getContext());
            int imgResId = imgList == null ? -1 : imgList.getResourceId(i, -1);

            itemView.setPadding(0, i == 0 ? padding : 0, 0, padding);
            itemView.setType(IconListItem.Type.Light);
            itemView.setContent(contentList[i]);
            itemView.setIcon(imgResId);

            innerLayout.addView(itemView);
        }

        mScrollContainer.addView(innerLayout);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.layout_header_list_view, this);

        mTV_header = (SmartTextView) findViewById(R.id.tv_hlbHeader);
        mTV_tag = (SmartTextView) findViewById(R.id.tv_hlbTag);

        mScrollContainer = (ScrollView) findViewById(R.id.scrollContainer_list);
    }

    @Override
    public CharSequence getHeading() {
        return mTV_header.getText();
    }

}
