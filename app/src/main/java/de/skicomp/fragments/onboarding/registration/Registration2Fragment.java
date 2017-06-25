package de.skicomp.fragments.onboarding.registration;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
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

        ((TextView) getParentFragment().getView().findViewById(R.id.tv_registration_toolbar_subtitle)).setText("2 / 2");
        ((Button) getParentFragment().getView().findViewById(R.id.bt_forward)).setText(R.string.onboarding_register_button_complete);

        return viewBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setListener((Registration2CompletedListener) getParentFragment());
    }

    public void setListener(Registration2CompletedListener listener) {
        this.listener = listener;
    }

    public void onClickForward() {
        if (allFieldsFilled() && allFieldsValid() && addressValid()) {
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

    private boolean addressValid() {
        String city = viewBinding.etCity.getText().toString();
        String country = viewBinding.etCountry.getText().toString();

        String addressString;
        if (!city.isEmpty() && !country.isEmpty()) {
            addressString = city.concat(", ").concat(country);
        } else if (!city.isEmpty() && country.isEmpty()) {
            addressString = country;
        } else if (city.isEmpty() && !country.isEmpty()) {
            addressString = city;
        } else {
            return true;
        }

        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addressList = geocoder.getFromLocationName(addressString, 10);
            for (Address address : addressList) {
                String addressLocality = address.getLocality();
                String addressCountry = address.getCountryName();

                // validate city & country
                if (!city.isEmpty() && !country.isEmpty()
                    && StringUtils.getLevenshteinDistance(addressLocality, city) <= 2
                    && StringUtils.getLevenshteinDistance(addressCountry, country) <= 2) {

                    viewBinding.etCity.setText(addressLocality);
                    viewBinding.etCountry.setText(addressCountry);
                    return true;
                } else {
                    // validate city
                    if (!city.isEmpty() && StringUtils.getLevenshteinDistance(addressLocality, city) <= 2 && country.isEmpty()) {
                        viewBinding.etCity.setText(addressLocality);
                        viewBinding.etCountry.setText(addressCountry);
                        return true;
                    }

                    // validate country
                    if (!country.isEmpty() && StringUtils.getLevenshteinDistance(addressCountry, country) <= 2 && city.isEmpty()) {
                        viewBinding.etCountry.setText(addressCountry);
                        return true;
                    }
                }
            }

            Utils.showToast(getContext(), R.string.error_address_not_valid, Toast.LENGTH_SHORT);
            viewBinding.etCity.setError();
            viewBinding.etCountry.setError();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
