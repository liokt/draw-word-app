package com.example.lio.drawwordapp.ui.setup.drawing

import androidx.lifecycle.ViewModel
import com.example.lio.drawwordapp.R
import com.example.lio.drawwordapp.data.remote.ws.DrawingApi
import com.example.lio.drawwordapp.util.DispatcherProvider
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DrawingVIewModel @Inject constructor(
    private val drawingApi: DrawingApi,
    private val dispatchers: DispatcherProvider,
    private val gson: Gson
): ViewModel() {

    private val _selectColorButtonId = MutableStateFlow(R.id.rbBlack)
    val selectedColorButtonId: StateFlow<Int> = _selectColorButtonId

    fun checkRadioButton(id: Int) {
        _selectColorButtonId.value = id
    }
}