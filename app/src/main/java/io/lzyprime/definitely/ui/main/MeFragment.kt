package io.lzyprime.definitely.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import io.lzyprime.definitely.R
import io.lzyprime.definitely.databinding.MeFragmentBinding
import io.lzyprime.definitely.ui.UserViewModel
import io.lzyprime.definitely.utils.viewBinding
import kotlinx.coroutines.flow.collect

class MeFragment:Fragment(R.layout.me_fragment) {
    private val binding by viewBinding<MeFragmentBinding>()
    private val userViewModel by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            userViewModel.unsplashUser.collect {
                binding.unsplashUser = it
            }
        }
        binding.userInfo.setOnClickListener {

        }
    }
}