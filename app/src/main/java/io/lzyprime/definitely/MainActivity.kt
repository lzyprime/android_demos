package io.lzyprime.definitely

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
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

        binding.mainVP.adapter = object : FragmentStateAdapter(this) {
            private val fragments = listOf(SwipeListFragment())
            override fun getItemCount(): Int = fragments.size

            override fun createFragment(position: Int): Fragment = fragments[position]
        }
        binding.mainVP.isUserInputEnabled = false
        val titles = listOf(
            "滑动删除",
        )
        TabLayoutMediator(binding.tabLayout, binding.mainVP) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}