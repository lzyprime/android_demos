package io.lzyprime.bottomnavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import io.lzyprime.bottomnavigation.databinding.ActivitySecondaryBinding

class SecondaryActivity : AppCompatActivity() {

    private val fragments = arrayOf(Item1Fragment(), Item2Fragment(), Item3Fragment())
    private val itemIds = arrayOf(R.id.item1, R.id.item2, R.id.item3)
    private lateinit var binding: ActivitySecondaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondaryBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.secondaryVP.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }
        // 页面预加载
        binding.secondaryVP.offscreenPageLimit = fragments.size

        // 若不想滑动切换页面时设置
        //secondaryVP.isUserInputEnabled = false
        binding.secondaryVP.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.secondaryBtmNavView.selectedItemId = itemIds[position]
            }
        })

        binding.secondaryBtmNavView.setOnNavigationItemSelectedListener {
            binding.secondaryVP.currentItem = itemIds.indexOf(it.itemId)
            true
        }
    }
}