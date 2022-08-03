package io.lzyprime.definitely.ui.utils

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

enum class PermissionState {
    GRANTED, DENIED, SHOW_RATIONALE
}

private class RequestPermissionCallback : ActivityResultCallback<Boolean> {
    var callback: (PermissionState) -> Unit = {}
    override fun onActivityResult(result: Boolean?) {
        callback.invoke(if (result == true) PermissionState.GRANTED else PermissionState.DENIED)
    }
}

fun Fragment.registerCheckPermissionLauncher(): (permission: String, callback:(PermissionState) -> Unit) -> Unit {
    val requestPermissionCallback = RequestPermissionCallback()
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        requestPermissionCallback,
    )
    return { permission, callback ->
        requestPermissionCallback.callback = callback
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> callback(PermissionState.GRANTED)
            shouldShowRequestPermissionRationale(permission) -> callback(PermissionState.SHOW_RATIONALE)
            else -> requestPermissionLauncher.launch(permission)
        }
    }
}