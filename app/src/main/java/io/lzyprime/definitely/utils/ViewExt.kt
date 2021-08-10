package io.lzyprime.definitely.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

///**
// * 显示时常为 [Toast.LENGTH_SHORT] 的 [Toast].
// *
// * @param text 要显示的文本,
// * 当[Context]或[text]为空时，不显示[Toast]
// *
// * 调用: `context toast "toast text"` 或
// * `context.toast("toast text")`.
// * */
//fun Context?.toast(text: CharSequence?) {
//    val applicationContext = this?.applicationContext ?: return
//    if (!text.isNullOrEmpty()) {
//        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
//    }
//}
//
//fun Context?.toast(@StringRes resId: Int) = toast(this?.getString(resId))

/** 禁用toast, 使用snackBar. */

fun View.snackBar(
    text: CharSequence,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_SHORT
) =
    Snackbar.make(this, text, duration)

fun View.snackBar(
    resId: Int,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_SHORT
) =
    Snackbar.make(this, resId, duration)

fun View.showSoftKeyboard() {
    if (requestFocus()) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View.hideSoftKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
        windowToken,
        InputMethodManager.RESULT_UNCHANGED_SHOWN
    )
    clearFocus()
}

fun Context.themeColor(@AttrRes resId: Int): Int =
    MaterialColors.getColor(this, resId, "theme color: $resId get failed")