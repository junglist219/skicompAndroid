package de.skicomp.fragments.myprofile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import de.skicomp.R;
import de.skicomp.SessionManager;
import de.skicomp.activities.BaseActivity;
import de.skicomp.data.manager.UserManager;
import de.skicomp.databinding.FragmentChangePasswordBinding;
import de.skicomp.events.user.UserEvent;
import de.skicomp.events.user.UserEventFailure;
import de.skicomp.events.user.UserEventSuccess;
import de.skicomp.models.User;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 11.05.17.
 */

public class ChangePasswordFragment extends Fragment {

    public static final String TAG = ChangePasswordFragment.class.getSimpleName();

    private FragmentChangePasswordBinding viewBinding;

    private String updatedPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false);
        viewBinding.setHandler(this);

        return viewBinding.getRoot();
    }

    @SuppressWarnings("unused")
    public void onClickUpdatePassword(View view) {
        if (allFieldsValid()) {
            updatedPassword = viewBinding.etNewPassword.getText().toString();
            User updatedUser = UserManager.getInstance().getUser();
            updatedUser.setPassword(updatedPassword);

            UserManager.getInstance().updateUser(updatedUser);
        }
    }

    private boolean allFieldsValid() {
        if (viewBinding.etOldPassword.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Altes Passwort fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (viewBinding.etNewPassword.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Neues Passwort fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (viewBinding.etConfirmNewPassword.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Neues Passwort fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!viewBinding.etNewPassword.getText().toString().equals(viewBinding.etConfirmNewPassword.getText().toString())) {
            Toast.makeText(getContext(), "Passwörter stimmen nicht überein", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onUserEvent(UserEvent event) {
        if (event instanceof UserEventSuccess) {
            Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
            viewBinding.etOldPassword.setText("");
            viewBinding.etNewPassword.setText("");
            viewBinding.etConfirmNewPassword.setText("");

            SessionManager.getInstance().setPassword(updatedPassword);
        } else {
            Response response = ((UserEventFailure) event).getResponse();
            ((BaseActivity) getActivity()).handleError(response);
        }
    }
}
