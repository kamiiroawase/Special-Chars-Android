package com.github.kamiiroawase.specialchars.view.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.github.kamiiroawase.specialchars.R

class ZitiliebiaoAdapter<T : ZitiliebiaoAdapter.Font>(private var dataList: List<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_FONT_ITEM_1 = 1
        private const val VIEW_TYPE_FONT_ITEM_2 = 2
    }

    override fun getItemCount(): Int = dataList.size

    override fun getItemViewType(position: Int): Int {
        return when (dataList[position]) {
            is Font.Item1 -> VIEW_TYPE_FONT_ITEM_1
            is Font.Item2 -> VIEW_TYPE_FONT_ITEM_2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_zitiliebiao_text, parent, false)

        return when (viewType) {
            VIEW_TYPE_FONT_ITEM_1 -> FontItem1ViewHolder(view)
            VIEW_TYPE_FONT_ITEM_2 -> FontItem2ViewHolder(view)
            else -> throw IllegalArgumentException("")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FontItem1ViewHolder -> {
                val item = dataList[position] as Font.Item1

                holder.fontListText.text = item.text

                holder.fontListTextWrap.setOnClickListener {
                    getClipboard(it).setPrimaryClip(
                        ClipData.newPlainText(
                            "fontItem",
                            item.text
                        )
                    )

                    clipCopyDone(holder.fontListText)
                }
            }

            is FontItem2ViewHolder -> {
                val item = dataList[position] as Font.Item2

                holder.fontListText.text = item.text
                holder.fontListText.typeface = item.typeface

                holder.fontListTextWrap.setOnClickListener {
                    getClipboard(it).setPrimaryClip(
                        ClipData.newHtmlText(
                            "fontItem", item.text, """
                            <span style="font-family:'${item.fontName}';">
                                ${item.text.replace("\n", "<br/>")}
                            </span>
                        """.trimIndent()
                        )
                    )

                    clipCopyDone(holder.fontListText)
                }
            }
        }
    }

    fun submitData(newData: List<T>) {
        dataList = newData
        notifyItemRangeChanged(0, dataList.size)
    }

    private fun clipCopyDone(view: TextView) {
        showToast(view, R.string.fuzhichenggong)

        view.apply {
            setTextColor("#EE869B".toColorInt())
            postDelayed({
                setTextColor("#000000".toColorInt())
            }, 500L)
        }
    }

    private fun getClipboard(view: View): ClipboardManager {
        return view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    private fun showToast(view: View, resId: Int) {
        Toast.makeText(view.context, resId, Toast.LENGTH_SHORT).show()
    }

    sealed class Font {
        data class Item1(
            val text: String
        ) : Font()

        data class Item2(
            val text: String,
            val typeface: Typeface,
            val fontName: String
        ) : Font()
    }

    class FontItem1ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fontListText: TextView = itemView.findViewById(R.id.fontListText)
        val fontListTextWrap: LinearLayout = itemView.findViewById(R.id.fontListTextWrap)
    }

    class FontItem2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fontListText: TextView = itemView.findViewById(R.id.fontListText)
        val fontListTextWrap: LinearLayout = itemView.findViewById(R.id.fontListTextWrap)
    }
}
