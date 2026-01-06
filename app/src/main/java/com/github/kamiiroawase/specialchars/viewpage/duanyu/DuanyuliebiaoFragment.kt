package com.github.kamiiroawase.specialchars.viewpage.duanyu

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.activity.MainActivity
import com.github.kamiiroawase.specialchars.databinding.FragmentDuanyuliebiaoBinding
import com.github.kamiiroawase.specialchars.databinding.FragmentDuanyuliebiaoVerticalBinding
import com.github.kamiiroawase.specialchars.viewpage.duanyu.fragment.DuanyuneirongFragment

class DuanyuliebiaoFragment : Fragment() {
    private var _binding: ViewBinding? = null
    private val binding get() = _binding!!

    private var data: List<Map<String, Any>>? = null

    private var listEnable: Boolean = true
    private var verticalEnable: Boolean = false

    private val listTextWraps = mutableMapOf<Int, FrameLayout>()

    private var listTextWrapPosition = 0
    private var listTextWrapPositionKey = "LIST_TEXT_WRAP_POSITION"

    private val fragments by lazy {
        data!!.map {
            @Suppress("UNCHECKED_CAST")
            DuanyuneirongFragment.newInstance(
                it["textList"] as List<String>,
                (it["spanCount"] as Number).toInt(),
                (it["spanType"] as Number).toInt()
            )
        }
    }

    companion object {
        fun newInstance(
            data: String,
            verticalEnable: Boolean = false,
            listEnable: Boolean = true
        ): DuanyuliebiaoFragment {
            return DuanyuliebiaoFragment().apply {
                arguments = Bundle().apply {
                    putBoolean("listEnable", listEnable)
                    putBoolean("verticalEnable", verticalEnable)
                    putString("data", data)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(listTextWrapPositionKey, listTextWrapPosition)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            listTextWrapPosition =
                savedInstanceState.getInt(listTextWrapPositionKey, listTextWrapPosition)
        }

        requireArguments().let { bundle ->
            listEnable = bundle.getBoolean("listEnable", listEnable)
            verticalEnable = bundle.getBoolean("verticalEnable", verticalEnable)

            bundle.getString("data").let { json ->
                data = try {
                    Gson().fromJson(json, object : TypeToken<List<Map<String, Any>>>() {}.type)
                } catch (_: Exception) {
                    data
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = if (verticalEnable) {
            FragmentDuanyuliebiaoVerticalBinding.inflate(inflater, container, false)
        } else {
            FragmentDuanyuliebiaoBinding.inflate(inflater, container, false)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()

        MainActivity.initializedFragmentCount++
    }

    private fun setUpViews() {
        val listRecyclerView = if (verticalEnable) {
            (binding as FragmentDuanyuliebiaoVerticalBinding).listRecyclerView
        } else {
            (binding as FragmentDuanyuliebiaoBinding).listRecyclerView
        }

        val listRecyclerViewWrap = if (verticalEnable) {
            (binding as FragmentDuanyuliebiaoVerticalBinding).listRecyclerViewWrap
        } else {
            (binding as FragmentDuanyuliebiaoBinding).listRecyclerViewWrap
        }

        if (!listEnable) {
            val layoutParams = listRecyclerViewWrap.layoutParams

            if (verticalEnable) {
                layoutParams.height = 0
            } else {
                layoutParams.width = 0
            }

            listRecyclerViewWrap.layoutParams = layoutParams
        }

        data!!.forEachIndexed { position, item ->
            val view = if (verticalEnable) {
                LayoutInflater.from(context)
                    .inflate(R.layout.item_duanyuliebiao_list_vertical, listRecyclerView, false)
            } else {
                LayoutInflater.from(context)
                    .inflate(R.layout.item_duanyuliebiao_list, listRecyclerView, false)
            }

            val listText: TextView = view.findViewById(R.id.listText)
            val listTextWrap: FrameLayout = view.findViewById(R.id.listTextWrap)

            listTextWraps.put(position, listTextWrap)

            listText.text = item["listName"] as String

            if (position == listTextWrapPosition) {
                listTextWraps[listTextWrapPosition]?.setBackgroundColor(Color.WHITE)
                switchFragment(position)
            }

            listTextWrap.setOnClickListener {
                val savedScroll = if (verticalEnable) {
                    listRecyclerViewWrap.scrollX
                } else {
                    listRecyclerViewWrap.scrollY
                }

                listTextWraps[listTextWrapPosition]?.setBackgroundColor(Color.TRANSPARENT)
                listTextWrapPosition = position
                listTextWraps[listTextWrapPosition]?.setBackgroundColor(Color.WHITE)

                switchFragment(position)

                if (verticalEnable) {
                    listRecyclerViewWrap.postDelayed({
                        listRecyclerViewWrap.scrollX = savedScroll
                    }, 100L)
                } else {
                    listRecyclerViewWrap.postDelayed({
                        listRecyclerViewWrap.scrollY = savedScroll
                    }, 100L)
                }
            }

            if (!listEnable) {
                listText.text = ""
                listText.textSize = 0f
                listTextWrap.setPadding(0, 0, 0, 0)

                val layoutParams = listText.layoutParams

                if (verticalEnable) {
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
        transaction.replace(R.id.fragmentContainer, fragments[position])
        transaction.commitAllowingStateLoss()
    }
}
