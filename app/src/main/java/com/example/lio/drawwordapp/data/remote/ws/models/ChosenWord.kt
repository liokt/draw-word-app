package com.example.lio.drawwordapp.data.remote.ws.models

import com.example.lio.drawwordapp.data.remote.ws.models.BaseModel
import com.example.lio.drawwordapp.util.Constants.TYPE_CHOSEN_WORD

data class ChosenWord(
    val chosenWord: String,
    val roomName: String
): BaseModel(TYPE_CHOSEN_WORD)
