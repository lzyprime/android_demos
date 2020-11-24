package io.lzyprime.mvvmdemo.extension

import android.app.AlertDialog
import android.content.Context

fun Context.showDialog(context: Context) = AlertDialog.Builder(context)