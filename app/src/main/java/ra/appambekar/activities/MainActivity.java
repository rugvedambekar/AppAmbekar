package ra.appambekar.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Timer;

import ra.appambekar.R;
import ra.appambekar.adapters.MenuAdapter;
import ra.appambekar.fragments.android.AndroidFragment;
import ra.appambekar.fragments.apps.AppProfileFragment;
import ra.appambekar.fragments.HomeFragment;
import ra.appambekar.fragments.qualifications.QualificationsFragment;
import ra.appambekar.fragments.apps.AppsPagerFragment;
import ra.appambekar.models.MenuOption;

import static com.balysv.materialmenu.MaterialMenuDrawable.*;


public class MainActivity extends BaseToolbarActivity {

    private DrawerLayout mMenuLayout;
    private MenuAdapter mMenuAdapter;
    private ListView mNavMenuList;

    private ActionBarDrawerToggle mNavMenuToggle;
    private MenuOption mLastSelectedOption;

    private long mLastBackPress = 0;
    private Toast mBackToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeToolbar();
        initializeNavMenu();

        setHeader(R.string.main_header);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastBackPress < 2000) {
            mBackToast.cancel();
            super.onBackPressed();

        } else {
            mBackToast = Toast.makeText(this, R.string.back_notification, Toast.LENGTH_SHORT);
            mLastBackPress = System.currentTimeMillis();
            mBackToast.show();
        }
    }

    private void initializeNavMenu() {
        mMenuLayout = (DrawerLayout) findViewById(R.id.dl_mainMenu);
        mNavMenuList = (ListView) findViewById(R.id.lv_mainMenu);
        mMenuAdapter = new MenuAdapter(this);

        mNavMenuList.setAdapter(mMenuAdapter);
        mNavMenuList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mNavMenuList.setDividerHeight(0);
        mNavMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectMenuOption(mMenuAdapter.getItem(position));
            }
        });

        mNavMenuToggle = new ActionBarDrawerToggle(this, mMenuLayout, getToolbar(), R.string.app_name, R.string.app_name) {
            private boolean isDrawerOpened;

            @Override public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setHeader(R.string.main_menu);
                isDrawerOpened = true;
            }
            @Override public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setHeader(R.string.main_header);
                isDrawerOpened = false;
            }

            @Override public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (getMaterialMenu() != null) {
                    getMaterialMenu().setTransformationOffset(AnimationState.BURGER_X,
                            isDrawerOpened ? 2 - slideOffset : slideOffset);
                }

                if (getHeaderView() != null) getHeaderView().setAlpha(getHeaderAlpha(slideOffset));
            }

            @Override public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);

                if (newState == DrawerLayout.STATE_IDLE && getMaterialMenu() != null) {
                    getMaterialMenu().setState(isDrawerOpened ? IconState.X : IconState.BURGER);
                }
            }

            private boolean shouldShowHeader(float slideOffset) {
                return slideOffset == 0 && !isDrawerOpened || slideOffset == 1 && isDrawerOpened;
            }
            private float getHeaderAlpha(float slideOffset) {
                return isDrawerOpened ? slideOffset : 1 - slideOffset;
            }
        };
        mMenuLayout.setDrawerListener(mNavMenuToggle);

        selectMenuOption(mMenuAdapter.getItem(0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void selectMenuOption(MenuOption option) {
        if (mLastSelectedOption == option) {
            mMenuLayout.closeDrawer(mNavMenuList);
            return;
        }

        mLastSelectedOption = option;

        switch (option.getTitleID()) {
            case R.string.m_home:
                startFragment(new HomeFragment());
                break;
            case R.string.m_qualifications:
                startFragment(new QualificationsFragment());
                break;
            case R.string.m_apps:
                startFragment(new AndroidFragment());
                break;
            case R.string.m_more_work:
                startFragment(new AppsPagerFragment().forApps(option.getAppInfoList()));
                break;
            case -1:
                if (option.isApp()) startFragment(new AppProfileFragment().forApp(option.getAppInfo()));
                break;
            default:
                return;
        }

        mNavMenuList.setSelection(option.getIndex());
        mNavMenuList.setItemChecked(option.getIndex(), true);

        mMenuLayout.closeDrawer(mNavMenuList);
    }

    private void startFragment(Fragment frag) {
        FragmentManager fManager = getSupportFragmentManager();
        fManager.beginTransaction().replace(R.id.container_main, frag).commit();
    }
}
