package com.github.kamiiroawase.specialchars.view.viewpage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.FragmentDuanyuliebiaoBinding
import com.github.kamiiroawase.specialchars.databinding.FragmentDuanyuliebiaoVerticalBinding
import com.github.kamiiroawase.specialchars.view.activity.MainActivity
import com.github.kamiiroawase.specialchars.view.fragment.DuanyuneirongFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DuanyuliebiaoFragment : Fragment() {
    private var _binding: FragmentDuanyuliebiaoBinding? = null
    private var _bindingVertical: FragmentDuanyuliebiaoVerticalBinding? = null

    private var isVertical: Boolean = false
    private var isListVisible: Boolean = true

    private var currentSelectedIndex = 0
    private val listItemViewMap = mutableMapOf<Int, FrameLayout>()

    private val sectionList: List<DuanyuSection> by lazy {
        val json = requireArguments().getString(KEY_SECTION_DATA_LIST)
            ?: return@lazy emptyList()

        runCatching {
            Gson().fromJson<List<DuanyuSection>>(
                json,
                object : TypeToken<List<DuanyuSection>>() {}.type
            )
        }.getOrElse { emptyList() }
    }

    private val contentFragments by lazy {
        sectionList.map {
            DuanyuneirongFragment.newInstance(
                it.textList,
                it.spanCount,
                it.spanType
            )
        }
    }

    companion object {
        private const val KEY_IS_VERTICAL = "KEY_IS_VERTICAL"
        private const val KEY_IS_LIST_VISIBLE = "KEY_IS_LIST_VISIBLE"
        private const val KEY_SECTION_DATA_LIST = "KEY_SECTION_DATA_LIST"
        private const val KEY_CURRENT_SELECTED_INDEX = "KEY_CURRENT_SELECTED_INDEX"

        fun newInstance(
            sectionList: String,
            isVertical: Boolean = false,
            isListVisible: Boolean = true
        ): DuanyuliebiaoFragment {
            return DuanyuliebiaoFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(KEY_IS_VERTICAL, isVertical)
                    putBoolean(KEY_IS_LIST_VISIBLE, isListVisible)
                    putString(KEY_SECTION_DATA_LIST, sectionList)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(KEY_CURRENT_SELECTED_INDEX, currentSelectedIndex)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            currentSelectedIndex =
                savedInstanceState.getInt(KEY_CURRENT_SELECTED_INDEX, currentSelectedIndex)
        }

        requireArguments().let { bundle ->
            isListVisible = bundle.getBoolean(KEY_IS_LIST_VISIBLE, isListVisible)
            isVertical = bundle.getBoolean(KEY_IS_VERTICAL, isVertical)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return if (isVertical) {
            _bindingVertical =
                FragmentDuanyuliebiaoVerticalBinding.inflate(inflater, container, false)
            _bindingVertical!!.root
        } else {
            _binding =
                FragmentDuanyuliebiaoBinding.inflate(inflater, container, false)
            _binding!!.root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _bindingVertical = null
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        MainActivity.Companion.initializedFragmentCount++
    }

    private fun initView() {
        val listRecyclerView = _binding?.listRecyclerView
            ?: _bindingVertical!!.listRecyclerView

        val listRecyclerViewWrap = _binding?.listRecyclerViewWrap
            ?: _bindingVertical!!.listRecyclerViewWrap

        if (!isListVisible) {
            val layoutParams = listRecyclerViewWrap.layoutParams

            if (isVertical) {
                layoutParams.height = 0
            } else {
                layoutParams.width = 0
            }

            listRecyclerViewWrap.layoutParams = layoutParams
        }

        sectionList.forEachIndexed { position, item ->
            val view = if (isVertical) {
                LayoutInflater.from(context)
                    .inflate(R.layout.item_duanyuliebiao_list_vertical, listRecyclerView, false)
            } else {
                LayoutInflater.from(context)
                    .inflate(R.layout.item_duanyuliebiao_list, listRecyclerView, false)
            }

            val listText: TextView = view.findViewById(R.id.listText)
            val listTextWrap: FrameLayout = view.findViewById(R.id.listTextWrap)

            listItemViewMap.put(position, listTextWrap)

            listText.text = item.listName

            if (position == currentSelectedIndex) {
                listItemViewMap[currentSelectedIndex]?.setBackgroundColor(Color.WHITE)
                switchFragment(position)
            }

            listTextWrap.setOnClickListener {
                val savedScroll = if (isVertical) {
                    listRecyclerViewWrap.scrollX
                } else {
                    listRecyclerViewWrap.scrollY
                }

                listItemViewMap[currentSelectedIndex]?.setBackgroundColor(Color.TRANSPARENT)
                currentSelectedIndex = position
                listItemViewMap[currentSelectedIndex]?.setBackgroundColor(Color.WHITE)

                switchFragment(position)

                listRecyclerViewWrap.postDelayed({
                    if (isVertical) {
                        listRecyclerViewWrap.scrollX = savedScroll
                    } else {
                        listRecyclerViewWrap.scrollY = savedScroll
                    }
                }, 100L)
            }

            if (!isListVisible) {
                listText.text = ""
                listText.textSize = 0f
                listTextWrap.setPadding(0, 0, 0, 0)

                val layoutParams = listText.layoutParams

                if (isVertical) {
                    layoutParams.height = 0
                } else {
                    layoutParams.width = 0
                }

                listText.layoutParams = layoutParams
            }

            listRecyclerView.addView(view)
        }
    }

    fun switchFragment(position: Int) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, contentFragments[position])
        transaction.commit()
    }

    data class DuanyuSection(
        val listName: String,
        val textList: List<String>,
        val spanCount: Int,
        val spanType: Int
    )
}
