package io.lzyprime.definitely.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitely.R
import io.lzyprime.definitely.databinding.LoginFragmentBinding
import io.lzyprime.definitely.ui.utils.viewBinding
import io.lzyprime.definitely.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {
    private val binding by viewBinding<LoginFragmentBinding>()
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.usernameTextField.editText?.setText(loginViewModel.loginUiState.value.username)
        binding.passwordTextField.editText?.setText(loginViewModel.loginUiState.value.password)

        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.loginUiState.map { it.enableLoginBtn }
                .flowWithLifecycle(lifecycle)
                .collectLatest {
                    binding.button.isEnabled = it
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.loginUiState.map { !it.isLoading }
                .flowWithLifecycle(lifecycle)
                .collectLatest {
                    binding.usernameTextField.isEnabled = it
                    binding.passwordTextField.isEnabled = it
                }
        }

        binding.usernameTextField.editText?.doOnTextChanged { text, _, _, _ ->
            loginViewModel.editUsername(text)
        }
        binding.passwordTextField.editText?.doOnTextChanged { text, _, _, _ ->
            loginViewModel.editPassword(text)
        }
        binding.button.setOnClickListener {
            loginViewModel.login()
        }
    }
}