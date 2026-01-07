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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DianzhuiListFragment : Fragment() {
    private var _binding: FragmentZitiliebiaoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ZitiFragmentViewModel by viewModels({ requireParentFragment() })

    private lateinit var fontListAdapter: ZitiFragment.FontListAdapter<ZitiFragment.Font.Item1>

    private val fontMetaList: List<FontMeta> by lazy {
        runCatching {
            Gson().fromJson<List<FontMeta>>(
                App.getStringFromRaw(R.raw.ziti_dianzhui_data),
                object : TypeToken<List<FontMeta>>() {}.type
            )
        }.getOrElse { emptyList() }
    }

    companion object {
        private const val DEFAULT_SAMPLE_TEXT: String = "一期一会"
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

    fun buildFontItemList(text: String): List<ZitiFragment.Font.Item1> {
        return fontMetaList.map {
            ZitiFragment.Font.Item1(
                buildFontItemText(
                    text,
                    it.a,
                    it.b
                )
            )
        }
    }

    private fun buildFontItemText(text: String, append1: String, append2: String): String {
        return buildString {
            for (char in text) {
                append(append1)
                append(char)
                append(append2)
            }
        }
    }

    data class FontMeta(
        val a: String,
        val b: String
    )
}
