package ra.appambekar.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.ArrayList;

import ra.appambekar.R;
import ra.appambekar.adapters.AppScreensAdapter;
import ra.appambekar.fragments.apps.AppScreenFragment;
import ra.appambekar.models.AppScreen;

public class AppScreenActivity extends AppCompatActivity {

    private static final String TAG = AppScreenActivity.class.getSimpleName();

    public static final String KEY_CURR_INDEX = "currIndex";
    public static final String KEY_SCREENS = "screens";

    private ViewPager mScreensPager;
    private AppScreensAdapter mScreensAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_full_pager);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle screensBundle = getIntent().getExtras();
        ArrayList<AppScreen> appScreens = screensBundle.getParcelableArrayList(KEY_SCREENS);
        int firstScreenIndex = screensBundle.getInt(KEY_CURR_INDEX);

        Log.d(TAG, "Current Screen: " + appScreens.get(firstScreenIndex).getTitle());
        for (AppScreen appScreen : appScreens) Log.d(TAG, "Screen: " + appScreen.getTitle());

        mScreensPager = (ViewPager) findViewById(R.id.viewPager_main);
        mScreensAdapter = new AppScreensAdapter(getSupportFragmentManager(), appScreens);

        mScreensPager.setAdapter(mScreensAdapter);
        mScreensPager.setCurrentItem(firstScreenIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
