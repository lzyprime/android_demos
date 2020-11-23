package io.lzyprime.mvvmdemo.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.lzyprime.mvvmdemo.data.UnsplashRepository
import io.lzyprime.mvvmdemo.data.bean.Photo
import io.lzyprime.mvvmdemo.data.bean.Response
import kotlinx.coroutines.launch

class ListPhotoViewModel @ViewModelInject constructor(private val repository: UnsplashRepository) :
    ViewModel() {
    val listPhotos:MutableLiveData<List<Photo>> = MutableLiveData()

    fun refreshListPhotos() = viewModelScope.launch {
        when(val res = repository.listPhoto()){
            is Response.Success -> listPhotos.value = res.data
            is Response.Failed -> listPhotos.value = emptyList()
        }
    }
}