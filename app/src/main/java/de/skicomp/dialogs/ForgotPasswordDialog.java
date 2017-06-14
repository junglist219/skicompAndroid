package de.skicomp.dialogs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import de.skicomp.R;
import de.skicomp.activities.BaseActivity;
import de.skicomp.databinding.DialogForgotPasswordBinding;
import de.skicomp.network.SkiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 11.05.17.
 */

public class ForgotPasswordDialog extends DialogFragment {

    public static final String TAG = ForgotPasswordDialog.class.getSimpleName();

    private DialogForgotPasswordBinding viewBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_forgot_password, container, false);
        viewBinding.setHandler(this);

        setStyle(STYLE_NORMAL, R.style.FullscreenDialog);
        setCancelable(false);

        return viewBinding.getRoot();
    }

    @SuppressWarnings("unused")
    public void onClickClose(View view) {
        getActivity().onBackPressed();
    }

    @SuppressWarnings("unused")
    public void onClickRequestNewPassword(View view) {
        if (allFieldsValid()) {
            final String username = viewBinding.etUsername.getText().toString();

            SkiService.getInstance().forgotPassword(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        dismiss();
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    } else {
                        ((BaseActivity) getActivity()).handleError(response);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
                }
            }, username);
        }
    }

    private boolean allFieldsValid() {
        if (viewBinding.etUsername.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Username fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
