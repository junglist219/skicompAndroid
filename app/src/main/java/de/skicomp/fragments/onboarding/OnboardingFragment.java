package de.skicomp.fragments.onboarding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.skicomp.R;
import de.skicomp.databinding.FragmentOnboardingBinding;

/**
 * Created by benjamin.schneider on 10.05.17.
 */

public class OnboardingFragment extends Fragment {

    public static final String TAG = OnboardingFragment.class.getSimpleName();

    private FragmentOnboardingBinding viewBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding, container, false);
        viewBinding.setHandler(this);

        return viewBinding.getRoot();
    }

    @SuppressWarnings("unused")
    public void onClickLogin(View view) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, new LoginFragment())
                .addToBackStack(LoginFragment.TAG)
                .commit();
    }

    @SuppressWarnings("unused")
    public void onClickRegister(View view) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, new RegistrationFragment())
                .addToBackStack(RegistrationFragment.TAG)
                .commit();
    }
}
