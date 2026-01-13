package com.github.kamiiroawase.specialchars.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class ZitiFragmentViewModel : ViewModel() {
    private val _zitiEditTextValue = MutableStateFlow("")

    val zitiEditTextValue = _zitiEditTextValue.asSharedFlow()

    fun updateZitiEditTextValue(text: String) {
        _zitiEditTextValue.value = text
    }
}
