package com.github.kamiiroawase.specialchars.viewpage.ziti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kamiiroawase.specialchars.App
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.FragmentZitiliebiaoBinding
import com.github.kamiiroawase.specialchars.fragment.ZitiFragment
import com.github.kamiiroawase.specialchars.viewmodel.ZitiFragmentViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import kotlin.getValue

class ZhuangshiListFragment : Fragment() {
    private var _binding: FragmentZitiliebiaoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ZitiFragmentViewModel by viewModels({ requireParentFragment() })

    private val adapter = ZitiFragment.FontListAdapter(getFontList(EMPTY_TEXT))

    companion object {
        const val EMPTY_TEXT: String = "Sakura"

        fun getFontList(text: String): List<ZitiFragment.Font.Item1> {
            val data = try {
                Gson().fromJson(
                    App.getStringFromRaw(R.raw.ziti_zhuangshi_data),
                    object : TypeToken<List<Map<String, String>>>() {}.type
                )
            } catch (_: Exception) {
                listOf<Map<String, String>>()
            }

            return data.map {
                ZitiFragment.Font.Item1(
                    customBuildString(
                        text,
                        it["a"] as String,
                        it["b"] as String
                    )
                )
            }
        }

        private fun customBuildString(text: String, append1: String, append2: String): String {
            return append1 + text + append2
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentZitiliebiaoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        observeEvents()
    }

    private fun initView() {
        binding.fontList.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.fontList.isNestedScrollingEnabled = false
        binding.fontList.adapter = adapter
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.zitiEditTextValue.collect { value ->
                    if (value.isEmpty()) {
                        adapter.submitData(getFontList(EMPTY_TEXT))
                    } else {
                        val a = getFontList(value)
                        adapter.submitData(a)
                    }
                }
            }
        }
    }
}
