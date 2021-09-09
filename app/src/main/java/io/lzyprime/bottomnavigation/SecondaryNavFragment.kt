package io.lzyprime.bottomnavigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import io.lzyprime.bottomnavigation.databinding.SecondaryNavFragmentBinding

class SecondaryNavFragment : Fragment(R.layout.secondary_nav_fragment) {
    private val binding by viewBinding<SecondaryNavFragmentBinding>()

    private val fragments = arrayOf(Item3Fragment(), Item2Fragment(), Item1Fragment())
    private val itemIds = arrayOf(R.id.item3, R.id.item2, R.id.item1)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.secondaryViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }

        binding.secondaryViewPager.offscreenPageLimit = fragments.size

        //binding.secondaryViewPager.isUserInputEnabled = false // 禁用滑动

        binding.secondaryViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.secondaryBtmNavView.selectedItemId = itemIds[position]
            }
        })

        binding.secondaryBtmNavView.setOnItemSelectedListener {
            binding.secondaryViewPager.currentItem = itemIds.indexOf(it.itemId)
            true
        }
    }

}