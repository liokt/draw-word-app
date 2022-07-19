package com.example.lio.drawwordapp.data.remote.ws.models

import com.example.lio.drawwordapp.data.remote.ws.models.BaseModel
import com.example.lio.drawwordapp.util.Constants.TYPE_GAME_ERROR

data class GameError(
    val errorType: Int
): BaseModel(TYPE_GAME_ERROR) {
    companion object {
        const val ERROR_ROOM_NOT_FOUND = 0
    }
}
