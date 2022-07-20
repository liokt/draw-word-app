package com.example.lio.drawwordapp.ui.setup.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.lio.drawwordapp.R
import com.example.lio.drawwordapp.databinding.FragmentUsernameBinding
import com.example.lio.drawwordapp.ui.setup.UsernameViewModel
import com.example.lio.drawwordapp.util.Constants.MAX_USERNAME_LENGTH
import com.example.lio.drawwordapp.util.Constants.MIN_USERNAME_LENGTH
import com.example.lio.drawwordapp.util.hideKeyboard
import com.example.lio.drawwordapp.util.navigateSafely
import com.example.lio.drawwordapp.util.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UserNameFragment: Fragment(R.layout.fragment_username) {

    private var _binding: FragmentUsernameBinding? = null
    private val binding: FragmentUsernameBinding
        get() = _binding!!

    private val viewModel: UsernameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUsernameBinding.bind(view)
        listenToEvents()

        binding.btnNext.setOnClickListener {
            viewModel.validateUserNameAndNavigateToSelectRoom(
                binding.etUsername.text.toString()
            )
            requireActivity().hideKeyboard(binding.root)
        }
    }

    private fun listenToEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.setupEvent.collect { event ->
                when(event) {
                    is UsernameViewModel.SetupEvent.NavigateToSelectRoomEvent -> {
                        findNavController().navigateSafely(
                          R.id.action_userNameFragment_to_selectRoomFragment,
                            args = Bundle().apply { putString("username", event.username) }
                        )
                    }
                    is UsernameViewModel.SetupEvent.InputEmptyError -> {
                        snackbar(R.string.error_field_empty)
                    }
                    is UsernameViewModel.SetupEvent.InputTooShortError -> {
                        snackbar(getString(R.string.error_username_too_short, MIN_USERNAME_LENGTH))
                    }
                    is UsernameViewModel.SetupEvent.InputTooLongError -> {
                        snackbar(getString(R.string.error_username_too_long, MAX_USERNAME_LENGTH))
                    }
                    else -> Unit
                }
            }
        }
}

    

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}