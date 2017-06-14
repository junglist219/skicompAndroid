package de.skicomp.utils;

import android.content.Context;

import de.skicomp.dialogs.LoadingDialog;

/**
 * Created by benjamin.schneider on 18.05.17.
 */

public class ProgressDialogManager {

    private static ProgressDialogManager instance;

    private Context context;
    private LoadingDialog loadingDialog;

    private ProgressDialogManager(Context context) {
        this.context = context;
    }

    public static ProgressDialogManager createInstance(Context context) {
        if (instance == null) {
            synchronized (ProgressDialogManager.class) {
                if (instance == null) {
                    instance = new ProgressDialogManager(context);
                }
            }
        }
        return instance;
    }

    public static ProgressDialogManager getInstance() {
        return instance;
    }

    public void startProgressDialog(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
            loadingDialog.show();
            loadingDialog.startAnimation();
        }
    }

    public void stopProgressDialog() {
        if (loadingDialog != null) {
            loadingDialog.stopAnimation();
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

}
