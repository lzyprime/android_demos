package io.lzyprime.definitely.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.core.data.models.LoginState
import io.lzyprime.definitely.R
import io.lzyprime.definitely.databinding.LoginFragmentBinding
import io.lzyprime.core.viewmodels.UserViewModel
import io.lzyprime.definitely.utils.viewBinding
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {
    private val binding by viewBinding<LoginFragmentBinding>()
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            userViewModel.loginState.first { it == LoginState.Logged }
            findNavController().navigateUp()
        }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.loginState = userViewModel.loginState
        binding.loginBtn.setOnClickListener {
            userViewModel.login(binding.userCodeEditText.text.toString())
        }
    }
}