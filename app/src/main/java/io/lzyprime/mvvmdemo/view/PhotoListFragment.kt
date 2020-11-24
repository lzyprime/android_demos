package io.lzyprime.mvvmdemo.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import io.lzyprime.mvvmdemo.R
import io.lzyprime.mvvmdemo.databinding.FragmentPhotoListBinding
import io.lzyprime.mvvmdemo.extension.viewBinding
import io.lzyprime.mvvmdemo.viewmodel.ListPhotoViewModel

class PhotoListFragment : Fragment(R.layout.fragment_photo_list) {
    private val model: ListPhotoViewModel by activityViewModels()
    private val binding:FragmentPhotoListBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.refreshBtn.setOnClickListener {
            model.refreshListPhotos()
        }

        binding.photoList.layoutManager = GridLayoutManager(context, 2)
        model.listPhotos.observe(viewLifecycleOwner) { photos ->
            binding.photoList.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder = object : RecyclerView.ViewHolder(
                    ImageView(parent.context)
                ) {}

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    val photo = photos[position]

                    with(holder.itemView as ImageView) {
                        load(photo.urls.raw)
                        setOnClickListener {
                            val directions = PhotoListFragmentDirections.actionPhotoListFragmentToDetailFragment(photo.urls.raw)
                            this@PhotoListFragment.findNavController().navigate(directions)
                        }
                    }
                }

                override fun getItemCount(): Int = photos.size
            }
        }
    }

}