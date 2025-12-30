package com.github.kamiiroawase.specialchars.viewpage.ziti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kamiiroawase.specialchars.databinding.FragmentZitiliebiaoBinding
import com.github.kamiiroawase.specialchars.fragment.ZitiFragment

class DianzhuiListFragment : Fragment() {
    private var _binding: FragmentZitiliebiaoBinding? = null
    private val binding get() = _binding!!

    private val adapter = ZitiFragment.FontListAdapter(getFontList(EMPTY_TEXT))

    companion object {
        const val EMPTY_TEXT: String = "一期一会"

        fun getFontList(text: String): List<ZitiFragment.Font.Item1> {
            return listOf(
                ZitiFragment.Font.Item1(customBuildString(text, "", "̚")),
                ZitiFragment.Font.Item1(customBuildString(text, "", "҈")),
                ZitiFragment.Font.Item1(customBuildString(text, "", "҉")),
                ZitiFragment.Font.Item1(customBuildString(text, "៚", "ོོ ͜✿ ҉҉҉҉҉")),
                ZitiFragment.Font.Item1(customBuildString(text, "ζั͡ ", "ζั͡✾")),
                ZitiFragment.Font.Item1(customBuildString(text, "ζั͡ ", "ζั͡✿")),
                ZitiFragment.Font.Item1(customBuildString(text, "ζั͡ ", "ζั͡❀")),
                ZitiFragment.Font.Item1(customBuildString(text, "", "ஊ")),
                ZitiFragment.Font.Item1(customBuildString(text, "", "෴"))
            )
        }

        private fun customBuildString(text: String, append1: String, append2: String): String {
            return buildString {
                for ((_, char) in text.withIndex()) {
                    append(append1)
                    append(char)
                    append(append2)
                }
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

        setUpViews()
    }

    private fun setUpViews() {
        binding.fontList.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.fontList.isNestedScrollingEnabled = false
        binding.fontList.adapter = adapter

        ZitiFragment.getInstance()?.chineseListAdapter = adapter
    }
}