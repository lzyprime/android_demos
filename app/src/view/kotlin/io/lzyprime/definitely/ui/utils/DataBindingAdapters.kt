//package io.lzyprime.definitely.ui.utils
//
//import android.view.View
//import android.widget.ImageView
//import androidx.annotation.AttrRes
//import androidx.databinding.BindingAdapter
//import coil.load
//import coil.loadAny
//import com.google.android.material.card.MaterialCardView
//
//@BindingAdapter(value = ["backgroundColor"])
//fun viewBackgroundColor(v: View, @AttrRes colorResId: Int) {
//    val color = if (colorResId != 0) v.context.themeColor(colorResId) else 0
//    if (v is MaterialCardView) {
//        v.setCardBackgroundColor(color)
//    } else {
//        v.setBackgroundColor(color)
//    }
//}
//
//@BindingAdapter(value = ["srcCompat"])
//fun imageViewLoadAny(v: ImageView, src: Any?) {
//    v.load(src)
//}
//
//@BindingAdapter(value = ["isGone"])
//fun viewIsGone(v:View, isGone: Boolean?) {
//    v.visibility = if(isGone == true) {
//        View.GONE
//    } else {
//        View.VISIBLE
//    }
//}