package io.lzyprime.definitely.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.definitely.R
import io.lzyprime.definitely.databinding.MainNavFragmentBinding
import io.lzyprime.definitely.utils.viewBinding

@AndroidEntryPoint
class MainNavFragment : Fragment(R.layout.main_nav_fragment) {
    private val binding by viewBinding<MainNavFragmentBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments = listOf({ PhotoLibraryFragment() }, { MeFragment() })
        val menuItemIds = listOf(R.id.photoList, R.id.me)

        binding.mainViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]()
        }

        binding.mainViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.mainBottomNav.selectedItemId = menuItemIds[position]
            }
        })

        binding.mainBottomNav.setOnItemSelectedListener {
            binding.mainViewPager.currentItem = menuItemIds.indexOf(it.itemId)
            true
        }
    }
}