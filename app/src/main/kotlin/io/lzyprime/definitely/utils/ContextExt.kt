package io.lzyprime.definitely.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.findActivity():Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        val next = context.baseContext
        if(context == next) return null
        context = next
    }
    return null
}