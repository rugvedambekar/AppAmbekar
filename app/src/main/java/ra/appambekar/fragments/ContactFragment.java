package ra.appambekar.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ra.appambekar.AmbekarApplication;
import ra.appambekar.R;
import ra.appambekar.helpers.FirebaseHelper;
import ra.appambekar.models.ContactCard;
import ra.appambekar.models.events.LoginEvent;
import ra.appambekar.models.events.NetworkEvent;
import ra.appambekar.views.ErrorItemView;
import ra.appambekar.views.IconListItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private static final String TAG = ContactFragment.class.getSimpleName();

    private IconListItem mLocation, mEmail, mPhone;
    private ErrorItemView mErrorView;
    private ImageView mContactPic;

    private ContactCard mContactCard = null;
    private boolean mHasActiveConnection = false;

    public ContactFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadContactData();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoggedIn(LoginEvent event) {
        loadContactData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHasNetwork(NetworkEvent event) {
        loadContactData();
    }

    private void loadContactData() {
        if (mHasActiveConnection = AmbekarApplication.HasActiveConnection()) {
            FirebaseHelper.getInstance().getChildREF(FirebaseHelper.FireChild.Contact).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mContactCard = dataSnapshot.getValue(ContactCard.class);
                    setContactInfo();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    if (firebaseError.getCode() == FirebaseError.PERMISSION_DENIED) setContactInfo();
                    Log.e(TAG, firebaseError.toString());
                }
            });

        } else setContactInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        mContactPic = (ImageView) rootView.findViewById(R.id.iv_contactPic);
        mErrorView = (ErrorItemView) rootView.findViewById(R.id.eiv_contact);

        mLocation = (IconListItem) rootView.findViewById(R.id.ili_location);
        mEmail = (IconListItem) rootView.findViewById(R.id.ili_email);
        mPhone = (IconListItem) rootView.findViewById(R.id.ili_phone);

        mEmail.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mEmail.getContent().toString(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.c_email_subject));
                startActivity(Intent.createChooser(emailIntent, "Send Email."));
            }
        });

        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mPhone.getContent()));
                startActivity(Intent.createChooser(callIntent, "Make Call."));
            }
        });

        setContactInfo();

        return rootView;
    }

    private void setContactInfo() {
        if (mLocation != null && mContactCard != null) mLocation.switchContent(mContactCard.getAddress());
        if (mEmail != null && mContactCard != null) mEmail.switchContent(mContactCard.getEmail());
        if (mPhone != null && mContactCard != null) mPhone.switchContent(mContactCard.getPhone());


        if (!setErrorView() && mContactPic != null) mContactPic.animate().alpha(1).setDuration(750).start();
    }

    private boolean setErrorView() {
        if (mErrorView == null) return false;

        if (!mHasActiveConnection) {
            mErrorView.setForNoConnection(R.string.e_no_connection_info_contact, 0);
            mErrorView.setVisibility(View.VISIBLE);
            return true;

        } else if (!FirebaseHelper.getInstance().isAuthenticated()) {
            mErrorView.setForNoAuthentication(R.string.e_no_auth_info_contact, 0);
            mErrorView.setVisibility(View.VISIBLE);
            return true;

        } else if (mErrorView.getVisibility() == View.VISIBLE) {
            mErrorView.animate().alpha(0).setDuration(750).withEndAction(new Runnable() {
                @Override public void run() { mErrorView.setVisibility(View.GONE); }
            }).start();
            return false;
        }

        return mErrorView.getVisibility() == View.VISIBLE;
    }
}
