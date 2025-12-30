package com.github.kamiiroawase.specialchars.fragment

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    fun setStatusBarWrap(view: View) {
        @SuppressLint("DiscouragedApi", "InternalInsetResource")
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

        if (resourceId > 0) {
            view.setPadding(0, resources.getDimensionPixelSize(resourceId), 0, 0)
        }
    }
}
