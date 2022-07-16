package com.example.lio.drawwordapp.ui.setup.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.lio.drawwordapp.R
import com.example.lio.drawwordapp.databinding.FragmentSelectRoomBinding

class SelectRoomFragment: Fragment(R.layout.fragment_select_room) {

    private var _binding: FragmentSelectRoomBinding? = null
    private val binding: FragmentSelectRoomBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSelectRoomBinding.bind(view)
    }

    

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}