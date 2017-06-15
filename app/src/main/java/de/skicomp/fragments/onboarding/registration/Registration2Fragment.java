package de.skicomp.fragments.onboarding.registration;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import de.skicomp.R;
import de.skicomp.databinding.FragmentRegistration2Binding;
import de.skicomp.utils.Utils;

/**
 * Created by benjamin.schneider on 15.06.17.
 */

public class Registration2Fragment extends Fragment {

    public static final String TAG = Registration2Fragment.class.getSimpleName();

    private FragmentRegistration2Binding viewBinding;

    private Registration2CompletedListener listener;

    public interface Registration2CompletedListener {
        void onCompletedRegistrationStep2(String firstname, String lastname, String city, String country);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration_2, container, false);

        return viewBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setListener((Registration2CompletedListener) getParentFragment());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) getParentFragment().getView().findViewById(R.id.tv_registration_toolbar_subtitle)).setText("2 / 3");
    }

    public void setListener(Registration2CompletedListener listener) {
        this.listener = listener;
    }

    public void onClickForward() {
        if (allFieldsFilled() && allFieldsValid()) {
            final String firstname = viewBinding.etFirstname.getText().toString();
            final String lastname = viewBinding.etLastname.getText().toString();
            final String city = viewBinding.etCity.getText().toString();
            final String country = viewBinding.etCountry.getText().toString();

            listener.onCompletedRegistrationStep2(firstname, lastname, city, country);
        }
    }

    private boolean allFieldsFilled() {
        boolean allFieldsFilled = true;

        if (viewBinding.etFirstname.getText().toString().isEmpty()) {
            viewBinding.etFirstname.setError();
            allFieldsFilled = false;
        }

        if (viewBinding.etLastname.getText().toString().isEmpty()) {
            viewBinding.etLastname.setError();
            allFieldsFilled = false;
        }

        return allFieldsFilled;
    }

    private boolean allFieldsValid() {
        Pattern namePattern = Pattern.compile("^[a-zA-Z]*$");

        if (!namePattern.matcher(viewBinding.etFirstname.getText().toString()).matches()) {
            viewBinding.etFirstname.setError();
            Utils.showToast(getContext(), R.string.error_firstname_not_valid, Toast.LENGTH_SHORT);
            return false;
        }

        if (!namePattern.matcher(viewBinding.etLastname.getText().toString()).matches()) {
            viewBinding.etLastname.setError();
            Utils.showToast(getContext(), R.string.error_lastname_not_valid, Toast.LENGTH_SHORT);
            return false;
        }

        return true;
    }
}
