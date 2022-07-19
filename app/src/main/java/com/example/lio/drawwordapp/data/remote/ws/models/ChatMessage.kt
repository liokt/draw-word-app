package com.example.lio.drawwordapp.data.remote.ws.models

import com.example.lio.drawwordapp.util.Constants.TYPE_CHAT_MESSAGE

data class ChatMessage(
    val from: String,
    val roomName: String,
    val message: String,
    val timeStamp: Long
) : BaseModel(TYPE_CHAT_MESSAGE)
