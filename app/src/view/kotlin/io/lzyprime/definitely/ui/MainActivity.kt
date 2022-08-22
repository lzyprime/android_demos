package io.lzyprime.definitely.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitely.R
import io.lzyprime.definitely.databinding.MainActivityBinding
import io.lzyprime.definitely.ui.utils.viewBinding
import io.lzyprime.definitely.ui.utils.snackBar
import io.lzyprime.definitely.viewmodel.UserViewModel
import io.lzyprime.svr.model.LoginState
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding<MainActivityBinding>()
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment).navController
    }

    private val snackBar by lazy {
        binding.root.snackBar(R.string.re_enter_press_again_to_exit)
    }

    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Definitely)
        DynamicColors.applyToActivityIfAvailable(this)

        setContentView(binding.root)

        onBackPressedDispatcher.addCallback {
            if (snackBar.isShown) finish() else snackBar.show()
        }

        lifecycleScope.launchWhenCreated {
            userViewModel.loginState.filter { it != LoginState.Loading }.collectLatest {
                val cur = navController.currentDestination?.id ?: 0
                fun singleNavOption() = navOptions {
                    launchSingleTop = true
                    popUpTo(cur) {
                        inclusive = true
                    }
                    restoreState = true
                }

                if (it !is LoginState.LoginUserInfo) {
                    if (cur != R.id.loginFragment) {
                        navController.navigate(R.id.loginFragment, null, singleNavOption())
                    }
                } else if (it.needComplete) {
                    if (cur != R.id.updateUserInfoFragment) {
                        navController.navigate(R.id.updateUserInfoFragment, null, singleNavOption())
                    }
                } else if (cur == R.id.loginFragment || cur == R.id.updateUserInfoFragment) {
                    navController.navigate(R.id.mainNavFragment, null, singleNavOption())
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            userViewModel.snackBarMsg.collectLatest {
                binding.root.snackBar(it).show()
            }
        }
    }
}