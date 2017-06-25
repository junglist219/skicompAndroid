package de.skicomp.fragments.onboarding.registration;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;

import de.skicomp.R;
import de.skicomp.SessionManager;
import de.skicomp.activities.BaseActivity;
import de.skicomp.activities.onboarding.OnboardingActivity;
import de.skicomp.data.manager.UserManager;
import de.skicomp.databinding.FragmentRegistrationContainerBinding;
import de.skicomp.events.user.UserEvent;
import de.skicomp.events.user.UserEventFailure;
import de.skicomp.events.user.UserEventSuccess;
import de.skicomp.models.User;
import de.skicomp.utils.ProgressDialogManager;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 15.06.17.
 */

public class RegistrationContainerFragment extends Fragment implements Registration1Fragment.Registration1CompletedListener,
                                                                        Registration2Fragment.Registration2CompletedListener {

    public static final String TAG = RegistrationContainerFragment.class.getSimpleName();

    private FragmentRegistrationContainerBinding viewBinding;

    private int currentRegistrationStep = 1;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration_container, container, false);
        viewBinding.setHandler(this);

        initToolbar();

        getChildFragmentManager().beginTransaction()
                .setCustomAnimations(0, R.anim.bottom_sheet_slide_in)
                .replace(R.id.fl_container_registration, new Registration1Fragment(), Registration1Fragment.TAG)
                .commit();

        user = new User();

        return viewBinding.getRoot();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(viewBinding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressWarnings("unused")
    public void onClickForward(View view) {
        switch (currentRegistrationStep) {
            case 1:
                Registration1Fragment registration1Fragment = (Registration1Fragment) getChildFragmentManager().findFragmentByTag(Registration1Fragment.TAG);
                registration1Fragment.onClickForward();
                break;
            case 2:
                Registration2Fragment registration2Fragment = (Registration2Fragment) getChildFragmentManager().findFragmentByTag(Registration2Fragment.TAG);
                registration2Fragment.onClickForward();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCompletedRegistrationStep1(String username, String email, String password) {
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        getChildFragmentManager().beginTransaction()
                //.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right)
                .setCustomAnimations(R.anim.bottom_sheet_slide_in, R.anim.bottom_sheet_slide_out, R.anim.bottom_sheet_slide_in, R.anim.bottom_sheet_slide_out)
                .replace(R.id.fl_container_registration, new Registration2Fragment(), Registration2Fragment.TAG)
                .addToBackStack(Registration2Fragment.TAG)
                .commit();
        currentRegistrationStep++;
    }

    @Override
    public void onCompletedRegistrationStep2(String firstname, String lastname, String city, String country) {
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setCity(city);
        user.setCountry(country);

        ProgressDialogManager.getInstance().startProgressDialog(getContext());
        UserManager.getInstance().registerUser(user);
    }

    public boolean onBackPressed() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            if (currentRegistrationStep == 2) {
                Registration1Fragment registration1Fragment = (Registration1Fragment) getChildFragmentManager().findFragmentByTag(Registration1Fragment.TAG);
                registration1Fragment.setListener(this);
                viewBinding.tvRegistrationToolbarSubtitle.setText("1 / 2");
                viewBinding.btForward.setText(R.string.onboarding_register_button_forward);
            }
            currentRegistrationStep--;
            getChildFragmentManager().popBackStack();

            return true;
        }

        return false;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onUserEvent(UserEvent event) {
        ProgressDialogManager.getInstance().stopProgressDialog();
        if (event instanceof UserEventSuccess) {
            SessionManager.getInstance().setUsername(user.getUsername());
            SessionManager.getInstance().setPassword(user.getPassword());

            ((OnboardingActivity) getActivity()).startMainActivity();
        } else {
            Response response = ((UserEventFailure) event).getResponse();
            ((BaseActivity) getActivity()).handleError(response);
        }
    }
}
