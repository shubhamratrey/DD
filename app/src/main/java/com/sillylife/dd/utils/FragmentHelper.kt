package com.sillylife.dd.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**
 * Created on 26/09/18.
 */
object FragmentHelper {

    const val HOME = "home"
    const val CALENDAR = "calendar"
    const val EDIT_AND_ADD_EXPENSE_FRAGMENT = "edit_and_add_expense_fragment"

    fun replace(@IdRes containerId: Int, fragmentManager: FragmentManager, fragment: Fragment, tag: String) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(containerId, fragment, tag)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun add(@IdRes containerId: Int, fragmentManager: FragmentManager, fragment: Fragment, tag: String) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(containerId, fragment, tag)
        fragmentTransaction.addToBackStack(tag)

        val displayedFragment = fragmentManager.findFragmentById(containerId)

        if (displayedFragment != null) {
            fragmentTransaction.hide(displayedFragment)
        }

        fragmentTransaction.commitAllowingStateLoss()
    }

    fun add(@IdRes containerId: Int, fragmentManager: FragmentManager, fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(containerId, fragment)

        val displayedFragment = fragmentManager.findFragmentById(containerId)

        if (displayedFragment != null) {
            fragmentTransaction.hide(displayedFragment)
        }

        fragmentTransaction.commitAllowingStateLoss()
    }
}