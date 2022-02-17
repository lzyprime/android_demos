package io.lzyprime.definitely

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitely.databinding.MainActivityBinding
import io.lzyprime.definitely.utils.viewBinding
import io.lzyprime.definitely.utils.snackBar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding<MainActivityBinding>()

    private val snackBar by lazy {
        binding.root.snackBar(R.string.re_enter_press_again_to_exit)
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
    }
}