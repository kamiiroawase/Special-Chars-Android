package com.github.kamiiroawase.specialchars.fragment

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    fun View.setStatusBarPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(
                v.paddingLeft,
                statusBarInsets.top,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }
    }
}
