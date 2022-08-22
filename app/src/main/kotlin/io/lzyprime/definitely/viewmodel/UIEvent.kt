package io.lzyprime.definitely.viewmodel

sealed interface UIEvent {
    data class ShowSnackBar(val strId:Int) : UIEvent
}