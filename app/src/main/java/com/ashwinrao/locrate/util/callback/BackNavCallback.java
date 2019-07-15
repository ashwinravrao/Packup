package com.ashwinrao.locrate.util.callback;

public interface BackNavCallback {

    /***
     * Custom callback function that overrides the system level callback of the same name
     *
     * @return boolean value indicating whether the back press was consumed completely by the custom callback.
     * If the back press was consumed completely, no super calls are necessary.
     */

    boolean onBackPressed();
}
