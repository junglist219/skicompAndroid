package de.skicomp.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import de.skicomp.R;

/**
 * Created by benjamin.schneider on 15.06.17.
 */

public class SkiEditText extends AppCompatEditText {

    public SkiEditText(Context context) {
        super(context);
    }

    public SkiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SkiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_title));
    }

    public void setError() {
        this.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        this.setHintTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
    }
}
