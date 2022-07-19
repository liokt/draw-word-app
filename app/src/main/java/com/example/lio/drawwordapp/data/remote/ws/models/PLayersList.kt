package com.example.lio.drawwordapp.data.remote.ws.models

import com.example.lio.drawwordapp.util.Constants.TYPE_PLAYERS_LIST

data class PLayersList(
    val players: List<PlayerData>
): BaseModel(TYPE_PLAYERS_LIST)
