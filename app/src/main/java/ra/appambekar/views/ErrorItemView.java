package ra.appambekar.views;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ra.appambekar.R;
import ra.appambekar.activities.MainActivity;
import ra.appambekar.fragments.LoginFragment;
import ra.appambekar.models.ContactCard;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;

/**
 * Created by rugvedambekar on 2016-03-02.
 */
public class ErrorItemView extends LinearLayout implements View.OnClickListener {

    private ImageView mIV_error;
    private SmartTextView mTV_heading, mTV_info, mTV_details;

    private Type mErrorType = null;

    public ErrorItemView(Context context) {
        super(context);
        initialize(null);
    }

    public ErrorItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public ErrorItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        inflate(getContext(), R.layout.item_error, this);

        mIV_error = (ImageView) findViewById(R.id.iv_error);

        mTV_heading = (SmartTextView) findViewById(R.id.tv_errorHeading);
        mTV_details = (SmartTextView) findViewById(R.id.tv_errorDetails);
        mTV_info = (SmartTextView) findViewById(R.id.tv_errorInfo);

        setOnClickListener(this);
    }

    private void setInfoAndDetails(int infoID, int detailsID) {
        if (infoID <= 0) mTV_info.setVisibility(GONE);
        else mTV_info.setText(infoID);

        if (detailsID <= 0) mTV_details.setVisibility(GONE);
        else mTV_details.setText(detailsID);
    }

    private void setType(Type type) {
        if (mErrorType == type) return;

        mIV_error.setImageResource(type.imgID);
        mTV_heading.setText(type.headingID);

        if (mErrorType != null) LayoutUtils.View.Show(this, true);
        mErrorType = type;
    }

    public void setForNoConnection(int infoID, int detailsID) {
        setInfoAndDetails(infoID, detailsID);
        setType(Type.NoConnection);
    }

    public void setForNoAuthentication(int infoID, int detailsID) {
        setInfoAndDetails(infoID, detailsID);
        setType(Type.NoAuthentication);
    }

    @Override
    public void onClick(View v) {
        switch (mErrorType) {
            case NoConnection:
                getContext().startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                break;

            case NoAuthentication:
                AppCompatActivity parentActivity = LayoutUtils.View.getActivity(this);
                if (parentActivity != null) LoginFragment.ShowInstance(parentActivity.getSupportFragmentManager());
                break;
        }
    }

    public enum Type {
        NoConnection(R.drawable.ic_network, R.string.e_no_connection),
        NoAuthentication(R.drawable.ic_network, R.string.e_no_auth);

        public int imgID, headingID;

        Type(int img, int heading) {
            imgID = img;
            headingID = heading;
        }
    }
}
