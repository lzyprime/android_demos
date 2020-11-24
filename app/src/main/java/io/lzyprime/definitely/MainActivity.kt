package io.lzyprime.definitely

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitely.databinding.MainActivityBinding
import io.lzyprime.definitely.utils.viewBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding<MainActivityBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Definitely)
        setContentView(binding.root)
    }
}