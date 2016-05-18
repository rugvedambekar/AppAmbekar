package ra.appambekar.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.android.volley.toolbox.NetworkImageView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ra.appambekar.AmbekarApplication;
import ra.appambekar.R;
import ra.appambekar.helpers.FirebaseHelper;
import ra.appambekar.helpers.VolleyHelper;
import ra.appambekar.models.ContactCard;
import ra.appambekar.models.events.LoginEvent;
import ra.appambekar.models.events.NetworkEvent;
import ra.appambekar.utilities.LayoutUtils;
import ra.appambekar.views.ErrorItemView;
import ra.appambekar.views.IconListItem;
import ra.smarttextview.SmartTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private static final String TAG = ContactFragment.class.getSimpleName();

    private static final float ALPHA_BASE = 0.10f;

    private ScrollView mScrollView;
    private SmartTextView mThanksMsg;
    private NetworkImageView mProfilePic;
    private IconListItem mLocation, mEmail, mPhone;
    private ErrorItemView mErrorView;
    private View mPicFrame;

    private ViewTreeObserver.OnScrollChangedListener mScrollListener;

    private Typeface mThanksTypeface;
    private ContactCard mContactCard = null;
    private boolean mHasActiveConnection = false;

    public ContactFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThanksTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Wolf.ttf");
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
        if (mHasActiveConnection = AmbekarApplication.hasActiveConnection()) {
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        mProfilePic = (NetworkImageView) rootView.findViewById(R.id.niv_profilePic);
        mScrollView = (ScrollView) rootView.findViewById(R.id.scrollContainer_contact);

        mErrorView = (ErrorItemView) rootView.findViewById(R.id.eiv_contact);
        mPicFrame = rootView.findViewById(R.id.fl_picFrame);

        mThanksMsg = (SmartTextView) rootView.findViewById(R.id.tv_thanks);
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

        mThanksMsg.setAlpha(0);
        mThanksMsg.setTypeface(mThanksTypeface);
        mThanksMsg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                mThanksMsg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                LayoutUtils.View.AdjustTextSizeForWidth(mThanksMsg, 0);
            }
        });

        mProfilePic.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        mProfilePic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        mPicFrame.getLayoutParams().height = mProfilePic.getMeasuredHeight();
                        mPicFrame.requestLayout();
                    }
                });

        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                // restrict all click and scroll features for view can child views
                return !mHasActiveConnection || !FirebaseHelper.getInstance().isAuthenticated();
            }
        });
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(mScrollListener = new ViewTreeObserver.OnScrollChangedListener() {
            float maxScrollY = 0, lastScrollY = 0;

            @Override public void onScrollChanged() {
                if (lastScrollY == mScrollView.getScrollY()) return;
                if (maxScrollY == 0) maxScrollY = mScrollView.getChildAt(0).getHeight() - mScrollView.getHeight();
                lastScrollY = mScrollView.getScrollY();

                float percentScrolledY = Math.min(lastScrollY / maxScrollY, 1);
                mPicFrame.setAlpha(1 - percentScrolledY);

                if (percentScrolledY >= 0.85 && mThanksMsg.getAlpha() == 0) mThanksMsg.animate().alpha(0.75f).setDuration(500).start();
                else if (percentScrolledY < 0.85 && mThanksMsg.getAlpha() > 0) mThanksMsg.animate().alpha(0).setDuration(500).start();

                Log.d(TAG, String.format("onScrolled:: scrolledY=%f maxScrollY=%f alphaDelta=%f", lastScrollY, maxScrollY, (1 - ALPHA_BASE) * percentScrolledY));
            }
        });

        setContactInfo();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (mScrollView != null && mScrollListener != null) {
            mScrollView.getViewTreeObserver().removeOnScrollChangedListener(mScrollListener);
            mScrollListener = null;
        }

        super.onDestroyView();
    }

    private void setContactInfo() {
        if (mLocation != null && mContactCard != null) mLocation.switchContent(mContactCard.getAddress());
        if (mEmail != null && mContactCard != null) mEmail.switchContent(mContactCard.getEmail());
        if (mPhone != null && mContactCard != null) mPhone.switchContent(mContactCard.getPhone());

        setErrorView();

        if (mProfilePic != null && mContactCard != null) {
            mProfilePic.postDelayed(new Runnable() {
                @Override public void run() {
                    mProfilePic.setImageUrl(mContactCard.getPicURL(), VolleyHelper.getInstance().getImageLoader());
                }
            }, 1000);
        }
    }

    private void setErrorView() {
        if (mErrorView == null) return;

        if (!mHasActiveConnection) {
            mErrorView.setForNoConnection(R.string.e_no_connection_info_contact, 0);
            mErrorView.setVisibility(View.VISIBLE);

        } else if (!FirebaseHelper.getInstance().isAuthenticated()) {
            mErrorView.setForNoAuthentication(R.string.e_no_auth_info_contact, 0);
            mErrorView.setVisibility(View.VISIBLE);

        } else if (mErrorView.getVisibility() == View.VISIBLE) {
            mErrorView.animate().alpha(0).setDuration(750).withEndAction(new Runnable() {
                @Override public void run() { mErrorView.setVisibility(View.GONE); }
            }).start();

        }
    }
}
