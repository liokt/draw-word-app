package com.example.lio.drawwordapp.data.remote.ws.models

import com.example.lio.drawwordapp.util.Constants.TYPE_GAME_STATE

data class GameState(
    val drawingPlayer: String,
    val word: String
): BaseModel(TYPE_GAME_STATE)

