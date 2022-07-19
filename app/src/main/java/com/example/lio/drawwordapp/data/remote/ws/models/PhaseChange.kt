package com.example.lio.drawwordapp.data.remote.ws.models

import com.example.lio.drawwordapp.data.remote.ws.Room
import com.example.lio.drawwordapp.util.Constants.TYPE_PHASE_CHANGE

data class PhaseChange(
    var phase: Room.Phase?,
    var time: Long,
    val drawingPlayer: String? = null
): BaseModel(TYPE_PHASE_CHANGE)
