package io.lzyprime.bottomnavigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import io.lzyprime.bottomnavigation.databinding.MainNavFragmentBinding

class MainNavFragment : Fragment(R.layout.main_nav_fragment) {
    private val binding by viewBinding<MainNavFragmentBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController =
            (childFragmentManager.findFragmentById(R.id.homePageNavHost) as NavHostFragment).navController
        binding.mainBtmNavView.setupWithNavController(navController)
    }
}