package io.lzyprime.definitely.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import io.lzyprime.core.data.models.LoginState
import io.lzyprime.definitely.R
import io.lzyprime.definitely.databinding.MeFragmentBinding
import io.lzyprime.core.viewmodels.UserViewModel
import io.lzyprime.definitely.utils.viewBinding

class MeFragment : Fragment(R.layout.me_fragment) {
    private val binding by viewBinding<MeFragmentBinding>()
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.model = userViewModel
        binding.userInfo.setOnClickListener {
            when(userViewModel.loginState.value) {
                is LoginState.NotLogin -> findNavController().navigate(R.id.login_graph)
                else -> findNavController().navigate(R.id.login_graph)
            }
        }
    }
}