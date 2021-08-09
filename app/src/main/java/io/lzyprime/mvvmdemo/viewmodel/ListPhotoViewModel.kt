package io.lzyprime.mvvmdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lzyprime.mvvmdemo.model.UnsplashRepository
import io.lzyprime.mvvmdemo.model.bean.Photo
import io.lzyprime.mvvmdemo.model.bean.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPhotoViewModel @Inject constructor(private val repository: UnsplashRepository) :
    ViewModel() {
    val listPhotos:MutableLiveData<List<Photo>> = MutableLiveData()

    fun refreshListPhotos() = viewModelScope.launch {
        when(val res = repository.listPhoto()){
            is Response.Success -> listPhotos.value = res.data
            is Response.Failed -> listPhotos.value = emptyList()
        }
    }
}