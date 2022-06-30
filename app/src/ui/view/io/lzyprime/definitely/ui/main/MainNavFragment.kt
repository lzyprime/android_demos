package io.lzyprime.definitely.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitely.R
import io.lzyprime.definitely.databinding.MainNavFragmentBinding
import io.lzyprime.definitely.ui.utils.viewBinding

@AndroidEntryPoint
class MainNavFragment : Fragment(R.layout.main_nav_fragment) {
    private val binding by viewBinding<MainNavFragmentBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}