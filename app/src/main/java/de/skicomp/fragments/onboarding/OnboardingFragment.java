package de.skicomp.fragments.onboarding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import de.skicomp.R;
import de.skicomp.SessionManager;
import de.skicomp.activities.BaseActivity;
import de.skicomp.activities.onboarding.OnboardingActivity;
import de.skicomp.data.manager.UserManager;
import de.skicomp.databinding.FragmentOnboardingBinding;
import de.skicomp.dialogs.ForgotPasswordDialog;
import de.skicomp.events.user.UserEvent;
import de.skicomp.events.user.UserEventFailure;
import de.skicomp.events.user.UserEventSuccess;
import de.skicomp.fragments.onboarding.registration.RegistrationContainerFragment;
import de.skicomp.utils.ProgressDialogManager;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 10.05.17.
 */

public class OnboardingFragment extends Fragment {

    public static final String TAG = OnboardingFragment.class.getSimpleName();

    private FragmentOnboardingBinding viewBinding;

    private String username;
    private String password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding, container, false);
        viewBinding.setHandler(this);
        viewBinding.llOnboardingContainer.requestFocus();

        return viewBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        viewBinding.llOnboardingContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings("unused")
    public void onClickLogin(View view) {
        username = viewBinding.etUsername.getText().toString();
        password = viewBinding.etPassword.getText().toString();

        if (allFieldsValid()) {
            ProgressDialogManager.getInstance().startProgressDialog(getContext());
            UserManager.getInstance().loadUser(username, password);
        }
    }

    @SuppressWarnings("unused")
    public void onClickForgotPassword(View view) {
        ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog();
        forgotPasswordDialog.show(getActivity().getSupportFragmentManager(), ForgotPasswordDialog.TAG);
    }

    @SuppressWarnings("unused")
    public void onClickRegister(View view) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right)
                .replace(R.id.fl_container, new RegistrationContainerFragment())
                .addToBackStack(RegistrationContainerFragment.TAG)
                .commit();
    }

    private boolean allFieldsValid() {
        boolean allFieldsValid = true;

        if (username.isEmpty()) {
            viewBinding.etUsername.setError();
            allFieldsValid = false;
        }

        if (password.isEmpty()) {
            viewBinding.etPassword.setError();
            allFieldsValid = false;
        }

        return allFieldsValid;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onUserEvent(UserEvent event) {
        ProgressDialogManager.getInstance().stopProgressDialog();
        if (event instanceof UserEventSuccess) {
            SessionManager.getInstance().setUsername(username);
            SessionManager.getInstance().setPassword(password);

            ((OnboardingActivity) getActivity()).startMainActivity();
        } else {
            Response response = ((UserEventFailure) event).getResponse();
            ((BaseActivity) getActivity()).handleError(response);
        }
    }
}
