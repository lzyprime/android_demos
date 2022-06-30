package io.lzyprime.definitely.ui.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

inline fun LifecycleOwner.launchWithRepeat(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend () -> Unit
) = lifecycleScope.launch {
    repeatOnLifecycle(state) {
        block()
    }
}