package de.skicomp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import com.wang.avi.AVLoadingIndicatorView;

import de.skicomp.R;

/**
 * Created by benjamin.schneider on 18.05.17.
 */

public class LoadingDialog extends Dialog {

    private AVLoadingIndicatorView avi;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
    }

    public void startAnimation() {
        avi.show();
    }

    public void stopAnimation() {
        avi.hide();
    }

}
