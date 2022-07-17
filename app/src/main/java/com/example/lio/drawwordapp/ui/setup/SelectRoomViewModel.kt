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
class SelectRoomViewModel @Inject constructor(
    private val repository: SetupRepository,
    private val dispatchers:  DispatcherProvider
): ViewModel() {

    sealed class SetupEvent {

        object InputEmptyError: SetupEvent()
        object InputTooShortError: SetupEvent()
        object InputTooLongError: SetupEvent()

        data class GetRoomEvent(val rooms: List<Room>): SetupEvent()
        data class GetRoomErrorEvent(val error: String): SetupEvent()
        object GetRoomLoadingEvent: SetupEvent()
        object GetRoomEmptyEvent: SetupEvent()

        data class JoinRoomEvent(val roomName: String): SetupEvent()
        data class JoinRoomErrorEvent(val error: String): SetupEvent()

    }

    private val _setupEvent = MutableSharedFlow<SetupEvent>()
    val setupEvent: SharedFlow<SetupEvent> = _setupEvent

    private val _rooms = MutableStateFlow<SetupEvent>(SetupEvent.GetRoomEmptyEvent)
    val rooms: StateFlow<SetupEvent> = _rooms

    fun getRooms(searchQuery: String) {
        _rooms.value = SetupEvent.GetRoomLoadingEvent
        viewModelScope.launch (dispatchers.main) {
            val result = repository.getRooms(searchQuery)
            if(result is Resource.Success) {
                //We set the rooms value each time for configuration changes
                _rooms.value = (SetupEvent.GetRoomEvent(result.data ?: return@launch))
            } else {
                //We want to set the rooms value one time only (we use "emit")
                _setupEvent.emit(SetupEvent.GetRoomErrorEvent(result.message ?: return@launch))
            }
        }
    }

    fun joinRoom(username: String, roomName: String) {
        viewModelScope.launch (dispatchers.main) {
            val result = repository.joinRoom(username, roomName)
            if(result is Resource.Success) {
                _setupEvent.emit(SetupEvent.JoinRoomEvent(roomName))
            } else {
                _setupEvent.emit(SetupEvent.JoinRoomErrorEvent(result.message ?: return@launch))
            }
        }
    }
}