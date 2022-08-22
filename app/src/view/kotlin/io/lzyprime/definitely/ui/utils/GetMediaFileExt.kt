package io.lzyprime.definitely.ui.utils

import android.graphics.Bitmap
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

private data class TakePictureCallback(
    var callback: (Bitmap?) -> Unit = {},
):ActivityResultCallback<Bitmap?> {
    override fun onActivityResult(result: Bitmap?) {
        callback(result)
    }
}
fun Fragment.registerTakePicturePreviewLauncher(): ((Bitmap?) -> Unit) -> Unit {
    val takePictureCallback = TakePictureCallback()
    val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview(), takePictureCallback)
    return { callback ->
        takePictureCallback.callback = callback
        takePicture.launch(null)
    }
}