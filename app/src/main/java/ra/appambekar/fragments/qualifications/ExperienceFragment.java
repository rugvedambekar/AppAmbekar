package ra.appambekar.fragments.qualifications;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ra.appambekar.R;
import ra.appambekar.fragments.BaseAsyncFragment;
import ra.appambekar.models.content.Experience;
import ra.appambekar.views.HeaderListView;
import ra.appambekar.views.VerticalViewSwitcher;


public class ExperienceFragment extends BaseAsyncFragment {

    private VerticalViewSwitcher mVVS_experience;
    private ArrayList<HeaderListView> mExperienceVVSViews;

    public ExperienceFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_experiene, container, false);

        mVVS_experience = (VerticalViewSwitcher) rootView.findViewById(R.id.xp_vvs);

        return rootView;
    }

    @Override
    protected void doInBackground() {
        mExperienceVVSViews = new ArrayList<>();
        for (Experience exp : Experience.values()) mExperienceVVSViews.add(new HeaderListView(getActivity(), exp));
    }

    @Override
    protected void onPostExecute() {
        for (HeaderListView expView : mExperienceVVSViews) mVVS_experience.addVVSView(expView);
        mVVS_experience.doneAddingViews();
    }
}
