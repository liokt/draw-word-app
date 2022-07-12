package com.example.lio.drawwordapp.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.datastore by preferencesDataStore("settings")