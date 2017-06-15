package de.skicomp.fragments.onboarding.registration;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.skicomp.R;
import de.skicomp.databinding.FragmentRegistration3Binding;

/**
 * Created by benjamin.schneider on 15.06.17.
 */

public class Registration3Fragment extends Fragment {

    public static final String TAG = Registration3Fragment.class.getSimpleName();

    private FragmentRegistration3Binding viewBinding;

    private Registration3Fragment.Registration3CompletedListener listener;

    public interface Registration3CompletedListener {
        void onCompletedRegistrationStep3(Bitmap bitmap);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration_3, container, false);

        return viewBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setListener((Registration3Fragment.Registration3CompletedListener) getParentFragment());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) getParentFragment().getView().findViewById(R.id.tv_registration_toolbar_subtitle)).setText("3 / 3");
        ((Button) getParentFragment().getView().findViewById(R.id.bt_forward)).setText(R.string.onboarding_register_button_complete);
    }

    public void setListener(Registration3Fragment.Registration3CompletedListener listener) {
        this.listener = listener;
    }

    public void onClickForward() {
        listener.onCompletedRegistrationStep3(null);
    }
}
