package com.github.kamiiroawase.specialchars.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ZitiFragmentViewModel : ViewModel() {
    private val _zitiEditTextValue = MutableSharedFlow<String>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val zitiEditTextValue = _zitiEditTextValue.asSharedFlow()

    fun updateZitiEditTextValue(text: String) {
        viewModelScope.launch {
            _zitiEditTextValue.emit(text)
        }
    }
}
