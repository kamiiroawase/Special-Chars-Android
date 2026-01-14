package com.github.kamiiroawase.specialchars.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.FragmentDuanyuBinding
import com.github.kamiiroawase.specialchars.view.adapter.DuanyuPagerAdapter

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

        val pagerAdapter = DuanyuPagerAdapter(this, tabs.size)

        binding.viewPager2.adapter = pagerAdapter
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.offscreenPageLimit = tabs.size - 1

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = getString(tabs[position])
        }.attach()
    }
}
