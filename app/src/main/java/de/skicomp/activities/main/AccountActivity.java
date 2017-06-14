package de.skicomp.activities.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.skicomp.R;
import de.skicomp.SessionManager;
import de.skicomp.activities.BottomNavigationActivity;
import de.skicomp.activities.onboarding.OnboardingActivity;
import de.skicomp.data.manager.UserManager;
import de.skicomp.databinding.ActivityAccountBinding;

/**
 * Created by benjamin.schneider on 14.06.17.
 */

public class AccountActivity extends BottomNavigationActivity {

    private ActivityAccountBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_account);

        viewBinding.setUser(UserManager.getInstance().getUser());
        viewBinding.setHandler(this);
        viewBinding.setProfileHandler(this);

        Glide.with(this)
                .load(String.format(getString(R.string.heroku_profile_picture_url), SessionManager.getInstance().getUsername()))
                .placeholder(R.drawable.googleg_standard_color_18)
                .error(R.drawable.googleg_standard_color_18)
                .into(viewBinding.civProfilePicture);
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.rl_menu_profile;
    }

    @Override
    public void setSelectedMenuItem() {
        viewBinding.navigation.btMenuProfileSelected.setVisibility(View.VISIBLE);
        viewBinding.navigation.btMenuProfile.setImageResource(R.drawable.ic_menu_account_selected);
    }

    @SuppressWarnings("unused")
    public void onClickUpdateData(View view) {
        Toast.makeText(getApplicationContext(), "Daten aktualisieren", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("unused")
    public void onClickLogout(View view) {
        Intent intent = new Intent(this, OnboardingActivity.class);
        startActivity(intent);
        finish();
        SessionManager.getInstance().clearUserData();
    }

}
