package com.github.kamiiroawase.specialchars.viewpage.duanyu.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.FragmentDuanyuneirongBinding

class DuanyuneirongFragment : Fragment() {
    private var _binding: FragmentDuanyuneirongBinding? = null
    private val binding get() = _binding!!

    private var spanType: Int = 1
    private var spanCount: Int = 1

    private val textList: List<String> by lazy {
        val json = requireArguments().getString(KEY_TEXT_LIST)
            ?: return@lazy emptyList()

        runCatching {
            Gson().fromJson<List<String>>(
                json,
                object : TypeToken<List<String>>() {}.type
            )
        }.getOrElse { emptyList() }
    }

    companion object {
        private const val KEY_SPAN_TYPE = "KEY_SPAN_TYPE"
        private const val KEY_SPAN_COUNT = "KEY_SPAN_COUNT"
        private const val KEY_TEXT_LIST = "KEY_TEXT_LIST"

        fun newInstance(
            textList: List<String>,
            spanCount: Int,
            spanType: Int
        ): DuanyuneirongFragment {
            return DuanyuneirongFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TEXT_LIST, Gson().toJson(textList))
                    putInt(KEY_SPAN_COUNT, spanCount)
                    putInt(KEY_SPAN_TYPE, spanType)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let { bundle ->
            spanType = bundle.getInt(KEY_SPAN_TYPE, spanType)
            spanCount = bundle.getInt(KEY_SPAN_COUNT, spanCount)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDuanyuneirongBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.contentRecyclerView.apply {
            layoutManager = if ((spanType == 2 || spanType == 3) && spanCount > 1) {
                StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
            } else {
                GridLayoutManager(requireActivity(), spanCount)
            }

            isNestedScrollingEnabled = false
            adapter = ContentRecyclerViewAdapter(textList, spanType)
        }
    }

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentText: TextView = itemView.findViewById(R.id.contentText)
        val contentTextWrap: LinearLayout = itemView.findViewById(R.id.contentTextWrap)
    }

    class ContentRecyclerViewAdapter(
        private var textList: List<String>,
        private val spanType: Int
    ) :
        RecyclerView.Adapter<ContentViewHolder>() {
        override fun getItemCount(): Int = textList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_duanyuliebiao_text, parent, false)

            return ContentViewHolder(view)
        }

        override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
            val text = textList[position]

            holder.contentText.text = text

            when (spanType) {
                4 -> {
                    holder.contentText.gravity = Gravity.START
                }

                2, 3 -> {
                    holder.contentText.apply {
                        gravity = Gravity.START
                        maxLines = text.count { it == '\n' } +
                                if (spanType == 3) 0 else 1
                        setAutoSizeTextTypeUniformWithConfiguration(
                            1,
                            16,
                            1,
                            TypedValue.COMPLEX_UNIT_SP
                        )
                    }
                }

                else -> {
                    holder.contentText.apply {
                        maxLines = 1
                        gravity = Gravity.CENTER
                        setAutoSizeTextTypeUniformWithConfiguration(
                            1,
                            16,
                            1,
                            TypedValue.COMPLEX_UNIT_SP
                        )
                    }
                }
            }

            holder.contentTextWrap.setOnClickListener {
                getClipboard(it).setPrimaryClip(ClipData.newPlainText("fontItem", text))

                showToast(it, R.string.fuzhichenggong)

                holder.contentText.apply {
                    setTextColor("#EE869B".toColorInt())
                    postDelayed({
                        setTextColor("#000000".toColorInt())
                    }, 500L)
                }
            }
        }

        private fun getClipboard(view: View): ClipboardManager {
            return view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        }

        private fun showToast(view: View, resId: Int) {
            Toast.makeText(view.context, resId, Toast.LENGTH_SHORT).show()
        }
    }
}
