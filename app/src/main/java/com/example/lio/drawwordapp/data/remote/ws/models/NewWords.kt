package com.example.lio.drawwordapp.data.remote.ws.models

import com.example.lio.drawwordapp.util.Constants.TYPE_NEW_WORDS

data class NewWords(
    val newWords: List<String>
): BaseModel(TYPE_NEW_WORDS)
