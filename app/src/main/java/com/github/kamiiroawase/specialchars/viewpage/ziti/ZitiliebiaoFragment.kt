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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class ZitiliebiaoFragment : Fragment() {
    private var _binding: FragmentZitiliebiaoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ZitiFragmentViewModel by viewModels({ requireParentFragment() })

    private lateinit var fontListAdapter: ZitiFragment.FontListAdapter<ZitiFragment.Font.Item2>

    private lateinit var fontMetaList: List<FontMeta>

    companion object {
        private const val DEFAULT_SAMPLE_TEXT: String = "Best wishes to you"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentZitiliebiaoBinding.inflate(inflater, container, false)

        fontListAdapter = ZitiFragment.FontListAdapter(buildFontItemList(DEFAULT_SAMPLE_TEXT))

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.fontList.adapter = null

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()

        initView()

        observeViewModel()
    }

    private fun initView() {
        binding.fontList.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            isNestedScrollingEnabled = false
            adapter = fontListAdapter
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.zitiEditTextValue.collectLatest { inputText ->
                    if (inputText.isEmpty()) {
                        fontListAdapter.submitData(buildFontItemList(DEFAULT_SAMPLE_TEXT))
                    } else {
                        fontListAdapter.submitData(buildFontItemList(inputText))
                    }
                }
            }
        }
    }

    private fun initData() {
        fontMetaList = listOf(
            FontMeta(Typeface.SANS_SERIF, "sans-serif"),
            FontMeta(Typeface.MONOSPACE, "monospace"),
            FontMeta(Typeface.SERIF, "sans-serif")
        )
    }

    fun buildFontItemList(text: String): List<ZitiFragment.Font.Item2> {
        return fontMetaList.map {
            ZitiFragment.Font.Item2(
                text,
                it.a,
                it.b
            )
        }
    }

    data class FontMeta(
        val a: Typeface,
        val b: String
    )
}
