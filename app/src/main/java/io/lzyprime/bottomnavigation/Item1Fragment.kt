package io.lzyprime.bottomnavigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.item1_fragment.*

class Item1Fragment:Fragment(R.layout.item1_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Item1", "onViewCreated")
        item1_btn.setOnClickListener {
            startActivity(Intent(context, SecondaryActivity::class.java))
        }
    }
}