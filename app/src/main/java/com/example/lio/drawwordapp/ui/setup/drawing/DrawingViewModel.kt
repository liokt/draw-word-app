package com.example.lio.drawwordapp.ui.setup.drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lio.drawwordapp.R
import com.example.lio.drawwordapp.data.remote.ws.DrawingApi
import com.example.lio.drawwordapp.data.remote.ws.models.*
import com.example.lio.drawwordapp.data.remote.ws.models.DrawAction.Companion.ACTION_UNDO
import com.example.lio.drawwordapp.util.DispatcherProvider
import com.google.gson.Gson
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val drawingApi: DrawingApi,
    private val dispatchers: DispatcherProvider,
    private val gson: Gson
): ViewModel() {

    sealed class SocketEvent {
        data class ChatMessageEvent(val data: ChatMessage): SocketEvent()
        data class AnnouncementEvent(val data: Announcement): SocketEvent()
        data class ChosenWordEvent(val data: ChosenWord): SocketEvent()
        data class GameStateEvent(val data: GameState): SocketEvent()
        data class DrawDataEvent(val data: DrawData): SocketEvent()
        data class NewWordsEvent(val data: NewWords): SocketEvent()
        data class GameErrorEvent(val data: GameError): SocketEvent()
        data class RoundDrawInfoEvent(val data: RoundDrawInfo): SocketEvent()
        object UndoEvent: SocketEvent()
    }

    private val _newWords = MutableStateFlow(NewWords(listOf()))
    val newWords: StateFlow<NewWords> = _newWords

    private val _chat = MutableStateFlow<List<BaseModel>>(listOf())
    val chat: StateFlow<List<BaseModel>> = _chat

    private val _selectColorButtonId = MutableStateFlow(R.id.rbBlack)
    val selectedColorButtonId: StateFlow<Int> = _selectColorButtonId

    private val _connectionProgressBarVisible = MutableStateFlow(true)
    val connectionProgressBarVisible: StateFlow<Boolean> = _connectionProgressBarVisible

    private val _chooseWordsOverlayVisible = MutableStateFlow(false)
    val chooseWordsOverlayVisible: StateFlow<Boolean> = _chooseWordsOverlayVisible

    //Channel where we sent connections events messages
    private val connectionEventChannel = Channel<WebSocket.Event>()
    val connectionEvent = connectionEventChannel.receiveAsFlow().flowOn(dispatchers.io)

    private val socketsEventChannel = Channel<SocketEvent>()
    val socketEvent = socketsEventChannel.receiveAsFlow().flowOn(dispatchers.io)

    init {
        observeBaseModels()
        observeEvents()
    }

    fun setChooseOverlayVisibility(isVisible: Boolean) {
        _chooseWordsOverlayVisible.value = isVisible
    }

    fun setConnectionProgressBarVisibility(isVisible: Boolean) {
        _connectionProgressBarVisible.value = isVisible
    }

    fun checkRadioButton(id: Int) {
        _selectColorButtonId.value = id
    }

    private fun observeEvents() {
        viewModelScope.launch(dispatchers.io) {
            drawingApi.observeEvents().collect { event ->
                connectionEventChannel.send(event)
            }
        }
    }

    private fun observeBaseModels() {
        viewModelScope.launch(dispatchers.io) {
            drawingApi.observeBaseModels().collect { data ->
                when(data) {
                    is DrawData -> {
                        socketsEventChannel.send(SocketEvent.DrawDataEvent(data))
                    }
                    is ChatMessage -> {
                        socketsEventChannel.send(SocketEvent.ChatMessageEvent(data))
                    }
                    is Announcement -> {
                        socketsEventChannel.send(SocketEvent.AnnouncementEvent(data))
                    }
                    is ChosenWord -> {
                        socketsEventChannel.send(SocketEvent.ChosenWordEvent(data))
                    }
                    is NewWords -> {
                        _newWords.value = data
                        socketsEventChannel.send(SocketEvent.NewWordsEvent(data))
                    }
                    is DrawAction -> {
                        when(data.action) {
                            ACTION_UNDO -> socketsEventChannel.send((SocketEvent.UndoEvent))
                        }
                    }
                    is GameError -> socketsEventChannel.send(SocketEvent.GameErrorEvent(data))
                    is Ping -> sendBaseModel(Ping())
                }
            }
        }
    }

    fun chooseWord(word: String, roomName: String) {
        val chosenWord = ChosenWord(word, roomName)
        sendBaseModel(chosenWord)
    }


    fun sendChatMessage(message: ChatMessage) {
        if (message.message.trim().isEmpty()) {
            return
        }
        viewModelScope.launch(dispatchers.io) {
            drawingApi.sendBaseModel(message)
        }
    }

    fun sendBaseModel(data: BaseModel) {
        viewModelScope.launch(dispatchers.io) {
            drawingApi.sendBaseModel(data)
        }
    }
}