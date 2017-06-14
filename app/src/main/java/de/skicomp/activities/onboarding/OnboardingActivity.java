package de.skicomp.activities.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import de.skicomp.R;
import de.skicomp.activities.BaseActivity;
import de.skicomp.activities.main.AccountActivity;
import de.skicomp.fragments.onboarding.OnboardingFragment;

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
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }
}
