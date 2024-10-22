package com.manipur.khannasi.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

class NavigateToFragment {
    companion object {
        fun navigateToFragment(activity: FragmentActivity, fragment: Fragment, id: Int) {
            activity.supportFragmentManager.beginTransaction().apply {
                replace(id, fragment)
                addToBackStack(null)
                commit()
            }
        }
        fun navigateToFragmentWithAddedBundle(fragmentManager: FragmentManager, fragment: Fragment, id: Int, bundle: Bundle? = null) {
            fragment.arguments = bundle
            fragmentManager.beginTransaction().apply {
                replace(id, fragment)
                addToBackStack(null)
                commit()
            }
        }
    }
}