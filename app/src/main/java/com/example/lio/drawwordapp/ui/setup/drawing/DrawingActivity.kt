package com.example.lio.drawwordapp.ui.setup.drawing

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lio.drawwordapp.databinding.ActivityDrawingBinding

class DrawingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawingBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}