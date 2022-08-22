package io.lzyprime.definitely.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SelectListDialog private constructor(
    private val resIdItems: Array<Int>? = null,
    private val strItems: Array<String>? = null,
    private val onClick: (Int) -> Unit,
) : DialogFragment() {
    constructor(items: Array<Int>, onClick: (Int) -> Unit) : this(
        resIdItems = items,
        onClick = onClick
    )

    constructor(items: Array<String>, onClick: (Int) -> Unit) : this(
        strItems = items,
        onClick = onClick
    )

    private val items: Array<String> by lazy {
        strItems
            ?: resIdItems?.map { requireContext().getString(it) }?.toTypedArray()
            ?: emptyArray()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext(), theme)
            .setItems(items) { i, index ->
                onClick(index)
                i.dismiss()
            }.create()
}