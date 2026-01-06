package com.github.kamiiroawase.specialchars.fragment

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.FragmentZitiBinding
import com.github.kamiiroawase.specialchars.viewmodel.ZitiFragmentViewModel
import com.github.kamiiroawase.specialchars.viewpage.ziti.DianzhuiListFragment
import com.github.kamiiroawase.specialchars.viewpage.ziti.ZhuangshiListFragment
import kotlinx.coroutines.launch
import kotlin.getValue

class ZitiFragment : BaseFragment() {
    private var _binding: FragmentZitiBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ZitiFragmentViewModel by viewModels()

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

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        setupClickListener()

        observeEvents()
    }

    private fun initView() {
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

    private fun setupClickListener() {
        binding.editTextZitiSubmit.setOnClickListener {
            viewModel.updateZitiEditTextValue(binding.editTextZiti.text.toString())
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.zitiEditTextValue.collect { value ->
                    if (value.isEmpty()) {
                        binding.customFont0.setTextColor("#EE869B".toColorInt())
                        binding.customFont1.setTextColor("#F8CDD5".toColorInt())
                    } else {
                        binding.customFont0.setTextColor("#F8CDD5".toColorInt())
                        binding.customFont1.setTextColor("#EE869B".toColorInt())
                    }
                }
            }
        }
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

    class DecoratePagerAdapter(
        fragment: ZitiFragment,
        private val tabs: List<String>
    ) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = tabs.size
        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> ZhuangshiListFragment()
            else -> DianzhuiListFragment()
        }
    }

    class FontItem1ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fontListText: TextView = itemView.findViewById(R.id.fontListText)
        val fontListTextWrap: LinearLayout = itemView.findViewById(R.id.fontListTextWrap)
    }

    class FontItem2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fontListText: TextView = itemView.findViewById(R.id.fontListText)
        val fontListTextWrap: LinearLayout = itemView.findViewById(R.id.fontListTextWrap)
    }

    class FontListAdapter<T : Font>(private var dataList: List<T>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var _context: Context? = null
        private val context get() = _context!!

        companion object {
            private const val VIEW_TYPE_FONT_ITEM_1 = 1
            private const val VIEW_TYPE_FONT_ITEM_2 = 2
        }

        @SuppressLint("NotifyDataSetChanged")
        fun submitData(newData: List<T>) {
            dataList = newData
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            _context = parent.context

            val view = LayoutInflater.from(context)
                .inflate(R.layout.item_zitiliebiao_text, parent, false)

            return when (viewType) {
                VIEW_TYPE_FONT_ITEM_1 -> FontItem1ViewHolder(view)
                VIEW_TYPE_FONT_ITEM_2 -> FontItem2ViewHolder(view)
                else -> throw IllegalArgumentException("")
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val clipDoneFn = fun(fontListText: TextView) {
                Toast.makeText(
                    context,
                    context.getString(R.string.fuzhichenggong),
                    Toast.LENGTH_SHORT
                ).show()

                fontListText.setTextColor("#EE869B".toColorInt())
                fontListText.postDelayed({
                    fontListText.setTextColor("#000000".toColorInt())
                }, 500L)
            }

            when (holder) {
                is FontItem1ViewHolder -> {
                    val item = dataList[position] as Font.Item1

                    holder.fontListText.text = item.text

                    holder.fontListTextWrap.setOnClickListener {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                        clipboard.setPrimaryClip(ClipData.newPlainText("fontItem", item.text))

                        clipDoneFn(holder.fontListText)
                    }
                }

                is FontItem2ViewHolder -> {
                    val item = dataList[position] as Font.Item2

                    holder.fontListText.text = item.text
                    holder.fontListText.typeface = item.typeface

                    holder.fontListTextWrap.setOnClickListener {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                        clipboard.setPrimaryClip(
                            ClipData.newHtmlText(
                                "fontItem", item.text, """
                            <span style="font-family:'${item.fontName}';">
                                ${item.text.replace("\n", "<br/>")}
                            </span>
                        """.trimIndent()
                            )
                        )

                        clipDoneFn(holder.fontListText)
                    }
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when (dataList[position]) {
                is Font.Item1 -> VIEW_TYPE_FONT_ITEM_1
                is Font.Item2 -> VIEW_TYPE_FONT_ITEM_2
            }
        }

        override fun getItemCount(): Int = dataList.size
    }
}
