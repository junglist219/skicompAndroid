package de.skicomp.activities.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import de.skicomp.R;
import de.skicomp.activities.BaseActivity;
import de.skicomp.activities.main.AccountActivity;
import de.skicomp.fragments.onboarding.OnboardingFragment;
import de.skicomp.fragments.onboarding.registration.RegistrationContainerFragment;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class OnboardingActivity extends BaseActivity {

    public static final int RESULT_SELECT_PICTURE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, new OnboardingFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (currentFragment instanceof RegistrationContainerFragment) {
            boolean handled = ((RegistrationContainerFragment) currentFragment).onBackPressed();
            if (handled) {
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }
}
