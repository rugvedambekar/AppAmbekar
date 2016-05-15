package ra.appambekar.fragments.qualifications;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ra.appambekar.R;
import ra.appambekar.fragments.BaseAsyncFragment;
import ra.appambekar.views.WorkEthicItem;


public class WorkEthicFragment extends BaseAsyncFragment implements WorkEthicItem.WEIObserver {

    private static final String TAG = WorkEthicFragment.class.getSimpleName();

    private LinearLayout mLL_content;
    private WorkEthicItem mExpandedItem;

    private ArrayList<View> mWorkEthicViews;

    public WorkEthicFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_work_ethic, container, false);

        mLL_content = (LinearLayout) rootView.findViewById(R.id.ll_contentContainer);

        return rootView;
    }

    @Override
    protected void doInBackground() {
        mWorkEthicViews = new ArrayList<>();

        mWorkEthicViews.add(new WorkEthicItem(getActivity(), this)
                .withTitle(R.string.we_conditions_pre, R.string.we_conditions)
                .withContent(R.string.we_conditions_content1, R.string.we_conditions_content2));

        mWorkEthicViews.add(new WorkEthicItem(getActivity(), this)
                .withTitle(R.string.we_environment_pre, R.string.we_environment)
                .withContent(R.string.we_environment_content1, R.string.we_environment_content2));

        mWorkEthicViews.add(new WorkEthicItem(getActivity(), this)
                .withTitle(R.string.we_requirements_pre, R.string.we_requirements)
                .withContent(R.string.we_requirements_content1, R.string.we_requirements_content2));

        mWorkEthicViews.add(new WorkEthicItem(getActivity(), this)
                .withTitle(R.string.we_deadlines_pre, R.string.we_deadlines)
                .withContent(R.string.we_deadlines_content1, R.string.we_deadlines_content2));

        mWorkEthicViews.add(new WorkEthicItem(getActivity(), this)
                .withTitle(R.string.we_problems_pre, R.string.we_problems)
                .withContent(R.string.we_problems_content1, R.string.we_problems_content2));
    }

    @Override
    protected void onPostExecute() {
        for (View weView : mWorkEthicViews) mLL_content.addView(weView);
    }

    @Override
    public void expanded(WorkEthicItem item) {
        if (mExpandedItem == item) return;

        if (mExpandedItem != null) mExpandedItem.collapse();
        mExpandedItem = item;
    }
}
