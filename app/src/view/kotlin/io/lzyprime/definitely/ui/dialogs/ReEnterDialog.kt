package io.lzyprime.definitely.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.lzyprime.definitely.R

data class ReEnterDialog(
    @StringRes private val title: Int,
    @StringRes private val negativeButtonText: Int = R.string.enter,
    @StringRes private val neutralButtonText: Int = R.string.cancel,
    private val cancelable: Boolean = true,
    private val onCancel:() -> Unit = {},
    private val onEnter: () -> Unit,
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext(), theme)
            .setTitle(title)
            .setCancelable(cancelable)
            .setNegativeButton(negativeButtonText) { di, _ ->
                di.dismiss()
                onEnter()
            }
            .setNeutralButton(neutralButtonText) { di, _ ->
                    di.dismiss()
                onCancel()
            }
            .setOnCancelListener {
                onCancel()
            }
            .create()
}