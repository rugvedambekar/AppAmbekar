package ra.appambekar.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ra.appambekar.R;
import ra.appambekar.adapters.MenuAdapter;
import ra.appambekar.fragments.LoginFragment;
import ra.appambekar.fragments.android.AndroidFragment;
import ra.appambekar.fragments.apps.AppProfileFragment;
import ra.appambekar.fragments.HomeFragment;
import ra.appambekar.fragments.qualifications.QualificationsFragment;
import ra.appambekar.fragments.apps.AppsPagerFragment;
import ra.appambekar.helpers.FirebaseHelper;
import ra.appambekar.models.MenuOption;
import ra.appambekar.models.events.LoginEvent;
import ra.appambekar.models.events.NetworkEvent;

import static com.balysv.materialmenu.MaterialMenuDrawable.*;


public class MainActivity extends BaseToolbarActivity {

    private DrawerLayout mMenuLayout;
    private MenuAdapter mMenuAdapter;
    private ListView mNavMenuList;

    private ActionBarDrawerToggle mNavMenuToggle;
    private MenuOption mLastSelectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initializeBaseToolbar();
        initializeApplication();

        if (!FirebaseHelper.getInstance().isAuthenticated()) LoginFragment.ShowInstance(getSupportFragmentManager());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setHeader(R.string.main_header);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseHelper.getInstance().unAuthenticate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoggedIn(LoginEvent event) {
        mMenuAdapter.refreshIfRequired();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHasNetwork(NetworkEvent event) {
        mMenuAdapter.refreshIfRequired();
    }

    @Override
    protected void initializeApplication() {
        super.initializeApplication();

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
    public void onBackPressed() {
        if (!closeNavMenu()) super.onBackPressed();
    }

    @Override
    public void addContactFragment() {
        closeNavMenu();
        super.addContactFragment();
    }

    private boolean closeNavMenu() {
        if (mMenuLayout.isDrawerOpen(GravityCompat.START)) {
            mMenuLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void selectMenuOption(MenuOption option) {
        popContactFragment();

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

        mNavMenuList.setSelection(mMenuAdapter.getPosition(option));
        mNavMenuList.setItemChecked(mMenuAdapter.getPosition(option), true);

        mMenuLayout.closeDrawer(mNavMenuList);
    }

    private void startFragment(Fragment frag) {
        popContactFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, frag).commit();
    }
}
