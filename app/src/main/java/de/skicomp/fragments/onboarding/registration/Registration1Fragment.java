package de.skicomp.fragments.onboarding.registration;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.skicomp.R;
import de.skicomp.databinding.FragmentRegistration1Binding;

/**
 * Created by benjamin.schneider on 15.06.17.
 */

public class Registration1Fragment extends Fragment {

    public static final String TAG = Registration1Fragment.class.getSimpleName();

    private FragmentRegistration1Binding viewBinding;

    private Registration1CompletedListener listener;

    public interface Registration1CompletedListener {
        void onCompletedRegistrationStep1(String username, String email, String password);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration_1, container, false);

        return viewBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setListener((Registration1CompletedListener) getParentFragment());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) getParentFragment().getView().findViewById(R.id.tv_registration_toolbar_subtitle)).setText("1 / 3");
    }

    public void setListener(Registration1CompletedListener listener) {
        this.listener = listener;
    }

    public void onClickForward() {
        resetErrors();
        if (allFieldsFilled() && allFieldsValid()) {
            final String username = viewBinding.etUsername.getText().toString();
            final String email = viewBinding.etEmail.getText().toString();
            final String password = viewBinding.etPassword.getText().toString();

            listener.onCompletedRegistrationStep1(username, email, password);
        }
    }

    private void resetErrors() {
        viewBinding.etUsername.setError(null);
        viewBinding.etEmail.setError(null);
        viewBinding.etPassword.setError(null);
        viewBinding.etConfirmPassword.setError(null);
    }

    private boolean allFieldsFilled() {
        boolean allFieldsFilled = true;

        if (viewBinding.etUsername.getText().toString().isEmpty()) {
            viewBinding.etUsername.setError();
            allFieldsFilled = false;
        }

        if (viewBinding.etEmail.getText().toString().isEmpty()) {
            viewBinding.etEmail.setError();
            allFieldsFilled = false;
        }

        if (viewBinding.etPassword.getText().toString().isEmpty()) {
            viewBinding.etPassword.setError();
            allFieldsFilled = false;
        }

        if (viewBinding.etConfirmPassword.getText().toString().isEmpty()) {
            viewBinding.etConfirmPassword.setError();
            allFieldsFilled = false;
        }

        return allFieldsFilled;
    }

    private boolean allFieldsValid() {
        if (!Patterns.EMAIL_ADDRESS.matcher(viewBinding.etEmail.getText().toString()).matches()) {
            viewBinding.etEmail.setError();
            Toast.makeText(getContext(), "E-Mail-Adresse fehlerhaft", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!viewBinding.etPassword.getText().toString().equals(viewBinding.etConfirmPassword.getText().toString())) {
            viewBinding.etPassword.setError();
            viewBinding.etConfirmPassword.setError();
            Toast.makeText(getContext(), "Passwörter stimmen nicht überein", Toast.LENGTH_SHORT).show();
            return false;
        }

        // validate password length & characters

        return true;
    }
}
