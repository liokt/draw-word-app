package com.example.lio.drawwordapp.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun Activity.hideKeyboard(root: View) {
    val windowToken = root.windowToken
    val imn = getSystemService((AppCompatActivity.INPUT_METHOD_SERVICE)) as InputMethodManager
    windowToken?.let {
        imn.hideSoftInputFromWindow(it, 0)
    } ?: kotlin.run {
        try {
            val keyboardHeight = InputMethodManager::class.java
                .getMethod("getInputMethodWindowVisibleHeight")
                .invoke(imn) as Int
            if(keyboardHeight > 0) {
                imn.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}