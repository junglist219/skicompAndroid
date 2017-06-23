package de.skicomp.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import de.skicomp.R;

/**
 * Created by benjamin.schneider on 15.06.17.
 */

public class Utils {

    private Utils() {
        // utility class
    }

    public static void showSnackbar(View view, int textResID, int duration) {
        showSnackbar(view, view.getContext().getString(textResID), duration);
    }

    public static void showSnackbar(View view, String text, int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.transparent_black_75));
        snackbar.show();
    }

    public static void showToast(Context context, int textResID, int duration) {
        showToast(context, context.getString(textResID), duration);
    }

    public static void showToast(Context context, String text, int duration) {
        final View toastLayout = View.inflate(context, R.layout.view_toast, null);
        ((TextView) toastLayout.findViewById(R.id.tv_toast)).setText(text);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(toastLayout);
        toast.show();
    }
}
