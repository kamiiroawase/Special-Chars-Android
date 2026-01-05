package com.github.kamiiroawase.specialchars.activity

import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.ActivityFeedbackBinding

class FeedbackActivity : BaseActivity() {
    private lateinit var binding: ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        setupEditTextChangedListener()
        setupClickListener()
    }

    private fun setupEditTextChangedListener() {
        binding.feedbackEditText.doAfterTextChanged { text ->
            val count = text?.length ?: 0
            binding.feedbackCountText.text = count.toString()
        }
    }

    private fun setupClickListener() {
        binding.submitButton.setOnClickListener {
            if (binding.contactEditText.text.toString().trim().isEmpty()) {
                showToast(R.string.fankuiyijian3)
                return@setOnClickListener
            }

            if (binding.feedbackEditText.text.toString().trim().isEmpty()) {
                showToast(R.string.fankuiyijian3)
                return@setOnClickListener
            }

            binding.feedbackCountText.text = "0"
            binding.feedbackEditText.setText("")

            showToast(R.string.tijiaochenggong)

            finish()
        }
    }

    private fun showToast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}
