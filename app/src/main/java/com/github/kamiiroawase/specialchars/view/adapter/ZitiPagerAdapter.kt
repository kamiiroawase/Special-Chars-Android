package com.github.kamiiroawase.specialchars.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.kamiiroawase.specialchars.view.fragment.ZitiFragment
import com.github.kamiiroawase.specialchars.view.viewpage.DianzhuiListFragment
import com.github.kamiiroawase.specialchars.view.viewpage.ZhuangshiListFragment

class ZitiPagerAdapter(
    fragment: ZitiFragment,
    private val tabs: List<String>
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = tabs.size
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ZhuangshiListFragment()
        else -> DianzhuiListFragment()
    }
}
