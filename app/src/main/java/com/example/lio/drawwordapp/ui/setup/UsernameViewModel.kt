package com.example.lio.drawwordapp.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lio.drawwordapp.data.remote.ws.Room
import com.example.lio.drawwordapp.repository.SetupRepository
import com.example.lio.drawwordapp.util.Constants.MAX_ROOM_NAME_LENGTH
import com.example.lio.drawwordapp.util.Constants.MAX_USERNAME_LENGTH
import com.example.lio.drawwordapp.util.Constants.MIN_ROOM_NAME_LENGTH
import com.example.lio.drawwordapp.util.Constants.MIN_USERNAME_LENGTH
import com.example.lio.drawwordapp.util.DispatcherProvider
import com.example.lio.drawwordapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsernameViewModel @Inject constructor(
    private val repository: SetupRepository,
    private val dispatchers:  DispatcherProvider
): ViewModel() {

    sealed class SetupEvent {

        object InputEmptyError: SetupEvent()
        object InputTooShortError: SetupEvent()
        object InputTooLongError: SetupEvent()

        data class NavigateToSelectRoomEvent(val username: String): SetupEvent()

    }

    private val _setupEvent = MutableSharedFlow<SetupEvent>()
    val setupEvent: SharedFlow<SetupEvent> = _setupEvent

    fun validateUserNameAndNavigateToSelectRoom(username: String){
        viewModelScope.launch (dispatchers.main) {
            val trimmedUserName = username.trim()
            when {
                trimmedUserName.isEmpty() -> {
                    _setupEvent.emit(SetupEvent.InputEmptyError)
                }
                trimmedUserName.length < MIN_USERNAME_LENGTH -> {
                    _setupEvent.emit(SetupEvent.InputTooShortError)
                }
                trimmedUserName.length > MAX_USERNAME_LENGTH -> {
                    _setupEvent.emit(SetupEvent.InputTooLongError)
                }
                else -> _setupEvent.emit(SetupEvent.NavigateToSelectRoomEvent(username))
            }
        }
    }


}