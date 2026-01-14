package com.github.kamiiroawase.specialchars.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.base.App
import com.github.kamiiroawase.specialchars.view.viewpage.DuanyuliebiaoFragment

class DuanyuPagerAdapter(
    fragment: Fragment,
    private val itemCount: Int
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = itemCount

    @Suppress("BooleanLiteralArgument")
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> DuanyuliebiaoFragment.newInstance(
            App.getStringFromRaw(R.raw.duanyu_duanyu_data)
        )

        1 -> DuanyuliebiaoFragment.newInstance(
            App.getStringFromRaw(R.raw.duanyu_tuan_data),
            true,
            false
        )

        2 -> DuanyuliebiaoFragment.newInstance(
            App.getStringFromRaw(R.raw.duanyu_fenge_data),
            true
        )

        3 -> DuanyuliebiaoFragment.newInstance(
            App.getStringFromRaw(R.raw.duanyu_fuhao_data)
        )

        else -> DuanyuliebiaoFragment.newInstance(
            App.getStringFromRaw(R.raw.duanyu_yanzi_data)
        )
    }
}
