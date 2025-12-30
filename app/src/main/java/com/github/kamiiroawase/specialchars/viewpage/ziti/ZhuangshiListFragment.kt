package com.github.kamiiroawase.specialchars.viewpage.ziti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kamiiroawase.specialchars.databinding.FragmentZitiliebiaoBinding
import com.github.kamiiroawase.specialchars.fragment.ZitiFragment

class ZhuangshiListFragment : Fragment() {
    private var _binding: FragmentZitiliebiaoBinding? = null
    private val binding get() = _binding!!

    private val adapter = ZitiFragment.FontListAdapter(getFontList(EMPTY_TEXT))

    companion object {
        const val EMPTY_TEXT: String = "Sakura"

        fun getFontList(text: String): List<ZitiFragment.Font.Item1> {
            return listOf(
                ZitiFragment.Font.Item1(customBuildString(text, "꧁", "꧂")),
                ZitiFragment.Font.Item1(customBuildString(text, "", "✅已认证\uD83D\uDCAF")),
                ZitiFragment.Font.Item1(customBuildString(text, "‹v^v^v^• ", " •^v^v^v›")),
                ZitiFragment.Font.Item1(customBuildString(text, "(っ◔◡◔)っ ♥", "♥")),
                ZitiFragment.Font.Item1(customBuildString(text, "༺།༼࿄࿆", "࿅࿆༽།༻")),
                ZitiFragment.Font.Item1(customBuildString(text, "©º°¨¨°º© ", " ©º°¨¨°º©")),
                ZitiFragment.Font.Item1(customBuildString(text, "*•.¸♡", "♡¸.•*")),
                ZitiFragment.Font.Item1(customBuildString(text, "࿐ཉི༗࿆", "༗࿆ཉི࿐")),
                ZitiFragment.Font.Item1(customBuildString(text, "|I{•------»", "«------•}I|")),
                ZitiFragment.Font.Item1(customBuildString(text, "╰☆☆ ", " ☆☆╮")),
                ZitiFragment.Font.Item1(customBuildString(text, "༺", "༻")),
                ZitiFragment.Font.Item1(customBuildString(text, "_/¯/__/¯/_ ", " _/¯/__/¯/_")),
                ZitiFragment.Font.Item1(customBuildString(text, "⇇⇇⇇ ", " ⇉⇉⇉")),
                ZitiFragment.Font.Item1(
                    customBuildString(
                        text,
                        "\uD83C\uDF6B ⋆ \uD83C\uDF4E  \uD83C\uDF80",
                        "\uD83C\uDF80  \uD83C\uDF4E ⋆ \uD83C\uDF6B"
                    )
                ),
                ZitiFragment.Font.Item1(
                    customBuildString(
                        text,
                        "••.•´¯`•.••   \uD83C\uDF80",
                        "\uD83C\uDF80   ••.•`¯´•.••"
                    )
                )
            )
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

        setUpFontList()
    }

    private fun setUpFontList() {
        binding.fontList.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.fontList.isNestedScrollingEnabled = false
        binding.fontList.adapter = adapter

        ZitiFragment.getInstance()?.decorationListAdapter = adapter
    }
}