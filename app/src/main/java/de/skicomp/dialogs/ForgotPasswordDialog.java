package de.skicomp.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.skicomp.R;
import de.skicomp.activities.BaseActivity;
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

    @BindView(R.id.et_username) EditText etUsername;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.dialog_forgot_password, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        setStyle(STYLE_NORMAL, R.style.FullscreenDialog);
        setCancelable(false);

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ib_close)
    public void onClickClose() {
        this.dismiss();
    }

    @OnClick(R.id.bt_request_new_password)
    public void onClickRequestNewPassword() {
        if (allFieldsValid()) {
            final String username = etUsername.getText().toString();

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
        if (etUsername.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Username fehlt", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
