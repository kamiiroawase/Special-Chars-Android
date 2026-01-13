package com.github.kamiiroawase.specialchars.view.activity

import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.github.kamiiroawase.specialchars.R
import com.github.kamiiroawase.specialchars.databinding.ActivityFeedbackBinding

class FeedbackActivity : BaseActivity() {
    private lateinit var binding: ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val contactEditText = binding.contactEditText
            val feedbackEditText = binding.feedbackEditText
            val feedbackCountText = binding.feedbackCountText

            if (contactEditText.text.toString().isBlank()) {
                showToast(R.string.fankuiyijian3)
                return@setOnClickListener
            }

            if (feedbackEditText.text.toString().isBlank()) {
                showToast(R.string.fankuiyijian3)
                return@setOnClickListener
            }

            feedbackCountText.text = 0.toString()
            feedbackEditText.setText("")

            showToast(R.string.tijiaochenggong)

            finish()
        }
    }
}
