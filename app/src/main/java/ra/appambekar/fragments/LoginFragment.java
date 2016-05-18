package ra.appambekar.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ra.appambekar.AmbekarApplication;
import ra.appambekar.R;
import ra.appambekar.helpers.FirebaseHelper;
import ra.appambekar.helpers.SharedPrefsHelper;
import ra.appambekar.helpers.SharedPrefsHelper.UserInfo;
import ra.appambekar.models.events.LoginEvent;
import ra.appambekar.models.events.NetworkEvent;
import ra.appambekar.utilities.LayoutUtils;
import ra.smarttextview.SmartTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends DialogFragment {

    private static final float ALPHA_OFF = 0.35f;

    private static LoginFragment mInstance;

    public static void ShowInstance(FragmentManager fManager) {
        if (mInstance != null) mInstance.dismiss();

        mInstance = new LoginFragment();
        mInstance.show(fManager, "");
    }

    private Dialog mLoginDialog;

    private View mDialogContainer, mContentContainer;
    private SmartTextView mTitle, mDesc;
    private SmartTextView mRegister, mGuest, mLogin;
    private EditText mName, mEmail, mPass;

    private boolean mSetForRegister = false;

    public LoginFragment() { }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mLoginDialog = new Dialog(getActivity());
        mLoginDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mLoginDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mLoginDialog.setContentView(R.layout.login_dialog);

        mDialogContainer = mLoginDialog.findViewById(R.id.container_loginDialog);
        mContentContainer = mLoginDialog.findViewById(R.id.container_loginContent);

        mTitle = (SmartTextView) mLoginDialog.findViewById(R.id.tv_loginHeader);
        mDesc = (SmartTextView) mLoginDialog.findViewById(R.id.tv_loginInfo);

        mRegister = (SmartTextView) mLoginDialog.findViewById(R.id.tv_register);
        mGuest = (SmartTextView) mLoginDialog.findViewById(R.id.tv_guest);
        mLogin = (SmartTextView) mLoginDialog.findViewById(R.id.tv_login);

        mName = (EditText) mLoginDialog.findViewById(R.id.et_name);
        mEmail = (EditText) mLoginDialog.findViewById(R.id.et_email);
        mPass = (EditText) mLoginDialog.findViewById(R.id.et_pass);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mSetForRegister) setForLogin();
                else setForRegister();
            }
        });

        mGuest.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
            }
        });

        mLoginDialog.show();
        setForLogin();

        mLoginDialog.setCanceledOnTouchOutside(false);
        setCancelable(false);

        return mLoginDialog;
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
        if (event.loginSuccess) dismiss();
        else Toast.makeText(getActivity(), event.failureMsg, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHasNetwork(NetworkEvent event) {
        setForLogin();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mInstance = null;
    }

    private void setForRegister() {
        mTitle.setText(R.string.l_heading_register);
        mDesc.setText(R.string.l_info_reg);

        mRegister.setText(R.string.l_heading_cancel);
        mLogin.setText(R.string.l_heading_submit);

        mEmail.setHint(R.string.l_email_hint_reg);
        mPass.setHint(R.string.l_pass_hint_reg);

        mName.setVisibility(View.VISIBLE);
        mName.requestFocus();

        LayoutUtils.View.Show(mDialogContainer, true);

        mLogin.setOnClickListener(mOnSubmitClick);
        mSetForRegister = true;

        setAccessControl();
    }

    private void setForLogin() {
        mTitle.setText(R.string.l_heading_login);
        mDesc.setText(R.string.l_info_login);

        mRegister.setText(R.string.l_heading_register);
        mLogin.setText(R.string.l_heading_login);

        mEmail.setHint(R.string.l_email_hint_login);
        mPass.setHint(R.string.l_pass_hint_login);

        if (AmbekarApplication.hasActiveConnection()) {
            mEmail.setText(SharedPrefsHelper.getInstance().getUserInfo(UserInfo.Email));
            mPass.setText(SharedPrefsHelper.getInstance().getUserInfo(UserInfo.Password));
        }

        if (mEmail.getText().length() == 0) mEmail.requestFocus();
        else mLogin.requestFocus();

        LayoutUtils.View.Show(mDialogContainer, true);
        mName.setVisibility(View.GONE);

        LayoutUtils.View.SetAsButton(mLogin, mOnLoginClick);
        mSetForRegister = false;

        setAccessControl();
    }

    private void setAccessControl() {
        boolean mHasConnection = AmbekarApplication.hasActiveConnection();
        mContentContainer.setAlpha(mHasConnection ? 1 : ALPHA_OFF);
        for (int childIndex = 0; childIndex < ((ViewGroup) mContentContainer).getChildCount(); childIndex++) {
            View child = ((ViewGroup) mContentContainer).getChildAt(childIndex);
            child.setFocusableInTouchMode(mHasConnection);
            child.setFocusable(mHasConnection);
            child.setEnabled(mHasConnection);
        }

        mRegister.setAlpha(mHasConnection ? 1 : ALPHA_OFF);
        mRegister.setClickable(mHasConnection);
        mRegister.setEnabled(mHasConnection);

        mLogin.setAlpha(mHasConnection ? 1 : ALPHA_OFF);
        mLogin.setClickable(mHasConnection);
        mLogin.setEnabled(mHasConnection);
    }

    private View.OnClickListener mOnSubmitClick = new View.OnClickListener() {
        @Override public void onClick(View v) {
            FirebaseHelper.getInstance().createAccount(mName.getText().toString(), mEmail.getText().toString(), mPass.getText().toString(),
                    new Runnable() { @Override public void run() {
                            setForLogin();
                    } });
        }
    };

    private Runnable mOnLoginClick = new Runnable() { @Override public void run() {
        mLogin.setOnClickListener(null);
        FirebaseHelper.getInstance().authenticate(mEmail.getText().toString(), mPass.getText().toString());
    } };

}
