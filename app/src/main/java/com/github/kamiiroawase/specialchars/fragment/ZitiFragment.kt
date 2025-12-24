package com.github.kamiiroawase.specialchars.fragment

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.FragmentZitiBinding
import com.github.kamiiroawase.specialchars.viewpage.ziti.DianzhuiListFragment
import com.github.kamiiroawase.specialchars.viewpage.ziti.ZhuangshiListFragment
import com.github.kamiiroawase.specialchars.viewpage.ziti.ZitiliebiaoFragment

class ZitiFragment : BaseFragment() {
    private var _binding: FragmentZitiBinding? = null
    private val binding get() = _binding!!

    companion object {
        @SuppressLint("StaticFieldLeak")
        var fontListAdapter: FontListAdapter<FontItem2>? = null

        @SuppressLint("StaticFieldLeak")
        var chineseListAdapter: FontListAdapter<FontItem>? = null

        @SuppressLint("StaticFieldLeak")
        var decorationListAdapter: FontListAdapter<FontItem>? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentZitiBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        decorationListAdapter = null
        chineseListAdapter = null
        fontListAdapter = null
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewpage2AndTabLayout()

        setUpClickListener()

        setStatusBarWrap(binding.statusBarWrap)
    }

    private fun setUpClickListener() {
        binding.editTextZitiSubmit.setOnClickListener {
            editTextZitiChanged(binding.editTextZiti.text.toString())
        }
    }

    private fun setUpViewpage2AndTabLayout() {
        val tabs = listOf(
            getString(R.string.zhuangshi),
            getString(R.string.dianzhui)
        )

        val pagerAdapter = DecoratePagerAdapter(this, tabs)

        binding.viewPager2.adapter = pagerAdapter
        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.offscreenPageLimit = tabs.size

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    fun editTextZitiChanged(text: String) {
        if (TextUtils.isEmpty(text)) {
            binding.customFont0.setTextColor("#EE869B".toColorInt())
            binding.customFont1.setTextColor("#F8CDD5".toColorInt())

            fontListAdapter?.submitData(
                ZitiliebiaoFragment.getFontList(ZitiliebiaoFragment.EMPTY_TEXT)
            )
            chineseListAdapter?.submitData(
                DianzhuiListFragment.getFontList(DianzhuiListFragment.EMPTY_TEXT)
            )
            decorationListAdapter?.submitData(
                ZhuangshiListFragment.getFontList(ZhuangshiListFragment.EMPTY_TEXT)
            )
        } else {
            binding.customFont0.setTextColor("#F8CDD5".toColorInt())
            binding.customFont1.setTextColor("#EE869B".toColorInt())

            fontListAdapter?.submitData(
                ZitiliebiaoFragment.getFontList(text)
            )
            chineseListAdapter?.submitData(
                DianzhuiListFragment.getFontList(text)
            )
            decorationListAdapter?.submitData(
                ZhuangshiListFragment.getFontList(text)
            )
        }
    }

    abstract class BaseFontItem

    data class FontItem(
        val text: String
    ) : BaseFontItem()

    data class FontItem2(
        val text: String,
        val typeface: Typeface,
        val fontName: String
    ) : BaseFontItem()

    class DecoratePagerAdapter(
        fragment: Fragment,
        private val tabs: List<String>
    ) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = tabs.size
        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> ZhuangshiListFragment()
            else -> DianzhuiListFragment()
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fontListText: TextView = itemView.findViewById(R.id.fontListText)
        val fontListTextWrap: LinearLayout = itemView.findViewById(R.id.fontListTextWrap)
    }

    class FontListAdapter<T : BaseFontItem>(private var dataList: List<T>) :
        RecyclerView.Adapter<MyViewHolder>() {
        private var _context: Context? = null
        private val context get() = _context!!

        @SuppressLint("NotifyDataSetChanged")
        fun submitData(newData: List<T>) {
            dataList = newData
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            _context = parent.context

            val view = LayoutInflater.from(context)
                .inflate(R.layout.item_zitiliebiao_text, parent, false)

            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val item = dataList[position]

            when (item) {
                is FontItem -> {
                    holder.fontListText.text = item.text
                }

                is FontItem2 -> {
                    holder.fontListText.text = item.text
                    holder.fontListText.typeface = item.typeface
                }
            }

            holder.fontListTextWrap.setOnClickListener {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


                when (item) {
                    is FontItem -> {
                        clipboard.setPrimaryClip(ClipData.newPlainText("fontItem", item.text))
                    }

                    is FontItem2 -> {
                        clipboard.setPrimaryClip(
                            ClipData.newHtmlText(
                                "fontItem", item.text, """
                            <span style="font-family:'${item.fontName}';">
                                ${item.text.replace("\n", "<br/>")}
                            </span>
                        """.trimIndent()
                            )
                        )
                    }
                }

                Toast.makeText(
                    context,
                    context.getString(R.string.fuzhichenggong),
                    Toast.LENGTH_SHORT
                ).show()

                holder.fontListText.setTextColor("#EE869B".toColorInt())
                holder.fontListText.postDelayed({
                    holder.fontListText.setTextColor("#000000".toColorInt())
                }, 500L)
            }
        }

        override fun getItemCount(): Int = dataList.size
    }
}
