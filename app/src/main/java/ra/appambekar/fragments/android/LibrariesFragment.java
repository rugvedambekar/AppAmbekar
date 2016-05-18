package ra.appambekar.fragments.android;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import ra.appambekar.R;
import ra.appambekar.fragments.BaseAsyncFragment;
import ra.appambekar.views.HeaderContentHSV;
import ra.smarttextview.SmartTextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class LibrariesFragment extends BaseAsyncFragment implements HeaderContentHSV.ContentObserver {

    private static final String TAG = LibrariesFragment.class.getSimpleName();

    private SmartTextView mTV_libraryInfo;
    private HeaderContentHSV mHCV_libraries;

    private HashMap<Integer, String[]> mLibInfoMap;

    public LibrariesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_libraries, container, false);

        mTV_libraryInfo = (SmartTextView) rootView.findViewById(R.id.tv_libraryInfo);
        mHCV_libraries = (HeaderContentHSV) rootView.findViewById(R.id.hcv_libraries);

        mTV_libraryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHCV_libraries.scrollToSelected();
            }
        });
        mHCV_libraries.setContentObserver(this);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) mHCV_libraries.initializeOnVisible();
    }

    @Override
    protected void doInBackground() {
        Resources res = getResources();

        mLibInfoMap = new HashMap<>();
        mLibInfoMap.put(R.array.a_networking_libs, res.getStringArray(R.array.a_networking_libs_info));
        mLibInfoMap.put(R.array.a_application_libs, res.getStringArray(R.array.a_application_libs_info));
        mLibInfoMap.put(R.array.a_storage_libs, res.getStringArray(R.array.a_storage_libs_info));
        mLibInfoMap.put(R.array.a_interface_libs, res.getStringArray(R.array.a_interface_libs_info));
        mLibInfoMap.put(R.array.a_testing_libs, res.getStringArray(R.array.a_testing_libs_info));
    }

    @Override
    protected void onPostExecute() {
        mHCV_libraries.addHeaderAndContent(R.string.a_networking, R.array.a_networking_libs);
        mHCV_libraries.addHeaderAndContent(R.string.a_application, R.array.a_application_libs);
        mHCV_libraries.addHeaderAndContent(R.string.a_storage, R.array.a_storage_libs);
        mHCV_libraries.addHeaderAndContent(R.string.a_interface, R.array.a_interface_libs);
        mHCV_libraries.addHeaderAndContent(R.string.a_testing, R.array.a_testing_libs);
    }

    @Override
    public void onClick(Pair viewDogTag) {
        mTV_libraryInfo.switchText(mLibInfoMap.get(viewDogTag.first)[(Integer) viewDogTag.second]);
    }
}
