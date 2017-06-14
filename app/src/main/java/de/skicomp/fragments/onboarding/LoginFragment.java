package de.skicomp.fragments.onboarding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import de.skicomp.R;
import de.skicomp.SessionManager;
import de.skicomp.activities.BaseActivity;
import de.skicomp.activities.onboarding.OnboardingActivity;
import de.skicomp.data.manager.UserManager;
import de.skicomp.databinding.FragmentLoginBinding;
import de.skicomp.dialogs.ForgotPasswordDialog;
import de.skicomp.events.user.UserEvent;
import de.skicomp.events.user.UserEventFailure;
import de.skicomp.events.user.UserEventSuccess;
import de.skicomp.utils.ProgressDialogManager;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 10.05.17.
 */

public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();

    private FragmentLoginBinding viewBinding;

    private String username;
    private String password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        viewBinding.setHandler(this);

        return viewBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
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
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, new ForgotPasswordDialog())
                .addToBackStack(ForgotPasswordDialog.TAG)
                .commit();
    }

    private boolean allFieldsValid() {
        if (username.isEmpty()) {
            Toast.makeText(getContext(), "Username fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(getContext(), "Passwort fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
