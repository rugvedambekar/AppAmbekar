package ra.appambekar.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.toolbox.NetworkImageView;

import ra.appambekar.AmbekarApplication;
import ra.appambekar.R;
import ra.appambekar.helpers.MenuHelper;
import ra.appambekar.helpers.MenuHelper.MenuListener;
import ra.appambekar.helpers.VolleyHelper;
import ra.appambekar.models.MenuOption;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;

/**
 * Created by rugvedambekar on 2016-02-22.
 */
public class MenuAdapter extends ArrayAdapter<MenuOption> implements MenuListener {

    private LayoutInflater mInflater;
    private MenuHelper mMenuHelper;

    public MenuAdapter(Activity context) {
        super(context, -1);

        mInflater = context.getLayoutInflater();
        mMenuHelper = MenuHelper.getInstance();
        mMenuHelper.loadMenu(this);
    }

    @Override
    public int getCount() {
        return mMenuHelper.getOptions().size();
    }

    @Override
    public MenuOption getItem(int position) {
        return mMenuHelper.getOptions().get(position);
    }

    @Override
    public int getPosition(MenuOption item) {
        return mMenuHelper.getOptions().indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuOption mOption = getItem(position);

        if (mOption.isNoConnection()) {
            return mInflater.inflate(R.layout.item_no_connection_menu, null);
        }

        convertView = mInflater.inflate(R.layout.item_nav_menu, null);
        NetworkImageView iv_icon = (NetworkImageView) convertView.findViewById(R.id.iv_menuItemIcon);
        SmartTextView tv_title = (SmartTextView) convertView.findViewById(R.id.tv_menuItemTitle);

        if (mOption.getTitleID() != -1) tv_title.setText(mOption.getTitleID());
        else tv_title.setText(mOption.getTitle());

        if (mOption.isHeading()) {
            int vPadding = (int) LayoutUtils.Convert.DpToPx(10, getContext());
            convertView.setPadding(convertView.getPaddingLeft(), vPadding * 2, convertView.getPaddingRight(), vPadding);

            tv_title.setFontType(SmartTextView.FontType.Thick);
            tv_title.setAllCaps(true);
            tv_title.setTextSize(14);
        }

        if (mOption.getAppCount() > 1) tv_title.setPadding((int) LayoutUtils.Convert.DpToPx(15, getContext()), 0, 0, 0);

        if (!mOption.isApp() || mOption.getAppCount() > 1) iv_icon.setVisibility(View.GONE);
        else iv_icon.setImageUrl(mOption.getAppInfo().getLogoURL(), VolleyHelper.getInstance().getImageLoader());

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).isHeading();
    }

    public void refreshIfRequired() {
        if (!mMenuHelper.hasDynamicMenu()) mMenuHelper.loadMenu(this);
    }

    @Override
    public void onFullMenuLoaded() {
        notifyDataSetChanged();
    }
}
