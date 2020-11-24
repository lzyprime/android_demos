package io.lzyprime.mvvmdemo.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import io.lzyprime.mvvmdemo.R
import io.lzyprime.mvvmdemo.databinding.FragmentDetailBinding
import io.lzyprime.mvvmdemo.extension.viewBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val args by navArgs<DetailFragmentArgs>()
    private val binding by viewBinding<FragmentDetailBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.load(args.imageSrc)
    }
}