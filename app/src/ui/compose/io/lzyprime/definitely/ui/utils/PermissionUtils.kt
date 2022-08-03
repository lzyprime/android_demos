package io.lzyprime.definitely.ui.utils

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import io.lzyprime.definitely.utils.findActivity

enum class PermissionStatus { GRANTED, DENIED, SHOW_RATIONALE }

@Composable
fun rememberCheckPermissionFunc(): (String, (PermissionStatus) -> Unit) -> Unit {
    var onResult by remember {
        mutableStateOf<(PermissionStatus) -> Unit>({})
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            onResult(if (it) PermissionStatus.GRANTED else PermissionStatus.DENIED)
        })
    val context = LocalContext.current
    return remember {
        { permission, callback ->
            onResult = callback
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> callback(PermissionStatus.GRANTED)
                context.findActivity()
                    ?.shouldShowRequestPermissionRationale(permission) == true -> callback(
                    PermissionStatus.SHOW_RATIONALE
                )
                else -> requestPermissionLauncher.launch(permission)
            }
        }
    }
}