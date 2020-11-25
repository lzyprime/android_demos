package io.lzyprime.bottomnavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_secondary.*

class SecondaryActivity : AppCompatActivity() {

    private val fragments = arrayOf(Item1Fragment(), Item2Fragment(), Item3Fragment())
    private val itemIds = arrayOf(R.id.item1, R.id.item2, R.id.item3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)

        secondaryVP.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun getItemCount(): Int = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }
        // 页面预加载
        secondaryVP.offscreenPageLimit = fragments.size

        // 若不想滑动切换页面时设置
        //secondaryVP.isUserInputEnabled = false
        secondaryVP.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                secondaryBtmNavView.selectedItemId = itemIds[position]
            }
        })

        secondaryBtmNavView.setOnNavigationItemSelectedListener {
            secondaryVP.currentItem = itemIds.indexOf(it.itemId)
            true
        }
    }
}