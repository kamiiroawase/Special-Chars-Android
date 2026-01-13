package com.github.kamiiroawase.specialchars.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.kamiiroawase.specialchars.App
import com.google.android.material.tabs.TabLayoutMediator
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.FragmentDuanyuBinding
import com.github.kamiiroawase.specialchars.view.viewpage.DuanyuliebiaoFragment

class DuanyuFragment : BaseFragment() {
    private var _binding: FragmentDuanyuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDuanyuBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        val tabs = listOf(
            R.string.duanyu,
            R.string.tuan,
            R.string.fenge,
            R.string.fuhao,
            R.string.yanzi
        )

        val pagerAdapter = DecoratePagerAdapter(this, tabs.size)

        binding.viewPager2.adapter = pagerAdapter
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.offscreenPageLimit = tabs.size - 1

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = getString(tabs[position])
        }.attach()
    }

    class DecoratePagerAdapter(
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
}
