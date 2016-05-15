package ra.appambekar.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;

import ra.appambekar.R;
import ra.appambekar.fragments.ContactFragment;
import ra.appambekar.utilities.LayoutUtils;
import ra.appambekar.views.RAToolbar;

import static com.balysv.materialmenu.MaterialMenuDrawable.*;

/**
 * Created by rugvedambekar on 2016-02-22.
 */
public class BaseToolbarActivity extends AppCompatActivity {

    private static final float ALPHA_OFF = 0.25f;
    private static final int ALPHA_DUR = 300;

    private RAToolbar mToolbar;
    private MaterialMenuIconToolbar mMaterialMenu;
    private ImageView mContactLogo;

    private boolean mShowingContactInfo = false;
    private long mLastBackPress = 0;
    private Toast mBackToast = null;

    protected void initializeToolbar() {
        mToolbar = (RAToolbar) findViewById(R.id.main_toolbar);
        if (mToolbar == null) return;

        mContactLogo = (ImageView) mToolbar.findViewById(R.id.iv_logoContact);
        mMaterialMenu = new MaterialMenuIconToolbar(this, Color.WHITE, Stroke.THIN) {
            @Override public int getToolbarViewId() {
                return R.id.main_toolbar;
            }
        };

        mToolbar.setTitleTextColor(Color.WHITE);

        mContactLogo.setAlpha(ALPHA_OFF);
        mContactLogo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (!popContactFragment()) addContactFragment();
            }
        });

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.toolbar_main_dark));
        }
    }

    @Override
    public void onBackPressed() {

        if (popContactFragment()) return;

        if (System.currentTimeMillis() - mLastBackPress < 2000) {
            mBackToast.cancel();
            super.onBackPressed();

        } else {
            mBackToast = Toast.makeText(this, R.string.back_notification, Toast.LENGTH_SHORT);
            mLastBackPress = System.currentTimeMillis();
            mBackToast.show();
        }
    }

    public boolean popContactFragment() {
        if (mShowingContactInfo) {
            mShowingContactInfo = false;
            getSupportFragmentManager().popBackStack();
            mContactLogo.animate().alpha(ALPHA_OFF).setDuration(ALPHA_DUR).start();
            return true;
        }

        return false;
    }

    public void addContactFragment() {
        mShowingContactInfo = true;
        mContactLogo.animate().alpha(1).setDuration(ALPHA_DUR).start();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.frag_slide_in, R.anim.frag_slide_out, R.anim.frag_slide_in, R.anim.frag_slide_out)
                .add(R.id.container_main, new ContactFragment())
                .addToBackStack(null)
                .commit();
    }

    protected Toolbar getToolbar() { return mToolbar; }
    protected MaterialMenuIconToolbar getMaterialMenu() { return mMaterialMenu; }
    protected View getHeaderView() { return mToolbar == null ? null : mToolbar.getHeaderView(); }

    protected void setHeader(final int resId) {
        getToolbar().setTitle(resId);
        getHeaderView().animate().alpha(1).setDuration(150).start();
    }
}
