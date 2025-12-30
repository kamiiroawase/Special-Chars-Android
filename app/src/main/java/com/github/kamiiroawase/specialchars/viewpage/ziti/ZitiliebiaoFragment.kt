package com.github.kamiiroawase.specialchars.viewpage.ziti

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kamiiroawase.specialchars.databinding.FragmentZitiliebiaoBinding
import com.github.kamiiroawase.specialchars.fragment.ZitiFragment

class ZitiliebiaoFragment : Fragment() {
    private var _binding: FragmentZitiliebiaoBinding? = null
    private val binding get() = _binding!!

    private val adapter = ZitiFragment.FontListAdapter(getFontList(EMPTY_TEXT))

    companion object {
        const val EMPTY_TEXT: String = "Best wishes to you"

        fun getFontList(text: String): List<ZitiFragment.Font.Item2> {
            return listOf(
                ZitiFragment.Font.Item2(text, Typeface.SANS_SERIF, "sans-serif"),
                ZitiFragment.Font.Item2(text, Typeface.MONOSPACE, "monospace"),
                ZitiFragment.Font.Item2(text, Typeface.SERIF, "serif")
            )
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

        setUpFontList()
    }

    private fun setUpFontList() {
        binding.fontList.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.fontList.isNestedScrollingEnabled = false
        binding.fontList.adapter = adapter

        ZitiFragment.getInstance()?.fontListAdapter = adapter
    }
}