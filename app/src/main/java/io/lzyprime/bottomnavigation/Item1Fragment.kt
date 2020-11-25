package io.lzyprime.bottomnavigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import io.lzyprime.bottomnavigation.databinding.Item1FragmentBinding


class Item1Fragment:Fragment(R.layout.item1_fragment) {
    private lateinit var binding: Item1FragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = Item1FragmentBinding.bind(view)

        Log.d("Item1", "onViewCreated")
        binding.item1Btn.setOnClickListener {
            startActivity(Intent(context, SecondaryActivity::class.java))
        }
    }
}