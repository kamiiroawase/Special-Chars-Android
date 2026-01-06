package com.github.kamiiroawase.specialchars.viewpage.ziti

import android.graphics.Typeface
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
import com.github.kamiiroawase.specialchars.databinding.FragmentZitiliebiaoBinding
import com.github.kamiiroawase.specialchars.fragment.ZitiFragment
import com.github.kamiiroawase.specialchars.viewmodel.ZitiFragmentViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class ZitiliebiaoFragment : Fragment() {
    private var _binding: FragmentZitiliebiaoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ZitiFragmentViewModel by viewModels({ requireParentFragment() })

    private val adapter = ZitiFragment.FontListAdapter(getFontList(EMPTY_TEXT))

    companion object {
        const val EMPTY_TEXT: String = "Best wishes to you"

        fun getFontList(text: String): List<ZitiFragment.Font.Item2> {
            val data = listOf(
                mapOf("a" to Typeface.SANS_SERIF, "b" to "sans-serif"),
                mapOf("a" to Typeface.MONOSPACE, "b" to "monospace"),
                mapOf("a" to Typeface.SERIF, "b" to "sans-serif")
            )

            return data.map {
                ZitiFragment.Font.Item2(
                    text,
                    it["a"] as Typeface,
                    it["b"] as String
                )
            }
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
                        adapter.submitData(getFontList(value))
                    }
                }
            }
        }
    }
}
