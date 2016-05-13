package ra.appambekar.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;

import ra.appambekar.R;
import ra.appambekar.utilities.LayoutUtils;
import ra.appambekar.views.RAToolbar;

import static com.balysv.materialmenu.MaterialMenuDrawable.*;

/**
 * Created by rugvedambekar on 2016-02-22.
 */
public class BaseToolbarActivity extends AppCompatActivity {

    private RAToolbar mToolbar;
    private MaterialMenuIconToolbar mMaterialMenu;

    protected void initializeToolbar() {
        mToolbar = (RAToolbar) findViewById(R.id.main_toolbar);
        if (mToolbar == null) return;

        mMaterialMenu = new MaterialMenuIconToolbar(this, Color.WHITE, Stroke.THIN) {
            @Override public int getToolbarViewId() {
                return R.id.main_toolbar;
            }
        };

        mToolbar.setTitleTextColor(Color.WHITE);

//        ((TextView)mToolbar.findViewById(R.id.toolbar_header)).setTypeface(TextVieypeface.createFromAsset(getAssets(), "fonts/Birds-of-Paradise.ttf"));

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    protected Toolbar getToolbar() { return mToolbar; }
    protected MaterialMenuIconToolbar getMaterialMenu() { return mMaterialMenu; }
    protected View getHeaderView() { return mToolbar == null ? null : mToolbar.getHeaderView(); }

    protected void setHeader(final int resId) {
        if (mToolbar == null) return;
        getToolbar().setTitle(resId);
        getHeaderView().animate().alpha(1).setDuration(150).start();
    }

    public void elevateToolbar(boolean elevate) {
    }
}
