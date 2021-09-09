package io.lzyprime.bottomnavigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import io.lzyprime.bottomnavigation.databinding.Item1FragmentBinding


class Item1Fragment : Fragment(R.layout.item1_fragment) {
    private val binding: Item1FragmentBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val globalNavHostController = requireActivity().findNavController(R.id.mainNavHost)
        binding.item1Btn.visibility =
            if (globalNavHostController.currentDestination?.id == R.id.secondaryNavFragment) View.GONE else View.VISIBLE
        binding.item1Btn.setOnClickListener {
            globalNavHostController.navigate(R.id.action_mainNavFragment_to_secondaryNavFragment)
        }
    }
}