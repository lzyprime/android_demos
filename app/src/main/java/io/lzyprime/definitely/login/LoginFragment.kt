package io.lzyprime.definitely.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.core.model.bean.IMLoginState
import io.lzyprime.core.utils.GenerateTestUserSig
import io.lzyprime.core.viewmodel.UserViewModel
import io.lzyprime.definitely.R
import io.lzyprime.definitely.databinding.LoginFragmentBinding
import io.lzyprime.definitely.utils.hideSoftKeyboard
import io.lzyprime.definitely.utils.snackBar
import io.lzyprime.definitely.utils.viewBinding
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {
    private val binding by viewBinding<LoginFragmentBinding>()
    private val model by activityViewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            binding.editUserId.setText(model.userId.first())
        }
        binding.model = Model(model.loginState) {
            binding.editUserId.hideSoftKeyboard()
            val userId = binding.editUserId.text.toString()
            val sig = GenerateTestUserSig.genTestUserSig(userId)
            if (sig.isEmpty()) {
                binding.root.snackBar(R.string.generate_sig_failed).show()
            } else {
                model.login(userId, sig)
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        model.loginState.filterIsInstance<IMLoginState.Login>().onEach {
            findNavController().navigate(R.id.mainNavFragment, null, navOptions {
                popUpTo(R.id.login_graph) {
                    inclusive = true
                }
                launchSingleTop = true
            })
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    class Model(val loginState: StateFlow<IMLoginState>, val onClickLogin: View.OnClickListener)
}