package io.lzyprime.definitely

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.core.viewmodels.UserViewModel
import io.lzyprime.definitely.databinding.MainActivityBinding
import io.lzyprime.definitely.utils.viewBinding
import io.lzyprime.definitely.utils.snackBar

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

        userViewModel.autoLogin()

        setTheme(R.style.Theme_Definitely)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this) {
            if (!onSupportNavigateUp()) {
                if (snackBar.isShown) finish() else snackBar.show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}