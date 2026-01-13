package com.github.kamiiroawase.specialchars.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.FragmentZitiBinding
import com.github.kamiiroawase.specialchars.viewmodel.ZitiFragmentViewModel
import com.github.kamiiroawase.specialchars.view.adapter.ZitiPagerAdapter
import kotlinx.coroutines.launch
import kotlin.getValue

class ZitiFragment : BaseFragment() {
    private var _binding: FragmentZitiBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ZitiFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentZitiBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        setupClickListener()

        observeViewModel()
    }

    private fun initView() {
        val tabs = listOf(
            getString(R.string.zhuangshi),
            getString(R.string.dianzhui)
        )

        val pagerAdapter = ZitiPagerAdapter(this, tabs)

        binding.viewPager2.adapter = pagerAdapter
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.offscreenPageLimit = tabs.size

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    private fun setupClickListener() {
        binding.editTextZitiSubmit.setOnClickListener {
            viewModel.updateZitiEditTextValue(binding.editTextZiti.text.toString())
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.zitiEditTextValue.collect { value ->
                    if (value.isEmpty()) {
                        binding.customFont0.setTextColor("#EE869B".toColorInt())
                        binding.customFont1.setTextColor("#F8CDD5".toColorInt())
                    } else {
                        binding.customFont0.setTextColor("#F8CDD5".toColorInt())
                        binding.customFont1.setTextColor("#EE869B".toColorInt())
                    }
                }
            }
        }
    }
}
