package com.example.lio.drawwordapp.data.remote.ws.models

import com.example.lio.drawwordapp.util.Constants.TYPE_CUR_ROUND_DRAW_INFO

data class RoundDrawInfo(
    val data: List<String>
): BaseModel(TYPE_CUR_ROUND_DRAW_INFO)
