package ra.appambekar.fragments.android;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ra.appambekar.R;
import ra.appambekar.fragments.BaseAsyncFragment;
import ra.appambekar.models.content.Development;
import ra.appambekar.utilities.LayoutUtils;
import ra.appambekar.views.IconListItem;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class DevelopmentFragment extends BaseAsyncFragment {

    private LinearLayout mContentContainer;
    private ArrayList<IconListItem> mDevelopmentViews;

    public DevelopmentFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_scroller, container, false);

        mContentContainer = (LinearLayout) rootView.findViewById(R.id.ll_contentContainer);

        return rootView;
    }

    @Override
    protected void doInBackground() {
        mDevelopmentViews = new ArrayList<>();

        for (Development devContent : Development.values()) {
            IconListItem iconItem = new IconListItem(getActivity(), null, R.style.AndroidListItem);
            iconItem.setType(IconListItem.Type.Heavy);
            iconItem.setContent(devContent.ContentId);
            iconItem.setIcon(devContent.IconId);

            int pad = (int) LayoutUtils.Convert.DpToPx(5, getActivity());
            iconItem.setPadding(pad, pad, pad, pad);

            mDevelopmentViews.add(iconItem);
        }
    }

    @Override
    protected void onPostExecute() {
        for (IconListItem devView : mDevelopmentViews) mContentContainer.addView(devView);
    }
}
