package de.skicomp.fragments.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import de.skicomp.R;
import de.skicomp.SessionManager;
import de.skicomp.activities.BaseActivity;
import de.skicomp.activities.onboarding.OnboardingActivity;
import de.skicomp.data.manager.UserManager;
import de.skicomp.models.User;
import de.skicomp.network.SkiService;
import de.skicomp.utils.ImageUtils;
import de.skicomp.utils.ProgressDialogManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 10.05.17.
 */

public class RegistrationFragment extends Fragment {

    public static final String TAG = RegistrationFragment.class.getSimpleName();

    @BindView(R.id.civ_profile_picture) CircleImageView civProfilePicture;
    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.et_firstname) EditText etFirstname;
    @BindView(R.id.et_lastname) EditText etLastname;
    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_city) EditText etCity;
    @BindView(R.id.et_country) EditText etCountry;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.et_confirm_password) EditText etConfirmPassword;
    private Unbinder unbinder;

    private Bitmap profilePicture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_registration, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.civ_profile_picture)
    public void onClickProfilePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Profilbild auswählen"), OnboardingActivity.RESULT_SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch(requestCode) {
                case OnboardingActivity.RESULT_SELECT_PICTURE :
                    Uri selectedImageUri = data.getData();
                    try {
                        profilePicture = ImageUtils.handleSamplingAndRotationBitmap(getContext(), selectedImageUri);
                        civProfilePicture.setImageBitmap(profilePicture);
                    } catch (IOException e) {
                        Log.e(TAG, "IOException", e);
                    }
                    break;
                default :
                    break;
            }
        }
    }

    @OnClick(R.id.bt_register)
    public void onClickRegister() {
        if (allFieldsValid()) {
            ProgressDialogManager.getInstance().startProgressDialog(getContext());

            final String username = etUsername.getText().toString();
            final String firstname = etFirstname.getText().toString();
            final String lastname = etLastname.getText().toString();
            final String email = etEmail.getText().toString();
            final String city = etCity.getText().toString();
            final String country = etCountry.getText().toString();
            final String password = etPassword.getText().toString();

            User user = new User(username, firstname, lastname, email, password, city, country, true);
            SkiService.getInstance().register(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        SessionManager.getInstance().setUsername(username);
                        SessionManager.getInstance().setPassword(password);
                        UserManager.getInstance().setUser(response.body());

                        if (profilePicture != null) {
                            updateProfilePicture();
                        } else {
                            ProgressDialogManager.getInstance().stopProgressDialog();
                            ((OnboardingActivity) getActivity()).startMainActivity();
                        }
                    } else {
                        ProgressDialogManager.getInstance().stopProgressDialog();
                        ((BaseActivity) getActivity()).handleError(response);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    ProgressDialogManager.getInstance().stopProgressDialog();
                    Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
                }
            }, user);
        }
    }

    private void updateProfilePicture() {
        SkiService.getInstance().updateProfilePicture(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ProgressDialogManager.getInstance().stopProgressDialog();
                if (response.isSuccessful()) {
                    ((OnboardingActivity) getActivity()).startMainActivity();
                } else {
                    ((BaseActivity) getActivity()).handleError(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ProgressDialogManager.getInstance().stopProgressDialog();
                Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
            }
        }, SessionManager.getInstance().getUsername(), profilePicture);
    }

    private boolean allFieldsValid() {
        if (etUsername.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Username fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etFirstname.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Vorname fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etLastname.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Nachname fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "E-Mail fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etCity.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Stadt fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etCountry.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Land fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etPassword.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Passwort fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etConfirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Passwort fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            Toast.makeText(getContext(), "Passwörter stimmen nicht überein", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
