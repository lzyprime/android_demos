package io.lzyprime.mvvmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.lzyprime.mvvmdemo.databinding.ActivityMainBinding
import io.lzyprime.mvvmdemo.viewmodels.ListPhotoViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val model: ListPhotoViewModel by viewModels()

    init {
        lifecycleScope.launchWhenCreated {
            model.refreshListPhotos()
        }
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.refreshBtn.setOnClickListener {
            model.refreshListPhotos()
        }

        binding.photoList.layoutManager = GridLayoutManager(this, 2)
        model.listPhotos.observe(this) { photos ->
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
                        Glide.with(this).load(photo.urls.raw).into(this)
                    }
                }

                override fun getItemCount(): Int = photos.size
            }
        }
    }
}