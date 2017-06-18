package de.skicomp.activities.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import de.skicomp.SessionManager;
import de.skicomp.activities.BaseActivity;
import de.skicomp.activities.main.AccountActivity;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;
        if (SessionManager.getInstance().isLoggedIn()) {
            intent = new Intent(LaunchActivity.this, AccountActivity.class);
        } else {
            intent = new Intent(LaunchActivity.this, OnboardingActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
