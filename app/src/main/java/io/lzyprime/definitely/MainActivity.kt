package io.lzyprime.definitely

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.core.model.bean.IMLoginState
import io.lzyprime.core.viewmodel.UserViewModel
import io.lzyprime.definitely.databinding.MainActivityBinding
import io.lzyprime.definitely.utils.viewBinding
import io.lzyprime.definitely.utils.snackBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding<MainActivityBinding>()
    private val userModel by viewModels<UserViewModel>()

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment).navController
    }

    private val snackBar by lazy {
        binding.root.snackBar(R.string.re_enter_toast_press_again_to_exit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Definitely)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this) {
            if (!onSupportNavigateUp()) {
                if (snackBar.isShown) finish() else snackBar.show()
            }
        }

        userModel.autoLogin().invokeOnCompletion {
            userModel.loginState.onEach { state ->
                if (state is IMLoginState.Logout) {
                    navController.navigate(R.id.login_graph, null, navOptions {
                        popUpTo(R.id.mainNavFragment){
                            inclusive = true
                        }
                        launchSingleTop = true
                    })
                }
            }.launchIn(lifecycleScope)
        }
    }
}