package de.skicomp.views.fastscroll;

/**
 * Created by benjamin.schneider on 23.06.17.
 */

public interface OnFastScrollStateChangeListener {

    /**
     * Called when fast scrolling begins
     */
    void onFastScrollStart();

    /**
     * Called when fast scrolling ends
     */
    void onFastScrollStop();

}
