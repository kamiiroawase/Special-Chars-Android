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

    private var textList: List<String>? = null

    companion object {
        fun newInstance(
            textList: List<String>,
            spanCount: Int,
            spanType: Int
        ): DuanyuneirongFragment {
            return DuanyuneirongFragment().apply {
                arguments = Bundle().apply {
                    putString("textList", Gson().toJson(textList))
                    putInt("spanCount", spanCount)
                    putInt("spanType", spanType)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let { bundle ->
            spanType = bundle.getInt("spanType", spanType)
            spanCount = bundle.getInt("spanCount", spanCount)

            bundle.getString("textList").let { json ->
                textList = try {
                    Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
                } catch (_: Exception) {
                    null
                }
            }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()
    }

    private fun setUpViews() {
        if ((spanType == 2 || spanType == 3) && spanCount > 1) {
            binding.contentRecyclerView.layoutManager =
                StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        } else {
            binding.contentRecyclerView.layoutManager =
                GridLayoutManager(requireActivity(), spanCount)
        }

        binding.contentRecyclerView.isNestedScrollingEnabled = false
        binding.contentRecyclerView.adapter = ContentRecyclerViewAdapter(textList!!, spanType)
    }

    class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentText: TextView = itemView.findViewById(R.id.contentText)
        val contentTextWrap: LinearLayout = itemView.findViewById(R.id.contentTextWrap)
    }

    class ContentRecyclerViewAdapter(
        private var dataList: List<String>,
        private val spanType: Int
    ) :
        RecyclerView.Adapter<ContentViewHolder>() {
        private var _context: Context? = null
        private val context get() = _context!!

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
            _context = parent.context

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_duanyuliebiao_text, parent, false)

            return ContentViewHolder(view)
        }

        override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
            val text = dataList[position]

            holder.contentText.text = text

            when (spanType) {
                4 -> {
                    holder.contentText.gravity = Gravity.START
                }

                2, 3 -> {
                    holder.contentText.gravity = Gravity.START
                    holder.contentText.maxLines = text.count { it == '\n' } +
                            if (spanType == 3) 0 else 1
                    holder.contentText.setAutoSizeTextTypeUniformWithConfiguration(
                        1,
                        16,
                        1,
                        TypedValue.COMPLEX_UNIT_SP
                    )
                }

                else -> {
                    holder.contentText.maxLines = 1
                    holder.contentText.gravity = Gravity.CENTER
                    holder.contentText.setAutoSizeTextTypeUniformWithConfiguration(
                        1,
                        16,
                        1,
                        TypedValue.COMPLEX_UNIT_SP
                    )
                }
            }

            holder.contentTextWrap.setOnClickListener {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                clipboard.setPrimaryClip(ClipData.newPlainText("fontItem", text))

                Toast.makeText(
                    context,
                    context.getString(R.string.fuzhichenggong),
                    Toast.LENGTH_SHORT
                ).show()

                holder.contentText.setTextColor("#EE869B".toColorInt())
                holder.contentText.postDelayed({
                    holder.contentText.setTextColor("#000000".toColorInt())
                }, 500L)
            }
        }

        override fun getItemCount(): Int = dataList.size
    }
}