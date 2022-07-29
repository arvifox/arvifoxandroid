package com.arvifox.arvi.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import androidx.annotation.Nullable;

public final class FragmentUtils {

    private FragmentUtils() {
    }

    /**
     * Check is a fragment is visible by tag
     *
     * @param fragmentTag     tag of fragment that should be checked
     * @param fragmentManager fragment manager
     * @return true/false
     */
    public static boolean isFragmentVisible(final String fragmentTag, final FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        return fragment != null && fragment.isVisible();
    }

    /**
     * @param fragmentTag     tag of fragment that should be shown
     * @param fragmentManager fragment manager
     * @param containerResId  resource id for fragment container
     */
    public static void showFragmentByTag(final String fragmentTag, final FragmentManager fragmentManager,
                                         final int containerResId) {
        final FragmentTransaction fragmentTransition = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            return;
        }
        if (fragment.isHidden()) {
            fragmentTransition.show(fragment).commit();
        }
    }

    /**
     * Hide a visible fragment by tag
     *
     * @param fragmentTag     tag of fragment that should be hidden
     * @param fragmentManager fragment manager
     */
    public static void hideFragmentByTag(final String fragmentTag, final FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment == null || fragment.isHidden() || fragment.isRemoving()) {
            return;
        }
        fragmentManager.beginTransaction().hide(fragment).commit();
    }

    /**
     * Transition to safely remove fragments that were added to the container at runtime
     */
    public static void destroyFragmentByTag(final String fragmentTag, final FragmentManager childFragmentManager) {
        Fragment fragment = childFragmentManager.findFragmentByTag(fragmentTag);
        if (fragment != null && fragment.isAdded()
                && !fragment.isRemoving()) {
            childFragmentManager.beginTransaction().remove(fragment).commit();
        }
    }


    /**
     * Checks if the fragment is in state able to change view
     */
    public static boolean isFragmentActive(@Nullable Fragment frag) {
        return !(
                frag == null
                        || frag.isRemoving()
                        || frag.getActivity() == null
                        || frag.isDetached()
                        || !frag.isAdded()
                        || frag.getView() == null);
    }
}
